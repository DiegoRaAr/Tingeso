import http from 'k6/http';
import { check, sleep } from 'k6';

// Con mas de 400 usuarios, el sistema empieza a fallar.
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

const BASE_URL = 'http://localhost';

// Diferentes rangos de fechas para probar
const dateRanges = [
  { init: '2024-01-01', end: '2024-12-31' },
  { init: '2024-06-01', end: '2024-06-30' },
  { init: '2024-09-01', end: '2024-09-30' },
  { init: '2025-01-01', end: '2025-12-31' },
  { init: '2026-01-01', end: '2026-02-28' },
];

export default function () {
  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  // TEST 1: GET All Loans
  const allLoansResponse = http.get(`${BASE_URL}/api/v1/loan/`, params);
  check(allLoansResponse, {
    'getAllLoans: status is 200': (r) => r.status === 200,
    'getAllLoans: returns array': (r) => {
      try {
        const body = JSON.parse(r.body);
        return Array.isArray(body);
      } catch {
        return false;
      }
    },
  });

  // TEST 2: GET Restricted Clients
  const restrictedClientsResponse = http.get(`${BASE_URL}/api/v1/client/restricted-clients`, params);
  check(restrictedClientsResponse, {
    'getRestrictedClients: status is 200': (r) => r.status === 200,
    'getRestrictedClients: returns array': (r) => {
      try {
        const body = JSON.parse(r.body);
        return Array.isArray(body);
      } catch {
        return false;
      }
    },
  });

  // TEST 3: GET Best Tools By Range Date
  const randomRange = dateRanges[Math.floor(Math.random() * dateRanges.length)];
  const bestToolsResponse = http.get(
    `${BASE_URL}/api/v1/tool/best-tools-by-range-date/${randomRange.init}/${randomRange.end}`,
    params
  );
  
  check(bestToolsResponse, {
    'getBestTools: status is 200': (r) => r.status === 200,
    'getBestTools: returns array': (r) => {
      try {
        const body = JSON.parse(r.body);
        return Array.isArray(body);
      } catch {
        return false;
      }
    },
  });

  sleep(1);
}
