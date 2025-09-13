import React, { use, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import employeeServeice  from "../services/employee.service";

const Home = () => {

    const [employees, setEmployees] = useState([]);

    const navigate = useNavigate();

    const init = () => {
        employeeServeice
        .getAllEmployees()
        .then((response)=> {
            console.log("Mostrando lista de empleados", response.data);
            setEmployees(response.data);
        })
        .catch((error) => {
            console.log(
                "Se ha producido un error al mostrar la lista de empleados",
                error
            );
        });
    };

    useEffect(() => {
        init();
    }, []);

    return (
        <div>
            <h1>Tingeso</h1>
            <p>Pagina web de tingeso</p>
            <h2>Lista de empleados</h2>
            <table border="1">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Apellido</th>
                        {/* Agrega más columnas según tu modelo */}
                    </tr>
                </thead>
                <tbody>
                    {employees.map(emp => (
                        <tr key={emp.idEmployee}>
                            <td>{emp.idEmployee}</td>
                            <td>{emp.rutEmployee}</td>
                            <td>{emp.nameEmployee}</td>
                            <td>{emp.stateEmployee}</td>
                            {/* Agrega más celdas según tu modelo */}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
        
    );
};

export default Home;