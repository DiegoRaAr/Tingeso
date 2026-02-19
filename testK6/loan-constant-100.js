import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 100,              
  duration: "30s",      
  thresholds: {
    http_req_failed: ["rate<0.01"],      
    http_req_duration: ["p(95)<500"],    
  },
};

const BASE_URL = 'http://localhost/api/v1';

// Usuario de prueba fijo
const TEST_CLIENT_ID = 6; // RUT: 12345678-9

// Herramientas disponibles para el test
const availableToolIds = [1, 2, 3]; // Solo herramientas id 1, 2 o 3

export default function () {
  // Preparar payload para crear préstamo
  const initDate = new Date();
  const endDate = new Date();
  endDate.setDate(endDate.getDate() + 7); // 7 días después

  // Seleccionar una herramienta aleatoria de las disponibles
  const randomTool = availableToolIds[Math.floor(Math.random() * availableToolIds.length)];

  const payload = JSON.stringify({
    initDate: initDate.toISOString(),
    endDate: endDate.toISOString(),
    stateLoan: 'ACTIVO',
    penaltyLoan: 0,
    idClient: {
      idClient: TEST_CLIENT_ID
    },
    tool: [
      { idTool: randomTool }
    ]
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  // PASO 1: Crear préstamo
  const createResponse = http.post(`${BASE_URL}/loan/`, payload, params);
  
  const createSuccess = check(createResponse, {
    'create: status is 200 or 201': (r) => [200, 201].includes(r.status),
    'create: response time < 2000ms': (r) => r.timings.duration < 2000,
    'create: has loan id': (r) => {
      try {
        const body = JSON.parse(r.body);
        return body.idLoan !== undefined;
      } catch {
        return false;
      }
    },
  });

  // PASO 2: Finalizar préstamo si se creó exitosamente
  if (createSuccess && [200, 201].includes(createResponse.status)) {
    let loanId;
    try {
      const loanData = JSON.parse(createResponse.body);
      loanId = loanData.idLoan;
      
      // Generar totalValue aleatorio (0, 5000 o 10000)
      const possibleValues = [0, 5000, 10000];
      const totalValue = possibleValues[Math.floor(Math.random() * possibleValues.length)];
      
      // Finalizar préstamo con totalValue aleatorio (daños variables)
      const finishResponse = http.put(`${BASE_URL}/loan/finish-loan/${loanId}/${totalValue}`, null, params);
      
      check(finishResponse, {
        'finish: status is 200': (r) => r.status === 200,
        'finish: response time < 2000ms': (r) => r.timings.duration < 2000,
        'finish: loan finalized': (r) => {
          try {
            const body = JSON.parse(r.body);
            return body.stateLoan === 'FINALIZADO' || body.idLoan === loanId;
          } catch {
            return false;
          }
        },
      });
    } catch (e) {
      console.error(`Error al procesar préstamo: ${e}`);
    }
  }

  // Pausa entre iteraciones
  sleep(1);
}