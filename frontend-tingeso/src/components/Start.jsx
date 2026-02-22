import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../App.css';

function Start() {
  const navigate = useNavigate();

  const categories = [
    {
      icon: 'bi-people',
      title: 'Clientes',
      backgroundImage: '/images/clientes.jpg',
      description: 'Gestiona prestamos, historial y datos de clientes',
      path: '/admin-client',
      bgColor: '#4e75df',
    },
    {
      icon: 'bi-tools',
      title: 'Herramientas',
      backgroundImage: '/images/herramientas.jpg',
      description: 'Catálogo completo de herramientas disponibles',
      path: '/Home',
      bgColor: '#4e75df',
    },
    {
      icon: 'bi-filetype-mov',
      title: 'Control de inventario',
      backgroundImage: '/images/kardex.jpg',
      description: 'Registro detallado de movimientos y transacciones',
      path: '/kardex',
      bgColor: '#4e75df',
    },
    {
      icon: 'bi-file-earmark-arrow-down-fill',
      title: 'Reportes',
      backgroundImage: '/images/reportes.jpg',
      description: 'Genera y descarga informes del sistema',
      path: '/reports',
      bgColor: '#4e75df',
    },
  ];

  return (
    <>
      <h2 className="text-center my-4 mb-3">Selecciona una opción</h2>
      <h6 className="text-center mb-4">Bienvenido al sistema de préstamo de herramientas. Aquí puedes gestionar clientes, herramientas, revisar el kardex y generar reportes detallados para optimizar tu experiencia de préstamo.</h6>
      <div className="row g-4">
        {categories.map((category) => (
          <div key={category.path} className="col-12 col-md-6 col-lg-3">
            <button
              className="card h-100 border-0 shadow-sm category-card"
              onClick={() => navigate(category.path)}
              style={{ cursor: 'pointer', textAlign: 'inherit', width: '100%' }}
              type="button"
            >
              <div
                className="card-img-top position-relative"
                style={{
                  backgroundColor: category.bgColor,
                  backgroundImage: category.backgroundImage
                    ? `linear-gradient(rgba(78, 117, 223, 0.3), rgba(78, 117, 223, 0.3)), url(${category.backgroundImage})`
                    : 'none',
                  backgroundSize: 'cover',
                  backgroundPosition: 'center',
                  height: '200px',
                }}
              >
                <div className="position-absolute top-50 start-50 translate-middle">
                  <div
                    className="bg-dark rounded-circle d-flex align-items-center justify-content-center"
                    style={{ width: '80px', height: '80px' }}
                  >
                    <i className={`bi ${category.icon} text-white`} style={{ fontSize: '2rem' }} />
                  </div>
                </div>
              </div>
              <div className="card-body text-center d-flex flex-column">
                <h5 className="card-title fw-bold mb-3" style={{ color: '#1e3a8a' }}>{category.title}</h5>
                <p className="card-text flex-grow-1" style={{ color: '#555' }}>{category.description}</p>
                <button
                  type="button"
                  className="btn btn-danger w-100 mt-3"
                  onClick={() => navigate(category.path)}
                  style={{ backgroundColor: '#225dff', borderColor: '#555' }}
                >
                  Ir
                </button>
              </div>
            </button>
          </div>
        ))}
      </div>
    </>
  );
}

export default Start;
