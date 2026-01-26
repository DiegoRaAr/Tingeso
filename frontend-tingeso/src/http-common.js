import axios from 'axios';
import keycloak from './services/keycloak';

const backendServer = import.meta.env.VITE_BACKEND_SERVER;
const backendPort = import.meta.env.VITE_BACKEND_PORT;

// Para desarrollo con backend en Docker: http://localhost:70/api
// Para desarrollo con backend local: http://localhost:8090/api
const api = axios.create({
    baseURL: 'http://localhost:70/api',
    headers: {
        'Content-Type': 'application/json'
    }
});

api.interceptors.request.use(async (config) => {
    if (keycloak.authenticated) {
        await keycloak.updateToken(30);
        //config.headers.Authorization = `Bearer ${keycloak.token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

export default api;