import React from "react";
import { useNavigate } from "react-router-dom";
import '../App.css';

const Start = () => {

    const navigate = useNavigate();

    return (
        <div className="d-grid gap-2 col-6 mx-auto">
            <h2 className="text-center my-4">Bienvenido al sistema de prestamo de herramientas</h2>
            En la parte superior se encuentra el menu de navegacion, en el cual se puede acceder a las diferentes secciones del sistema.
            <button type="button" className="btn btn-primary btn-lg" onClick={() => navigate(`/admin-client`)}>
                <i className="bi bi-people me-2"></i>Clientes
            </button>
            <button type="button" className="btn btn-primary btn-lg" onClick={() => navigate(`/Home`)}>
                <i className="bi bi-tools m-2"></i>
                
            Herramientas
            </button>
        </div>
    );
}

export default Start;