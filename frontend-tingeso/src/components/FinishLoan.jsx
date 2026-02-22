import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import loanService from '../services/loan.service';
import '../App.css';

function FinishLoan() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [tools, setTools] = useState([]);
  const [loan, setLoan] = useState({});
  const [toolStates, setToolStates] = useState({});
  const [showPreview, setShowPreview] = useState(false);
  const [previewValues, setPreviewValues] = useState({
    valorReal: 0,
    valorAtraso: 0,
    valorDesperfecto: 0,
    total: 0,
  });

  useEffect(() => {
    loanService.getLoanById(id)
      .then((response) => {
        setTools(response.data.tool || []);
        setLoan(response.data);
      })
      .catch((error) => {
        console.log('Error al obtener prestamo', error);
      });
  }, [id]);

  const handleToolStateChange = (toolId, value) => {
    setToolStates((prev) => ({
      ...prev,
      [toolId]: value,
    }));
  };

  const handlePreview = () => {
    const valorReal = loan.totalLoan || 0;

    const valorAtraso = loan.penaltyLoan || 0;

    const allSelected = tools.every((tool) => toolStates[tool.idTool]);
    if (!allSelected) {
      alert('Debes seleccionar el estado de todas las herramientas.');
      return;
    }

    let valorDesperfecto = 0;
    tools.forEach((tool) => {
      const estado = toolStates[tool.idTool];
      if (estado === '2') {
        valorDesperfecto += tool.repairCharge || 0;
      } else if (estado === '3') {
        valorDesperfecto += tool.totalValue || 0;
      }
    });

    const total = valorReal + valorAtraso + valorDesperfecto;

    setPreviewValues({
      valorReal,
      valorAtraso,
      valorDesperfecto,
      total,
    });
    setShowPreview(true);
  };

  return (
    <div>
      <h2 className="text-start my-1 mb-4">Finalizar préstamo</h2>

      <h6 className="text-start my-3 mb-4">
        En este apartado puedes finalizar un préstamo. A continuación, selecciona el estado de
        cada herramienta y haz clic en &quot;Vista previa&quot; para ver el monto total a pagar.
      </h6>
      <table className="table">
        <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Herramienta</th>
            <th scope="col">Estado</th>
          </tr>
        </thead>
        <tbody>
          {tools.map((tool) => (
            <tr key={tool.idTool}>
              <th scope="row">{tool.idTool}</th>
              <td>{tool.nameTool}</td>
              <td>
                <select
                  className="form-select"
                  aria-label="Estado herramienta"
                  value={toolStates[tool.idTool] || ''}
                  onChange={(e) => handleToolStateChange(tool.idTool, e.target.value)}
                  required
                >
                  <option value="">Estado herramienta</option>
                  <option value="1">Buenas condiciones</option>
                  <option value="2">Daños leves</option>
                  <option value="3">Inutilizable</option>
                </select>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <OverlayTrigger
        placement="top"
        overlay={<Tooltip>Calcular el monto total antes de finalizar el préstamo</Tooltip>}
      >
        <button className="btn btn-primary mx-2" type="button" onClick={handlePreview}>
          Vista previa
        </button>
      </OverlayTrigger>

      {showPreview && (
        <div className="alert alert-info mt-4">
          <h5>Vista previa del préstamo</h5>
          <p>
            <strong>Valor real:</strong>
            {' '}
            $
            {previewValues.valorReal}
          </p>
          <p>
            <strong>Valor por atraso:</strong>
            {' '}
            $
            {previewValues.valorAtraso}
          </p>
          <p>
            <strong>Valor por desperfecto:</strong>
            {' '}
            $
            {previewValues.valorDesperfecto}
          </p>
          <hr />
          <p>
            <strong>Total a pagar:</strong>
            {' '}
            $
            {previewValues.total}
          </p>
        </div>
      )}

      <OverlayTrigger
        placement="top"
        overlay={<Tooltip>Completar el préstamo con el monto calculado</Tooltip>}
      >
        <span className="d-inline-block">
          <button
            className="btn btn-success mx-2 my-4"
            type="button"
            disabled={!showPreview}
            onClick={() => {
              loanService.finishLoan(id, previewValues.total)
                .then(() => {
                  alert('Préstamo finalizado con éxito');
                  const rutClient = loan.idClient?.rutClient;
                  if (rutClient) {
                    navigate(`/loans-by-rut/${rutClient}`);
                  } else {
                    navigate('/admin-client');
                  }
                })
                .catch((error) => {
                  console.log('Error al finalizar préstamo', error);
                  alert('Error al finalizar préstamo. Por favor, intente de nuevo.');
                });
            }}
            style={{ pointerEvents: showPreview ? 'auto' : 'none' }}
          >
            Finalizar préstamo
          </button>
        </span>
      </OverlayTrigger>

      <OverlayTrigger
        placement="top"
        overlay={<Tooltip>Regresar sin finalizar el préstamo</Tooltip>}
      >
        <button
          className="btn btn-warning mx-2 my-4"
          type="button"
          onClick={() => {
            const rutClient = loan.idClient?.rutClient;
            if (rutClient) {
              navigate(`/loans-by-rut/${rutClient}`);
            } else {
              navigate('/admin-client');
            }
          }}
        >
          Volver
        </button>
      </OverlayTrigger>
    </div>
  );
}

export default FinishLoan;
