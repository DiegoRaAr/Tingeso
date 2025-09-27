import React, { useState, useEffect } from 'react';
import toolService from '../services/tool.service';
import clientService from '../services/client.service';
import loanService from '../services/loan.service';

const MakeLoan = () => {
  const [tools, setTools] = useState([]);
  const [clients, setClients] = useState([]);
  const [selectedTools, setSelectedTools] = useState([]);
  const [selectedClient, setSelectedClient] = useState('');
  const [endDate, setEndDate] = useState('');

  useEffect(() => {
    toolService.getAllTools().then(r => setTools(r.data));
    clientService.getAllClients().then(r => setClients(r.data));
  }, []);

  const handleToolSelect = (toolId) => {
    setSelectedTools(prev => 
      prev.includes(toolId) 
        ? prev.filter(id => id !== toolId)
        : [...prev, toolId]
    );
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const loanData = {
      initDate: new Date(),
      endDate: new Date(endDate),
      stateLoan: 'ACTIVO',
      penaltyLoan: 0,
      tool: selectedTools.map(id => ({ idTool: id })),
      idClient: { idClient: selectedClient }
    };

    try {
      await loanService.createLoan(loanData);
      alert('Préstamo creado exitosamente');
      // Limpiar formulario
      setSelectedTools([]);
      setSelectedClient('');
      setEndDate('');
    } catch (error) {
      console.error('Error:', error);
      alert('Error al crear préstamo');
    }
  };

  return (
    <div>
      <h2>Crear Préstamo</h2>
      <form onSubmit={handleSubmit}>
        {/* Seleccionar Cliente */}
        <div className="mb-3">
          <label className="form-label">Cliente</label>
          <select 
            className="form-select" 
            value={selectedClient} 
            onChange={(e) => setSelectedClient(e.target.value)}
            required
          >
            <option value="">Seleccionar cliente</option>
            {clients.map(client => (
              <option key={client.idClient} value={client.idClient}>
                {client.nameClient} - {client.rutClient}
              </option>
            ))}
          </select>
        </div>

        {/* Seleccionar Herramientas */}
        <div className="mb-3">
          <label className="form-label">Herramientas</label>
          <div className="row">
            {tools.map(tool => (
              <div key={tool.idTool} className="col-md-4 mb-2">
                <div className="form-check">
                  <input
                    className="form-check-input"
                    type="checkbox"
                    id={`tool-${tool.idTool}`}
                    checked={selectedTools.includes(tool.idTool)}
                    onChange={() => handleToolSelect(tool.idTool)}
                  />
                  <label className="form-check-label" htmlFor={`tool-${tool.idTool}`}>
                    {tool.nameTool} - Stock: {tool.stockTool}
                  </label>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Fecha de Devolución */}
        <div className="mb-3">
          <label className="form-label">Fecha de Devolución</label>
          <input
            type="date"
            className="form-control"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
            required
          />
        </div>

        <button type="submit" className="btn btn-primary">
          Crear Préstamo
        </button>
      </form>
    </div>
  );
};

export default MakeLoan;