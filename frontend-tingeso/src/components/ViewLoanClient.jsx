import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import loanService from '../services/loan.service';
import clientService from '../services/client.service';

import '../App.css';

function ViewLoanClient() {
  const [loans, setLoans] = useState([]);
  const [client, setClient] = useState({
    rutClient: '',
    nameClient: '',
  });

  const { rut } = useParams();
  const navigate = useNavigate();
  const [showTools, setShowTools] = useState({});

  const toggleTools = (loanId) => {
    setShowTools((prev) => ({
      ...prev,
      [loanId]: !prev[loanId],
    }));
  };

  useEffect(() => {
    clientService.getClientByRut(rut)
      .then((response) => {
        setClient(response.data);
        setClient((prev) => ({
          ...prev,
          nameClient: response.data.nameClient,
        }));
      })
      .catch((error) => {
        console.log('Error al obtener cliente', error);
      });
    loanService.getLoansByRut(rut)
      .then((response) => {
        setLoans(response.data);
      })
      .catch((error) => {
        console.log('Error al obtener prestamos', error);
      });
  }, [rut]);

  return (
    <div>
      <h2>
        Se están visualizando los préstamos de
        {client.nameClient}
      </h2>

      <h6 className="text-start mb-4">En este apartado puedes revisar el historial de préstamos de este cliente, ver las herramientas asociadas a cada préstamo y finalizar los préstamos activos.</h6>
      <table className="table table-striped table-hover align-middle">
        <thead>
          <tr>
            <th>ID</th>
            <th>Fecha de inicio</th>
            <th>Fecha de fin</th>
            <th>Estado</th>
            <th>Multa</th>
            <th>Valor real</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {loans.map((loan) => (
            <React.Fragment key={loan.idLoan}>
              <tr>
                <td>{loan.idLoan}</td>
                <td>{loan.initDate ? new Date(loan.initDate).toLocaleDateString() : ''}</td>
                <td>{loan.endDate ? new Date(loan.endDate).toLocaleDateString() : ''}</td>
                <td>{loan.stateLoan}</td>
                <td>
                  $
                  {loan.penaltyLoan.toLocaleString()}
                </td>
                <td>
                  $
                  {loan.totalLoan ? loan.totalLoan.toLocaleString() : '0'}
                </td>
                <td>
                  {loan.stateLoan === 'ACTIVO' && (
                    <OverlayTrigger
                      placement="top"
                      overlay={<Tooltip>Completar y cerrar este préstamo activo</Tooltip>}
                    >
                      <button
                        className="btn btn-danger mx-2"
                        type="button"
                        onClick={() => { navigate(`/finish-loan/${loan.idLoan}`); }}
                      >
                        Finalizar prestamo
                      </button>
                    </OverlayTrigger>
                  )}
                  <OverlayTrigger
                    placement="top"
                    overlay={<Tooltip>{showTools[loan.idLoan] ? 'Ocultar la lista de herramientas' : 'Mostrar las herramientas del préstamo'}</Tooltip>}
                  >
                    <button
                      className="btn btn-info mx-2"
                      type="button"
                      onClick={() => toggleTools(loan.idLoan)}
                    >
                      <i className={`bi bi-chevron-${showTools[loan.idLoan] ? 'up' : 'down'}`} />
                      {showTools[loan.idLoan] ? 'Ocultar' : 'Ver'}
                      {' '}
                      herramientas
                    </button>
                  </OverlayTrigger>
                </td>
              </tr>
              {showTools[loan.idLoan] && (
                <tr>
                  <td colSpan="7">
                    <div className="card card-body">
                      <h6>Herramientas del préstamo:</h6>
                      {loan.tool && loan.tool.length > 0 ? (
                        <ul className="list-group">
                          {loan.tool.map((tool) => (
                            <li key={tool.idTool} className="list-group-item">
                              {tool.nameTool}
                              {' '}
                              -
                              {tool.categoryTool}
                            </li>
                          ))}
                        </ul>
                      ) : (
                        <p className="text-muted">No hay herramientas asociadas</p>
                      )}
                    </div>
                  </td>
                </tr>
              )}
            </React.Fragment>
          ))}

        </tbody>
      </table>

      <OverlayTrigger
        placement="top"
        overlay={<Tooltip>Regresar a la lista de clientes</Tooltip>}
      >
        <button
          className="btn btn-warning mx-2"
          type="button"
          onClick={() => navigate('/admin-client')}
        >
          Volver
        </button>
      </OverlayTrigger>
    </div>
  );
}

export default ViewLoanClient;
