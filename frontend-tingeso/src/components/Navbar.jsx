import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../App.css';

function Navbar() {
  const navigate = useNavigate();

  return (
    <nav className="navbar navbar-dark bg-navbar fixed-top">
      <div className="container-fluid">
        <button
          className="navbar-brand btn btn-link text-decoration-none"
          onClick={() => navigate('/start')}
          style={{
            cursor: 'pointer', border: 'none', background: 'none', padding: 0,
          }}
          type="button"
        >
          <i className="bi bi-tools me-2" aria-hidden="true" />
          Sistema de prestamo de herramientas
        </button>
        <button className="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar" aria-controls="offcanvasNavbar" aria-label="Toggle navigation">
          <span className="navbar-toggler-icon" />
        </button>
        <div className="offcanvas offcanvas-end text-bg-dark text-white" tabIndex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
          <div className="offcanvas-header">
            <h5 className="offcanvas-title" id="offcanvasNavbarLabel">Menú</h5>
            <button type="button" className="btn-close" data-bs-dismiss="offcanvas" aria-label="Close" />
          </div>
          <div className="offcanvas-body">
            <ul className="navbar-nav justify-content-end flex-grow-1 pe-3">
              <li className="nav-item">
                <Link className="nav-link" to="/">Inicio</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/home">Herramientas</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/admin-client">Clientes</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/kardex">Ver kardex</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/reports">Reportes</Link>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
