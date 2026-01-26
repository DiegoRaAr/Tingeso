import React from 'react';

const Footer = () => {
    return (
        <footer className="bg-dark text-white text-center text-lg-start mt-auto">
            <div className="container p-4">
                <div className="row">
                    <div className="col-lg-6 col-md-12 mb-4 mb-md-0">
                        <h5 className="text-uppercase">Sistema de Prestamo de Herramientas</h5>
                        <p>
                            Sistema diseñado para la administración eficiente de herramientas y préstamos.
                        </p>
                    </div>

                    <div className="col-lg-3 col-md-6 mb-4 mb-md-0">
                        <h5 className="text-uppercase">Enlaces</h5>
                        <ul className="list-unstyled mb-0">
                            <li>
                                <a href="/home" className="text-white text-decoration-none">Inicio</a>
                            </li>
                            <li>
                                <a href="/reports" className="text-white text-decoration-none">Reportes</a>
                            </li>
                        </ul>
                    </div>

                    <div className="col-lg-3 col-md-6 mb-4 mb-md-0">
                        <h5 className="text-uppercase">Contacto</h5>
                        <ul className="list-unstyled mb-0">
                            <li>
                                <span className="text-white">soporte@tingeso.cl</span>
                            </li>
                            <li>
                                <span className="text-white">+56 9 1234 5678</span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

            <div className="text-center p-3" style={{ backgroundColor: 'rgba(0, 0, 0, 0.2)' }}>
                © {new Date().getFullYear()} Tingeso - Todos los derechos reservados
            </div>
        </footer>
    );
};

export default Footer;
