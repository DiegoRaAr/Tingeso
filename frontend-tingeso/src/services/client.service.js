import httpClient from "../http-common";

const getAllClients = () => {
    return httpClient.get('/api/v1/client/');
}

const createClient = data => {
    return httpClient.post('/api/v1/client/', data);
}

const getClientById = id => {
    return httpClient.get(`/api/v1/client/${id}`);
}

const getClientByRut = rut => {
    return httpClient.get(`/api/v1/client/by-rut/${rut}`);
}

const updateClient = (id, data) => {
    return httpClient.put(`/api/v1/client/${id}`, data);
}

const deleteClient = id => {
    return httpClient.delete(`/api/v1/client/${id}`);
}

const changeStateClient = (id) => {
    return httpClient.put(`/api/v1/client/change-state/${id}`);
}

const getRestrictedClients = () => {
    return httpClient.get('/api/v1/client/restricted-clients');
}

export default { getAllClients, createClient, getClientById, updateClient, deleteClient, getClientByRut, changeStateClient, getRestrictedClients };
