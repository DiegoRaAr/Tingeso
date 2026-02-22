import httpKardex from '../http-common';

const getAllKardex = () => httpKardex.get('/v1/kardex/');

const createKardex = (data) => httpKardex.post('/v1/kardex/', data);

const getKardexById = (id) => httpKardex.get(`/v1/kardex/${id}`);

const updateKardex = (id, data) => httpKardex.put(`/v1/kardex/${id}`, data);

const deleteKardex = (id) => httpKardex.delete(`/v1/kardex/${id}`);

export default {
  getAllKardex, createKardex, getKardexById, updateKardex, deleteKardex,
};
