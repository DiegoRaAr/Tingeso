import React from "react";
import { useNavigate } from "react-router-dom";
import '../App.css';
import kardexService from "../services/kardex.service";
import toolService from "../services/tool.service";

const Kardex = () => {
    const navigate = useNavigate();

    const [kardexes, setKardexes] = React.useState([]);

    React.useEffect(() => {
        kardexService.getAllKardex()
            .then(response => {
                setKardexes(response.data);
            })
            .catch(error => {
                console.log("Error al obtener kardex", error);
            });
    }, []);

    return (
        <div>
            <h2>Kardex</h2>
            <table className="table table-hover">
                <thead>
                    <tr>
                        <th scope="col">ID Kardex</th>
                        <th scope="col">Fecha</th>
                        <th scope="col">ID Herramienta</th>
                        <th scope="col">Nombre Herramienta</th>
                        <th scope="col">Estado Herramienta</th>
                    </tr>
                </thead>
                <tbody className="table-group-divider">
                    {kardexes.map((kardex) => (
                        <tr key={kardex.idKardex}>
                            <td>{kardex.idKardex}</td>
                            <td>{new Date(kardex.dateKardex).toLocaleDateString()}</td>
                            <td>{kardex.idTool}</td>
                            <td>{kardex.nameTool}</td>
                            <td>{kardex.stateTool}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <button className="btn btn-primary mx-2 my-4" type="button" onClick={() => navigate(`/start`)}>Volver al inicio</button>
        </div>
    );
};

export default Kardex;