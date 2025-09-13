import httpEmployee from "../http-common";

const getAllEmployees = () => {
    return httpEmployee.get('/api/v1/employee/');
}

const createEmployee = data => {
    return httpEmployee.post('/api/v1/employee/', data);
}

const getEmployeeById = id => {
    return httpEmployee.get(`/api/v1/employee/${id}`);
}

const updateEmployee = (id, data) => {
    return httpEmployee.put(`/api/v1/employee/${id}`, data);
}

const deleteEmployee = id => {
    return httpEmployee.delete(`/api/v1/employee/${id}`);
}

export default { getAllEmployees, createEmployee, getEmployeeById, updateEmployee, deleteEmployee };