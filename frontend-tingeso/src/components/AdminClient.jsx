import React, {use, useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import clientService from '../services/client.service';
import loanService from '../services/loan.service';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import '../App.css';

const AdminClient = () => {

  const navigate = useNavigate();

  const [clients, setClients] = React.useState([]);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    clientService.getAllClients()
      .then(response => {
        setClients(response.data);
      })
      .catch(error => {
        console.log("Error al obtener clientes", error);
      });
  }, []);

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2 className='text-start my-1 mb-0'>Listado de clientes</h2>
        <input 
          className="form-control" 
          style={{ width: '300px' }}
          type="search" 
          placeholder="Buscar cliente..." 
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>

      <h6 className="text-start mb-4">En este apartado puedes gestionar los clientes registrados en el sistema, iniciar nuevos préstamos, revisar el historial de préstamos de cada cliente y editar su información.</h6>
      <div style={{ height: '550px', overflowY: 'scroll', border: '1px solid #dee2e6', borderRadius: '5px' }}>
      <table className="table table-striped table-hover align-middle mb-0">
                <thead style={{ position: 'sticky', top: 0, backgroundColor: '#f8f9fa', zIndex: 1 }}>
                    <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Nombre</th>
                    <th scope='col'>Rut</th>
                    <th scope="col">Estado</th>
                    <th scope="col">Contacto</th>
                    <th scope="col" className="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody class="table-group-divider">
                    {clients
                      .filter(client => 
                        client.nameClient.toLowerCase().includes(searchTerm.toLowerCase()) ||
                        client.rutClient.toLowerCase().includes(searchTerm.toLowerCase())
                      )
                      .map((client) => (
                        <tr key={client.id}>
                            <th scope="row">{client.idClient}</th>
                            <td>{client.nameClient}</td>
                            <td>{client.rutClient}</td>
                            <td>{client.stateClient}</td>
                            <td>{client.phoneNumberClient}</td>
                            <td>
                            <div className="d-flex gap-2 justify-content-center">
                              <OverlayTrigger
                                placement="top"
                                overlay={<Tooltip>Crear un nuevo préstamo para este cliente</Tooltip>}
                              >
                                <button 
                                  className="btn btn-sm btn-success" 
                                  type="button" 
                                  style={{ width: '130px' }}
                                  onClick={() => {
                                    loanService.updatePenalty(client.idClient)
                                    .then(() => navigate(`/make-loan/${client.rutClient}`, {state: {client}}))
                                  }}
                                >
                                  Iniciar préstamo
                                </button>
                              </OverlayTrigger>

                              <div className="dropdown">
                                <button 
                                  className="btn btn-sm btn-info dropdown-toggle text-white" 
                                  type="button" 
                                  data-bs-toggle="dropdown" 
                                  aria-expanded="false"
                                >
                                  Opciones
                                </button>
                                <ul className="dropdown-menu">
                                  <li>
                                    <button 
                                      className="dropdown-item" 
                                      type="button" 
                                      onClick={() => {
                                        loanService.updatePenalty(client.idClient)
                                        .then(() => navigate(`/loans-by-rut/${client.rutClient}`))
                                      }}
                                    >
                                      Ver préstamos
                                    </button>
                                  </li>
                                  <li>
                                    <button 
                                      className="dropdown-item" 
                                      type="button" 
                                      onClick={() => navigate('/add-client', { state: { client } })}
                                    >
                                      Editar
                                    </button>
                                  </li>
                                  <li><hr className="dropdown-divider" /></li>
                                  <li>
                                    <button 
                                      className="dropdown-item text-danger" 
                                      type="button" 
                                      onClick={() => {
                                        clientService.changeStateClient(client.idClient)
                                        .then(() => {
                                          navigate('/admin-client');
                                          window.location.reload();
                                        })
                                        .catch(error => console.log("Error al cambiar estado del cliente", error));
                                      }}
                                    >
                                      Cambiar estado
                                    </button>
                                  </li>
                                </ul>
                              </div>
                            </div>
                            </td>
                        </tr>
                    ))}
                </tbody>
                </table>
            </div>

            <OverlayTrigger
              placement="top"
              overlay={<Tooltip>Usted será redirigido al formulario para crear un cliente
</Tooltip>}
            >
              <button className="btn btn-primary mx-2 my-4" type="button" onClick={() => navigate(`/add-client`)}>Agregar cliente</button>
            </OverlayTrigger>
            
            <OverlayTrigger
              placement="top"
              overlay={<Tooltip>Regresar a la página de inicio</Tooltip>}
            >
              <button className="btn btn-warning mx-2 my-4" type="button" onClick={() => navigate(`/start`)}>Volver al inicio</button>
            </OverlayTrigger>
      
    </div>
  );
};

export default AdminClient;