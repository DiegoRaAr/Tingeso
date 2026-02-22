import httpTool from '../http-common';

const getAllTools = () => httpTool.get('/v1/tool/');

const createTool = (data) => httpTool.post('/v1/tool/', data);

const getToolById = (id) => httpTool.get(`/v1/tool/${id}`);

const updateTool = (data) => httpTool.put('/v1/tool/', data);

const deleteTool = (id) => httpTool.delete(`/v1/tool/${id}`);

const subtractTool = (id) => httpTool.put(`/v1/tool/subtract-tool/${id}`);

const addTool = (id) => httpTool.put(`/v1/tool/add-tool/${id}`);

const getBestToolsByRangeDate = (initDate, endDate) => httpTool.get(`/v1/tool/best-tools-by-range-date/${initDate}/${endDate}`);

export default {
  getAllTools,
  createTool,
  getToolById,
  updateTool,
  deleteTool,
  subtractTool,
  addTool,
  getBestToolsByRangeDate,
};
