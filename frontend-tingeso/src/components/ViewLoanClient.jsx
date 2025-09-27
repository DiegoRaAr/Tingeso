import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import { useParams } from 'react-router-dom';
import loanService from '../services/loan.service';
import clientService from '../services/client.service';

import '../App.css';

const ViewLoanClient = () => {

  const [loans, setLoans] = useState([]);
  const [client, setClient] = useState({
    rutClient: "",
    nameClient: ""
  });

  const { rut } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    clientService.getClientByRut(rut)
      .then(response => {
        setClient(response.data);
        setClient({
          ...client,
          nameClient: response.data.nameClient
        });
      })
      .catch(error => {
        console.log("Error al obtener cliente", error);
      });
    loanService.getLoansByRut(rut)
      .then(response => {
        setLoans(response.data);
      })
      .catch(error => {
        console.log("Error al obtener prestamos", error);
      });
    }, [rut]);

  return (
    <div>
      <h2>Prestamos de {client.nameClient}</h2>
      <table className="table table-striped table-hover align-middle">
        <thead>
          <tr>
            <th>ID</th>
            <th>Fecha de inicio</th>
            <th>Hora de prestamo</th>
            <th>Fecha de fin</th>
            <th>Estado</th>
          </tr>
        </thead>
        <tbody>
          {loans.map(loan => (
            <tr key={loan.idLoan}>
              <td>{loan.idLoan}</td>
              <td>{loan.initDate}</td>
              <td>{loan.hourLoan}</td>
              <td>{loan.endDate}</td>
              <td>{loan.stateLoan}</td>

              <button 
                class="btn btn-danger mx-2" 
                type="button"
                >
                Finalizar prestamo
                </button>


            </tr>
          ))}
        </tbody>
      </table>

      <button 
                class="btn btn-primary mx-2" 
                type="button"
                onClick={() => navigate(`/admin-client`)}
                >
                Volver
                </button>
    </div>
  );
};

export default ViewLoanClient;