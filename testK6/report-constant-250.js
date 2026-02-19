import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 250,              
  duration: "30s",      
  thresholds: {
    http_req_failed: ["rate<0.05"],      
    http_req_duration: ["p(95)<2000"],    
  },
};

const BASE_URL = 'http://localhost/api/v1';

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
  const allLoansResponse = http.get(`${BASE_URL}/loan/`, params);
  check(allLoansResponse, {
    'getAllLoans: status is 200': (r) => r.status === 200,
    'getAllLoans: response time < 2000ms': (r) => r.timings.duration < 2000,
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
  const restrictedClientsResponse = http.get(`${BASE_URL}/client/restricted-clients`, params);
  check(restrictedClientsResponse, {
    'getRestrictedClients: status is 200': (r) => r.status === 200,
    'getRestrictedClients: response time < 2000ms': (r) => r.timings.duration < 2000,
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
    `${BASE_URL}/tool/best-tools-by-range-date/${randomRange.init}/${randomRange.end}`,
    params
  );
  
  check(bestToolsResponse, {
    'getBestTools: status is 200': (r) => r.status === 200,
    'getBestTools: response time < 2000ms': (r) => r.timings.duration < 2000,
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
