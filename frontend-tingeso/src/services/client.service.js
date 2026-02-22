import httpClient from '../http-common';

const getAllClients = () => httpClient.get('/v1/client/');

const createClient = (data) => httpClient.post('/v1/client/', data);

const getClientById = (id) => httpClient.get(`/v1/client/${id}`);

const getClientByRut = (rut) => httpClient.get(`/v1/client/by-rut/${rut}`);

const updateClient = (data) => httpClient.put('/v1/client/', data);

const deleteClient = (id) => httpClient.delete(`/v1/client/${id}`);

const changeStateClient = (id) => httpClient.put(`/v1/client/change-state/${id}`);

const getRestrictedClients = () => httpClient.get('/v1/client/restricted-clients');

export default {
  getAllClients,
  createClient,
  getClientById,
  updateClient,
  deleteClient,
  getClientByRut,
  changeStateClient,
  getRestrictedClients,
};
