import React, {use, useEffect, useState} from 'react';
import clientService from '../services/client.service';
import '../App.css';

const AdminClient = () => {

  const [clients, setClients] = React.useState([]);

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
      <h2 className='text-start my-1 mb-4'>Lista de clientes</h2>
      <table className="table table-striped table-hover align-middle">
                <thead>
                    <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Nombre</th>
                    <th scope="col">Estado</th>
                    <th scope="col">Contacto</th>
                    </tr>
                </thead>
                <tbody class="table-group-divider">
                    {clients.map((client) => (
                        <tr key={client.id}>
                            <th scope="row">{client.idClient}</th>
                            <td>{client.nameClient}</td>
                            <td>{client.stateClient}</td>
                            <td>{client.phoneNumberClient}</td>
                            <div class="d-grid gap-2 d-md-block">
                            <button class="btn btn-danger mx-4" type="button">Dar de baja</button>
                            <button class="btn btn-warning bg-" type="button">Editar</button>
                            </div>
                        </tr>
                    ))}
                </tbody>
                </table>
      
    </div>
  );
};

export default AdminClient;