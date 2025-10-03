import httpKardex from "../http-common";

const getAllKardex = () => {
    return httpKardex.get('/api/v1/kardex/');
}           

const createKardex = data => {
    return httpKardex.post('/api/v1/kardex/', data);
}

const getKardexById = id => {
    return httpKardex.get(`/api/v1/kardex/${id}`);
}

const updateKardex = (id, data) => {
    return httpKardex.put(`/api/v1/kardex/${id}`, data);
}

const deleteKardex = id => {
    return httpKardex.delete(`/api/v1/kardex/${id}`);
}

export default { getAllKardex, createKardex, getKardexById, updateKardex, deleteKardex };