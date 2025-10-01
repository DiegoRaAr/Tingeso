import React, {useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import toolService from "../services/tool.service";
import '../App.css';

const Home = () => {

    const [tools, setTools] = useState([]);
    const addToolNumber = (id, number) => {
        toolService.addToolNumber(id, number)
        window.location.reload()
            .then(response => {
                alert("Herramienta agregada con éxito");
            })
    }
    const navigate = useNavigate();

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
        

        <div className="container-fluid">
            <h1 className="text-start my-1 mb-4">Lista de herramientas</h1>
           
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
                <tbody className="table-group-divider">
                {tools.map((tool) => (
                    <tr key={tool.idTool}>
                        <th scope="row">{tool.idTool}</th>
                        <td>{tool.nameTool}</td>
                        <td>{tool.categoryTool}</td>
                        <td>{tool.dailyCharge}</td>
                        <td>{tool.stockTool}</td>
                        <td>
                            <div className="d-grid gap-2 d-md-block">
                                <button 
                                    className="btn btn-danger mx-2" 
                                    type="button" 
                                    onClick={async () => {
                                        if (confirm('¿Eliminar herramienta?')) {
                                            await toolService.deleteTool(tool.idTool);
                                            window.location.reload();
                                        }
                                    }}
                                >
                                    Dar de baja
                                </button>
                                <button className="btn btn-warning mx-2" type="button">Editar</button>
                                <button className="btn btn-success mx-2" type="button" onClick={() => addToolNumber(tool.idTool,1)}>Agregar herramienta</button>
                                <button className="btn btn-warning mx-2" type="button" onClick={() => addToolNumber(tool.idTool,-1)}>Quitar herramienta</button>
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
                </table>

            <button className="btn btn-primary mx-2" type="button" onClick={() => navigate(`/add-tool`)}>Agregar herramienta</button>
            <button className="btn btn-primary mx-2 my-4" type="button" onClick={() => navigate(`/start`)}>Volver al inicio</button>
            

        </div>
        
    );
};

export default Home;