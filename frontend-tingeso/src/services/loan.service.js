import httpLoan from '../http-common';

const getAllLoans = () => httpLoan.get('/v1/loan/');

const createLoan = (data) => httpLoan.post('/v1/loan/', data);

const getLoanById = (id) => httpLoan.get(`/v1/loan/${id}`);

const updateLoan = (id, data) => httpLoan.put(`/v1/loan/${id}`, data);

const deleteLoan = (id) => httpLoan.delete(`/v1/loan/${id}`);

const getLoansByRut = (rut) => httpLoan.get(`/v1/loan/by-rut/${rut}`);

const getToolsByLoanId = (id) => httpLoan.get(`/v1/loan/tools-by-loan/${id}`);

const updatePenalty = (id) => httpLoan.put(`/v1/loan/update-penalty/${id}`);

const finishLoan = (id, total) => httpLoan.put(`/v1/loan/finish-loan/${id}/${total}`);

const getLoanByRangeDate = (startDate, endDate) => httpLoan.get(`/v1/loan/loans-by-range-date/${startDate}/${endDate}`);

const getNumLoanRestrinByRutClient = (rut) => httpLoan.get(`/v1/loan/num-loans-restringido/${rut}`);

const getNumActiveLoans = (rut) => httpLoan.get(`/v1/loan/num-active-loans/${rut}`);

export default {
  getAllLoans,
  createLoan,
  getLoanById,
  updateLoan,
  deleteLoan,
  getLoansByRut,
  getToolsByLoanId,
  updatePenalty,
  finishLoan,
  getLoanByRangeDate,
  getNumLoanRestrinByRutClient,
  getNumActiveLoans,
};
