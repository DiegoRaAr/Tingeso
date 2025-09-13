import httpAdmin from "../http-common";

const getAllAdmins = () => {
    return httpAdmin.get('/api/v1/admin/');
}

const createAdmin = data => {
    return httpAdmin.post('/api/v1/admin/', data);
}

const getAdminById = id => {
    return httpAdmin.get(`/api/v1/admin/${id}`);
}

const updateAdmin = (id, data) => {
    return httpAdmin.put(`/api/v1/admin/${id}`, data);
}

const deleteAdmin = id => {
    return httpAdmin.delete(`/api/v1/admin/${id}`);
}

export default { getAllAdmins, createAdmin, getAdminById, updateAdmin, deleteAdmin };
