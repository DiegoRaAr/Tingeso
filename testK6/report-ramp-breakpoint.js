import http from 'k6/http';
import { check, sleep } from 'k6';

// 500 datos aprox empieza a dar mas de 2 segundos de respuesta

export const options = {
  stages: [
    { duration: '30s', target: 100 },
    { duration: '30s', target: 200 },
  ],
  thresholds: {
    http_req_duration: ["p(95)<2000"],
  },
};

const BASE_URL = "http://localhost";

// Diferentes rangos de fechas para probar
const dateRanges = [
  { init: '2024-01-01', end: '2024-12-31' },
  { init: '2024-06-01', end: '2024-06-30' },
  { init: '2024-09-01', end: '2024-09-30' },
  { init: '2025-01-01', end: '2025-12-31' },
  { init: '2026-01-01', end: '2026-02-28' },
];

export default function () {
  // TEST 1: GET All Loans
  const allLoansResponse = http.get(`${BASE_URL}/api/v1/loan/`);
  check(allLoansResponse, {
    'getAllLoans: status is 200': (r) => r.status === 200,
    'getAllLoans: returns array': (r) => Array.isArray(r.json()),
  });

  // TEST 2: GET Restricted Clients
  const restrictedClientsResponse = http.get(`${BASE_URL}/api/v1/client/restricted-clients`);
  check(restrictedClientsResponse, {
    'getRestrictedClients: status is 200': (r) => r.status === 200,
    'getRestrictedClients: returns array': (r) => Array.isArray(r.json()),
  });

  sleep(1);
}
