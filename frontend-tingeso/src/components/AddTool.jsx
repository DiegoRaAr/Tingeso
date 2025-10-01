import toolService from "../services/tool.service";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const AddTool = () => {

    const navigate = useNavigate();

    const [tool, setTool] = useState({
        nameTool: "",
        categoryTool: "",
        totalValueTool: 0,
        stockTool: 1,
        repairCharge: 0,
        dailyCharge: 0,
        lateCharge: 0
    });

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setTool({
            ...tool,
            [name]: type === 'checkbox' ? checked : value 
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        toolService.createTool(tool)
            .then(response => {
                alert("Herramienta añadida con éxito");
                setTool({
                    nameTool: "",
                    categoryTool: "",
                    totalValueTool: 0,
                    stockTool: 1,
                    repairCharge: 0,
                    dailyCharge: 0,
                    lateCharge: 0
                });
            })
            .catch(error => {
                console.log("Error al añadir herramienta", error);
            });
    };


    return (
        <div className="container-fluid">
            <h1 className="text-start my-1 mb-4">Añadir Nueva Herramienta</h1>
            <h5 className="text-start my-3 mb-4">Ingrese los datos de la herramienta:</h5>
            <form onSubmit={handleSubmit}>
                <div className="mb-4 text-start">
                    <label htmlFor="nameTool" className="form-label">Nombre herramienta</label>
                    <input 
                        type="text" 
                        className="form-control" 
                        id="nameTool"
                        required
                        minLength="4" 
                        maxLength="50"
                        name="nameTool"
                        value={tool.nameTool}
                        onChange={handleChange}
                    />
                </div>

                <div className="mb-4 text-start">
                <label htmlFor="categoryTool" className="form-label">Categoria herramienta</label>
                    <select 
                        class="form-select" 
                        id="categoryTool"
                        name="categoryTool"
                        value={tool.categoryTool}
                        onChange={handleChange}
                        required
                    >
                        <option value="">Selecciona una opción</option>
                        <option value="Manual">Manual</option>
                        <option value="Electrica">Electrica</option>
                        <option value="Pedestal">Pedestal</option>
                    </select>
                </div>

                <div className="mb-4 text-start">
                    <label htmlFor="totalValueTool" className="form-label">Valor total de la herramienta</label>
                    <input 
                        type="number" 
                        className="form-control" 
                        id="totalValueTool"
                        min="1"
                        name="totalValueTool"
                        value={tool.totalValueTool}
                        onChange={handleChange} 
                    />
                </div>

                <div className="mb-4 text-start">
                    <label htmlFor="rapairCharge" className="form-label">Cargo de reparación</label>
                    <input 
                        type="number" 
                        className="form-control" 
                        id="repairCharge" 
                        name="repairCharge"
                        min="1"
                        value={tool.repairCharge}
                        onChange={handleChange}
                    />
                </div>

                <div className="mb-4 text-start">
                    <label htmlFor="dailyCharge" className="form-label">Cargo diario</label>
                    <input 
                        type="number" 
                        className="form-control" 
                        id="dailyCharge"
                        name="dailyCharge"
                        min="1"
                        value={tool.dailyCharge}
                        onChange={handleChange} 
                    />
                </div>

                <div className="mb-4 text-start">
                    <label htmlFor="lateCharge" className="form-label">Cargo por atraso</label>
                    <input 
                        type="number" 
                        className="form-control" 
                        id="lateCharge"
                        name="lateCharge"
                        min="1"
                        value={tool.lateCharge}
                        onChange={handleChange}
                    />
                </div>
                
                <button type="submit" className="btn btn-primary">Añadir herramienta</button>
            </form>

            <button class="btn btn-primary mx-2 my-4" type="button" onClick={() => navigate(`/home`)}>Volver</button>
        </div>
    );
}

export default AddTool;