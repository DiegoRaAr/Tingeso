import React, {useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import toolService from "../services/tool.service";
import '../App.css';

const Home = () => {

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
                <tbody class="table-group-divider">
                    {tools.map((tool) => (
                        <tr key={tool.id}>
                            <th scope="row">{tool.idTool}</th>
                            <td>{tool.nameTool}</td>
                            <td>{tool.categoryTool}</td>
                            <td>{tool.dailyCharge}</td>
                            <td>{tool.stockTool}</td>
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

export default Home;