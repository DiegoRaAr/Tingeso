import React, { useState, useEffect } from 'react';
import toolService from '../services/tool.service';
import '../App.css';

const MakeLoan = () => {

  const [tools, setTools] = useState([]);
  
      useEffect(() => {
          toolService.getAllTools()
              .then(response => {
                  setTools(response.data);
              })
              .catch(error => {
                  console.log("Error al obtener herramientas", error);
              });
      }, []);

  return (
    <div>
      <h1 className="text-start my-1 mb-4">Realizar prestamo</h1>

      <form>
        <div className="mb-4 text-start">
            <label htmlFor="rutClient" className="form-label">Rut del cliente</label>
            <input 
                type="text" 
                className="form-control" 
                id="rutclient" 
                name="rutclient"
            />
        </div>
      </form>

      <table className="table table-striped table-hover align-middle">
                <thead>
                    <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Herramienta</th>
                    <th scope="col">Categoría</th>
                    <th scope="col">Cargo diario</th>
                    <th scope="col">Stock</th>
                    </tr>
                </thead>
                <tbody class="table-group-divider">
                    {tools.map((tool) => (
                        <tr key={tool.id}>
                            <th scope="row">{tool.idTool}</th>
                            <td>{tool.nameTool}</td>
                            <td>{tool.categoryTool}</td>
                            <td>{tool.dailyCharge}</td>
                            <td>{tool.stockTool}</td>
                            <div class="d-grid gap-2 d-md-block">
                            
                            <form>
                              <input 
                                  type="number" 
                                  className="form-control" 
                                  id="quantity" 
                                  name="quantity"
                                  min="0"
                                  max={tool.stockTool}
                              />
                            </form>

                            </div>
                        </tr>
                    ))}
                </tbody>
        </table>
          
    <form>
      <div>
        <label htmlFor="dateLoan" className="form-label text-start">Fecha de devolucion</label>
        <input 
            type="date" 
            className="form-control mx-3" 
            id="dateLoan" 
            name="dateLoan"
        />
      </div>
      <div className="mb-4 text-start">
      </div>
    </form>
    
    <h4 className='text-start'>Total: </h4>

    <button class="btn btn-primary" type="button">Realizar prestamo</button>

      
    </div>
  );
};

export default MakeLoan;