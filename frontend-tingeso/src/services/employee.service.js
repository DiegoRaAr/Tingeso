import httpEmployee from '../http-common';

const getAllEmployees = () => httpEmployee.get('/v1/employee/');

const createEmployee = (data) => httpEmployee.post('/v1/employee/', data);

const getEmployeeById = (id) => httpEmployee.get(`/v1/employee/${id}`);

const updateEmployee = (id, data) => httpEmployee.put(`/v1/employee/${id}`, data);

const deleteEmployee = (id) => httpEmployee.delete(`/v1/employee/${id}`);

export default {
  getAllEmployees, createEmployee, getEmployeeById, updateEmployee, deleteEmployee,
};
