import httpAdmin from '../http-common';

const getAllAdmins = () => httpAdmin.get('/v1/admin/');

const createAdmin = (data) => httpAdmin.post('/v1/admin/', data);

const getAdminById = (id) => httpAdmin.get(`/v1/admin/${id}`);

const updateAdmin = (id, data) => httpAdmin.put(`/v1/admin/${id}`, data);

const deleteAdmin = (id) => httpAdmin.delete(`/v1/admin/${id}`);

export default {
  getAllAdmins, createAdmin, getAdminById, updateAdmin, deleteAdmin,
};
