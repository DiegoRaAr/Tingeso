import React from "react";
import { Link } from "react-router-dom";
import '../App.css';

const Navbar = () => (
    <nav className="navbar navbar-dark bg-primary fixed-top">
      <div className="container-fluid">
        <a className="navbar-brand" href="#">Sistema de prestamo de herramientas</a>
        <button className="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar" aria-controls="offcanvasNavbar" aria-label="Toggle navigation">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="offcanvas offcanvas-end text-bg-dark text-white" tabIndex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
          <div className="offcanvas-header">
            <h5 className="offcanvas-title" id="offcanvasNavbarLabel">Menú</h5>
            <button type="button" className="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
          </div>
          <div className="offcanvas-body">
            <ul className="navbar-nav justify-content-end flex-grow-1 pe-3">
              <li className="nav-item">
                <Link className="nav-link active" aria-current="page" to="/">Lista de herramientas</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/add-tool">Registar herramienta</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/admin-client">Lista de clientes</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/add-client">Registrar cliente</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/make-loan">Realizar prestamo</Link>
              </li>
              <li className="nav-item">
                <Link className="nav-link" to="/finish-loan">Registrar devolución</Link>
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

export default Navbar;