import React, { useState, useEffect } from 'react';
import toolService from '../services/tool.service';
import loanService from '../services/loan.service';
import { useLocation, useNavigate } from "react-router-dom";
import DatePicker, { registerLocale } from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import es from 'date-fns/locale/es';

registerLocale('es', es);

const MakeLoan = () => {
  const location = useLocation();
  const client = location.state?.client;

  const navigate = useNavigate();

  const [previewPrice, setPreviewPrice] = useState(null);

  const [tools, setTools] = useState([]);
  const [selectedTools, setSelectedTools] = useState([]);
  const [endDate, setEndDate] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    toolService.getAllTools().then(r => setTools(r.data));
  }, []);

  const handleToolSelect = (toolId) => {
    setSelectedTools(prev =>
      prev.includes(toolId)
        ? prev.filter(id => id !== toolId)
        : [...prev, toolId]
    );
  };

  const handlePreview = () => {
    if (!endDate || selectedTools.length === 0) {
      alert("Selecciona herramientas y fecha de devolución");
      return;
    }

    const days =
      Math.ceil(
        (endDate.getTime() - new Date().getTime()) /
        (1000 * 60 * 60 * 24)
      ) + 1 || 1;

    const total = tools
      .filter(tool => selectedTools.includes(tool.idTool))
      .reduce((sum, tool) => sum + tool.dailyCharge * days, 0);

    setPreviewPrice(total);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const loanData = {
      initDate: new Date(),
      endDate: endDate,
      stateLoan: 'ACTIVO',
      penaltyLoan: 0,
      tool: selectedTools.map(id => ({ idTool: id })),
      idClient: { idClient: client.idClient }
    };

    try {
      await loanService.createLoan(loanData);
      alert('Préstamo creado exitosamente');
      setSelectedTools([]);
      setEndDate(null);
      navigate(`/loans-by-rut/${client.rutClient}`);
    } catch (error) {
      console.error('Error:', error);
      alert('Error al crear préstamo');
    }
  };

  return (
    <div>
      <h1 className="text-start fs-2 my-1 mb-4">{client.nameClient} - {client.rutClient}</h1>
      <form onSubmit={handleSubmit}>
        
        <div className="mb-3">
          <div className="d-flex justify-content-between align-items-center mb-2">
            <label className="form-label fw-bold fs-4 mb-0">Seleccionar Herramientas</label>
            <input
              type="text"
              className="form-control"
              style={{ width: '300px' }}
              placeholder="Buscar herramienta..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          <div style={{ height: '450px', overflowY: 'scroll', border: '1px solid #dee2e6', borderRadius: '5px' }}>
            <table className="table table-striped table-hover mb-0">
              <thead style={{ position: 'sticky', top: 0, backgroundColor: '#f8f9fa', zIndex: 1 }}>
                <tr>
                  <th>Herramienta</th>
                  <th>Cargo Diario</th>
                  <th>Cargo por Atraso</th>
                  <th>Stock</th>
                  <th>Acción</th>
                </tr>
              </thead>
              <tbody>
                {tools
                  .filter(tool => tool.stockTool >= 1 && tool.stateTool === "ACTIVA")
                  .filter(tool => tool.nameTool.toLowerCase().includes(searchTerm.toLowerCase()))
                  .map(tool => (
                    <tr key={tool.idTool}>
                      <td>{tool.nameTool}</td>
                      <td>{tool.dailyCharge}</td>
                      <td>{tool.lateCharge}</td>
                      <td>{tool.stockTool}</td>
                      <td>
                        <button
                          type="button"
                          className={`btn btn-sm ${selectedTools.includes(tool.idTool) ? 'btn-danger' : 'btn-success'}`}
                          onClick={() => handleToolSelect(tool.idTool)}
                        >
                          {selectedTools.includes(tool.idTool) ? 'Quitar' : 'Agregar'}
                        </button>
                      </td>
                    </tr>
                  ))}
              </tbody>
            </table>
          </div>
        </div>

        <div className="mb-3 d-flex align-items-center gap-5">
          <label className="fw-bold fs-4">Seleccione la fecha de devolución </label>
          <div className="ms-auto" style={{ marginRight: '0%' }}>
            <DatePicker
              selected={endDate}
              onChange={(date) => setEndDate(date)}
              locale="es"
              dateFormat="dd/MM/yyyy"
              minDate={new Date()}
              className="form-control form-control-lg"
              placeholderText="Selecciona una fecha"
              showPopperArrow={false}
              required
              calendarClassName="datepicker-large"
            />
          </div>
        </div>

        {previewPrice !== null && (
          <div className="alert alert-primary mb-3">
            <strong>Precio total del préstamo:</strong> ${previewPrice}
          </div>
        )}
        
        <div className="mb-3">          
        <button
          type="submit"
          className="btn btn-success mx-2"
        >
          Crear Préstamo
        </button>

        <button
          type="button"
          className="btn btn-info mx-2"
          onClick={handlePreview}
        >
          Vista previa precio
        </button>

        <button
          type="button"
          className="btn btn-warning mx-2"
          onClick={() => navigate(-1)}
        >
          Cancelar
        </button>

        </div>

      </form>
    </div>
  );
};

export default MakeLoan;