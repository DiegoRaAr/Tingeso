import httpTool from "../http-common";

const getAllTools = () => {
    return httpTool.get('/api/v1/tool/');
}

const createTool = data => {
    return httpTool.post('/api/v1/tool/', data);
}

const getToolById = id => {
    return httpTool.get(`/api/v1/tool/${id}`);
}

const updateTool = (id, data) => {
    return httpTool.put(`/api/v1/tool/${id}`, data);
}

const deleteTool = id => {
    return httpTool.delete(`/api/v1/tool/${id}`);
}

const addToolNumber = (id, number) => {
    return httpTool.put(`/api/v1/tool/add-tool-number/${id}`, number);
}

export default { getAllTools, createTool, getToolById, updateTool, deleteTool, addToolNumber };