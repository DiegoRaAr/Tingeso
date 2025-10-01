import httpLoan from '../http-common';

const getAllLoans = () => {
    return httpLoan.get('/api/v1/loan/');
}

const createLoan = data => {
    return httpLoan.post('/api/v1/loan/', data);
}

const getLoanById = id => {
    return httpLoan.get(`/api/v1/loan/${id}`);
}

const updateLoan = (id, data) => {
    return httpLoan.put(`/api/v1/loan/${id}`, data);
}

const deleteLoan = id => {
    return httpLoan.delete(`/api/v1/loan/${id}`);
}

const getLoansByRut = rut => {
    return httpLoan.get(`/api/v1/loan/by-rut/${rut}`);
}

const getToolsByLoanId = id => {
    return httpLoan.get(`/api/v1/loan/tools-by-loan/${id}`);
}

export default { getAllLoans, createLoan, getLoanById, updateLoan, deleteLoan , getLoansByRut, getToolsByLoanId};