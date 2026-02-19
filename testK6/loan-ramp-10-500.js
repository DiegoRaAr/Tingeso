import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

const iterationCounter = new Counter('iterations');

// 470 usuarios simultaneos empieza a fallar
export const options = {
  stages: [
    { duration: '30s', target: 10 },
    { duration: '30s', target: 100 },
    { duration: '30s', target: 200 },
    { duration: '30s', target: 350 },
    { duration: '30s', target: 500 },
    { duration: '30s', target: 0 },
  ],
  thresholds: {
    http_req_failed: ["rate<0.01"],      
    http_req_duration: ["p(95)<500"],  
  },
};

const BASE_URL = 'http://localhost/api/v1';

const availableToolIds = [1, 2, 3];

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
  const clientRut = `${10000000 + iterationNumber}-${iterationNumber % 10}`;
  const clientPayload = JSON.stringify({
    rutClient: clientRut,
    nameClient: `usuario ${iterationNumber}`,
    stateClient: 'ACTIVO',
    emailClient: `usuario${iterationNumber}@test.com`,
    phoneNumberClient: `+56912345${String(iterationNumber).padStart(3, '0')}`
  });

  const createClientResponse = http.post(`${BASE_URL}/client/`, clientPayload, params);
  
  const clientCreated = check(createClientResponse, {
    'client: status is 200': (r) => r.status === 200,
    'client: has client id': (r) => {
      try {
        const body = JSON.parse(r.body);
        return body.idClient !== undefined;
      } catch {
        return false;
      }
    },
  });

  if (!clientCreated || createClientResponse.status !== 200) {
    sleep(1);
    return;
  }

  let clientId;
  try {
    const clientData = JSON.parse(createClientResponse.body);
    clientId = clientData.idClient;
  } catch (e) {
    sleep(1);
    return;
  }

  // PASO 2: Crear préstamo con el cliente recién creado
  const initDate = new Date();
  const endDate = new Date();
  endDate.setDate(endDate.getDate() + 7);

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
      
      if (!responseBody.success) {
        sleep(1);
        return;
      }
      
      loanId = responseBody.data.idLoan;
      
      const possibleValues = [0, 5000, 10000];
      const totalValue = possibleValues[Math.floor(Math.random() * possibleValues.length)];
      
      const finishResponse = http.put(`${BASE_URL}/loan/finish-loan/${loanId}/${totalValue}`, null, params);
      
      check(finishResponse, {
        'finish: status is 200': (r) => r.status === 200,
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
      // Error handling
    }
  }

  sleep(1);
}
