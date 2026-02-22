import React, { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import toolService from '../services/tool.service';

function AddTool() {
  const location = useLocation();
  const toolToEdit = location.state?.tool;
  const navigate = useNavigate();

  const [tool, setTool] = useState({
    nameTool: '',
    categoryTool: '',
    totalValue: 0,
    stateTool: 'ACTIVA',
    stockTool: 1,
    repairCharge: 0,
    dailyCharge: 0,
    lateCharge: 0,
  });

  useEffect(() => {
    if (toolToEdit) {
      setTool({
        ...toolToEdit,
        totalValue: toolToEdit.totalValue ?? 0,
      });
    }
  }, [toolToEdit]);

  const handleChange = (e) => {
    const {
      name, value, type, checked,
    } = e.target;
    setTool({
      ...tool,
      [name]: type === 'checkbox' ? checked : value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (toolToEdit) {
      toolService.updateTool(tool)
        .then(() => {
          alert('Herramienta actualizada con éxito');
          navigate('/home');
        })
        .catch((error) => {
          console.log('Error al actualizar herramienta', error);
        });
    } else {
      toolService.createTool(tool)
        .then(() => {
          alert('Herramienta añadida con éxito');
          setTool({
            nameTool: '',
            categoryTool: '',
            stateTool: 'ACTIVA',
            totalValue: 0,
            stockTool: 1,
            repairCharge: 0,
            dailyCharge: 0,
            lateCharge: 0,
          });
        })
        .catch((error) => {
          console.log('Error al añadir herramienta', error);
        });
    }
  };

  return (
    <div className="container-fluid">
      <h2 className="text-start my-1 mb-4">
        {toolToEdit ? 'Editar herramienta' : 'Añadir Nueva Herramienta'}
      </h2>
      <h6 className="text-start my-3 mb-4">
        {toolToEdit ? 'En este apartado puede actualizar los datos de la herramienta seleccionada. A continuación, modifique los datos.' : 'En este apartado puede crear una herramienta nueva. A continuación ingrese los datos de la herramienta.'}
      </h6>
      <form onSubmit={handleSubmit}>
        <div className="mb-4 text-start">
          <label htmlFor="nameTool" className="form-label">Nombre herramienta</label>
          <input
            type="text"
            className="form-control"
            id="nameTool"
            required
            minLength="4"
            maxLength="50"
            name="nameTool"
            value={tool.nameTool}
            onChange={handleChange}
            placeholder="Ejemplo: Martillo"
          />
        </div>

        <div className="mb-4 text-start">
          <label htmlFor="categoryTool" className="form-label">Categoria herramienta</label>
          <select
            className="form-select"
            id="categoryTool"
            name="categoryTool"
            value={tool.categoryTool}
            onChange={handleChange}
            required
          >
            <option value="">Selecciona una opción</option>
            <option value="Manual">Manual</option>
            <option value="Electrica">Electrica</option>
            <option value="Pedestal">Pedestal</option>
          </select>
        </div>

        <div className="mb-4 text-start">
          <label htmlFor="totalValue" className="form-label">Valor total de la herramienta</label>
          <input
            type="number"
            className="form-control"
            id="totalValue"
            min="1"
            name="totalValue"
            value={tool.totalValue}
            onChange={handleChange}
          />
          <div className="form-text">Valor total de la herramienta, en caso de que se devuelva inutilizable, se cobra este precio como valor agregado</div>
        </div>

        <div className="mb-4 text-start">
          <label htmlFor="repairCharge" className="form-label">Cargo por daños leves</label>
          <input
            type="number"
            className="form-control"
            id="repairCharge"
            name="repairCharge"
            min="1"
            value={tool.repairCharge}
            onChange={handleChange}
          />
          <div className="form-text">Este es un valor agregado en caso de que la herramienta tenga daños leves a la hora de su devolución </div>
        </div>

        <div className="mb-4 text-start">
          <label htmlFor="dailyCharge" className="form-label">Cargo diario</label>
          <input
            type="number"
            className="form-control"
            id="dailyCharge"
            name="dailyCharge"
            min="1"
            value={tool.dailyCharge}
            onChange={handleChange}
          />
          <div className="form-text">Precio que se cobra diario por esta herramienta</div>
        </div>

        <div className="mb-4 text-start">
          <label htmlFor="lateCharge" className="form-label">Cargo por atraso</label>
          <input
            type="number"
            className="form-control"
            id="lateCharge"
            name="lateCharge"
            min="1"
            value={tool.lateCharge}
            onChange={handleChange}
          />
          <div className="form-text">Precio que empieza a cobrarse por cada día de atraso</div>
        </div>

        <div className="mb-4 text-start">
          <label htmlFor="stockTool" className="form-label">Stock inicial</label>
          <input
            type="number"
            className="form-control"
            id="stockTool"
            name="stockTool"
            min="1"
            required
            value={tool.stockTool}
            onChange={handleChange}
            placeholder="Ejemplo: 5"
          />
          <div className="form-text">Cantidad de unidades disponibles de esta herramienta</div>
        </div>

        <div className="d-flex gap-2 justify-content-center">
          <OverlayTrigger
            placement="top"
            overlay={(
              <Tooltip>
                {toolToEdit
                  ? 'Se guardarán los cambios realizados'
                  : 'Se agregará una nueva herramienta al sistema'}
              </Tooltip>
                    )}
          >
            <button
              type="submit"
              className="btn btn-primary"
            >
              {toolToEdit ? 'Actualizar herramienta' : 'Añadir herramienta'}
            </button>
          </OverlayTrigger>

          <OverlayTrigger
            placement="top"
            overlay={<Tooltip>Al volver se descartarán los cambios no guardados</Tooltip>}
          >
            <button
              type="button"
              className="btn btn-warning"
              onClick={() => navigate('/home')}
            >
              Volver
            </button>
          </OverlayTrigger>
        </div>
      </form>
    </div>
  );
}

export default AddTool;
