import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 10,              
  duration: "30s",      
  thresholds: {
    http_req_failed: ["rate<0.01"],      
    http_req_duration: ["p(95)<500"],    
  },
};

const BASE_URL = 'http://localhost/api/v1';

// Solo herramientas con stock disponible
const availableToolIds = [1, 2, 3, 6]; // Martillo, Pala, Taladro, Esmeril
// Solo clientes ACTIVOS
const activeClientIds = [1, 2, 3, 4, 5];

export default function () {
  // Preparar payload para crear préstamo
  const initDate = new Date();
  const endDate = new Date();
  endDate.setDate(endDate.getDate() + 7); // 7 días después

  // Seleccionar aleatorios de las listas válidas
  const randomTool = availableToolIds[Math.floor(Math.random() * availableToolIds.length)];
  const randomClient = activeClientIds[Math.floor(Math.random() * activeClientIds.length)];

  const payload = JSON.stringify({
    initDate: initDate.toISOString(),
    endDate: endDate.toISOString(),
    stateLoan: 'ACTIVO',
    penaltyLoan: 0,
    idClient: {
      idClient: randomClient
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

  // POST: Crear préstamo
  const response = http.post(`${BASE_URL}/loan/`, payload, params);
  
  check(response, {
    'status is 200 or 201': (r) => [200, 201].includes(r.status),
    'response time < 2000ms': (r) => r.timings.duration < 2000,
    'has loan id': (r) => {
      try {
        const body = JSON.parse(r.body);
        return body.idLoan !== undefined;
      } catch {
        return false;
      }
    },
  });

  // Pausa entre requests
  sleep(1);
}