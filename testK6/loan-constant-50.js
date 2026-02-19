import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

// Contador para las iteraciones
const iterationCounter = new Counter('iterations');

export const options = {
  vus: 50,              
  duration: "30s",      
  thresholds: {
    http_req_failed: ["rate<0.01"],     
    http_req_duration: ["p(95)<500"], 
  },
};

const BASE_URL = 'http://localhost/api/v1';

// Herramientas disponibles para el test
const availableToolIds = [1, 2, 3]; // Solo herramientas id 1, 2 o 3

// Contador global para generar nombres únicos
let globalIterationCount = 0;

export default function () {
  iterationCounter.add(1);
  const iterationNumber = ++globalIterationCount;
  
  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  // PASO 1: Crear un nuevo cliente
  const clientRut = '11111111-1';
  const clientPayload = JSON.stringify({
    rutClient: clientRut,
    nameClient: `usuario test`,
    stateClient: 'ACTIVO',
    emailClient: `usuario_test@test.com`,
    phoneNumberClient: `+5691234500`
  });

  const createClientResponse = http.post(`${BASE_URL}/client/`, clientPayload, params);
  
  const clientCreated = check(createClientResponse, {
    'client: status is 200': (r) => r.status === 200,
    'client: response time < 2000ms': (r) => r.timings.duration < 2000,
    'client: has client id': (r) => {
      try {
        const body = JSON.parse(r.body);
        return body.idClient !== undefined;
      } catch {
        return false;
      }
    },
  });

  // Si el cliente no se creó, terminar esta iteración
  if (!clientCreated || createClientResponse.status !== 200) {
    console.error(`No se pudo crear el cliente en iteración ${iterationNumber}`);
    sleep(1);
    return;
  }

  let clientId;
  try {
    const clientData = JSON.parse(createClientResponse.body);
    clientId = clientData.idClient;
  } catch (e) {
    console.error(`Error al parsear datos del cliente: ${e}`);
    sleep(1);
    return;
  }

  // PASO 2: Crear préstamo con el cliente recién creado
  const initDate = new Date();
  const endDate = new Date();
  endDate.setDate(endDate.getDate() + 7);

  // Seleccionar una herramienta aleatoria de las disponibles
  const randomTool = availableToolIds[Math.floor(Math.random() * availableToolIds.length)];

  const loanPayload = JSON.stringify({
    initDate: initDate.toISOString(),
    endDate: endDate.toISOString(),
    stateLoan: 'ACTIVO',
    penaltyLoan: 0,
    idClient: {
      idClient: clientId
    },
    tool: [
      { idTool: randomTool }
    ]
  });

  const createLoanResponse = http.post(`${BASE_URL}/loan/`, loanPayload, params);
  
  const loanCreated = check(createLoanResponse, {
    'loan: status is 200': (r) => r.status === 200,
    'loan: response time < 2000ms': (r) => r.timings.duration < 2000,
    'loan: creation success': (r) => {
      try {
        const body = JSON.parse(r.body);
        return body.success === true && body.data && body.data.idLoan !== undefined;
      } catch {
        return false;
      }
    },
  });

  // PASO 3: Finalizar préstamo si se creó exitosamente
  if (loanCreated && createLoanResponse.status === 200) {
    let loanId;
    try {
      const responseBody = JSON.parse(createLoanResponse.body);
      
      // Verificar si la operación fue exitosa
      if (!responseBody.success) {
        console.log(`Préstamo no creado: ${responseBody.message}`);
        sleep(1);
        return;
      }
      
      loanId = responseBody.data.idLoan;
      
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
  sleep(1);
}