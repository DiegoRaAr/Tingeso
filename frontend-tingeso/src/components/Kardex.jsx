import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import '../App.css';
import kardexService from "../services/kardex.service";
import DatePicker, { registerLocale } from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import es from 'date-fns/locale/es';

registerLocale('es', es);

const Kardex = () => {
    const navigate = useNavigate();

    const [kardexes, setKardexes] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);

    useEffect(() => {
        kardexService.getAllKardex()
            .then(response => setKardexes(response.data))
            .catch(error => console.log("Error al obtener kardex", error));
    }, []);

    const filteredKardexes = kardexes.filter(kardex => {
        // Filter for tool name or ID Kardex
        const searchMatch = searchTerm 
            ? kardex.nameTool.toLowerCase().includes(searchTerm.toLowerCase()) || 
              kardex.idKardex.toString().includes(searchTerm)
            : true;

        // filter for date range
        if (!startDate && !endDate) return searchMatch;
        
        const kardexDate = new Date(kardex.dateKardex);
        // Normalize dates to ignore time for stricter day comparison if desired, 
        // but simple comparison usually suffices if kardexDate is mostly date-based.
        // Assuming kardexDate comes from DB as ISO string.
        
        const startMatch = startDate ? kardexDate >= startDate : true;
        // For end date, we might want to include the whole end day.
        // DatePicker returns 00:00:00 of the selected day.
        // If kardexDate has time (e.g. 14:00), and endDate is selected day 00:00, 
        // <= endDate will exclude it unless we set endDate to end of day.
        
        let endMatch = true;
        if (endDate) {
            const endOfDay = new Date(endDate);
            endOfDay.setHours(23, 59, 59, 999);
            endMatch = kardexDate <= endOfDay;
        }

        return searchMatch && startMatch && endMatch;
    }).sort((a, b) => b.idKardex - a.idKardex);

    return (
        <div>
            <h2>Kardex</h2>
            {/* Filtros */}
            <div className="mb-3 d-flex gap-3 align-items-center" style={{ position: 'relative', zIndex: 1050 }}>
                <div style={{ flex: 1 }}>
                    <input
                        type="search"
                        className="form-control w-100"
                        placeholder="Buscar por ID o nombre..."
                        value={searchTerm}
                        onChange={e => setSearchTerm(e.target.value)}
                    />
                </div>
                
                <div className="d-flex gap-2" style={{ flex: 2 }}>
                    <div style={{ flex: 1 }}>
                        <DatePicker
                            selected={startDate}
                            onChange={(date) => setStartDate(date)}
                            placeholderText="Desde"
                            className="form-control w-100"
                            wrapperClassName="w-100"
                            locale="es"
                            dateFormat="dd/MM/yyyy"
                            showMonthDropdown
                            showYearDropdown
                            dropdownMode="select"
                        />
                    </div>
                    <div style={{ flex: 1 }}>
                        <DatePicker
                            selected={endDate}
                            onChange={(date) => setEndDate(date)}
                            minDate={startDate}
                            placeholderText="Hasta"
                            className="form-control w-100"
                            wrapperClassName="w-100"
                            locale="es"
                            dateFormat="dd/MM/yyyy"
                            showMonthDropdown
                            showYearDropdown
                            dropdownMode="select"
                        />
                    </div>
                </div>

                <button
                    className="btn btn-secondary"
                    type="button"
                    onClick={() => {
                        setSearchTerm("");
                        setStartDate(null);
                        setEndDate(null);
                    }}
                >
                    Limpiar filtros
                </button>
            </div>
            <div style={{ height: '550px', overflowY: 'scroll', border: '1px solid #dee2e6', borderRadius: '5px' }}>
            <table className="table table-striped table-hover mb-0">
                <thead style={{ position: 'sticky', top: 0, backgroundColor: '#f8f9fa', zIndex: 1 }}>
                    <tr>
                        <th scope="col">ID Kardex</th>
                        <th scope="col">Fecha</th>
                        <th scope="col">ID Herramienta</th>
                        <th scope="col">Nombre Herramienta</th>
                        <th scope="col">Estado Herramienta</th>
                    </tr>
                </thead>
                <tbody className="table-group-divider">
                    {filteredKardexes.map((kardex) => (
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
            </div>
            <button className="btn btn-primary mx-2 my-4" type="button" onClick={() => navigate(`/start`)}>Volver al inicio</button>
        </div>
    );
};

export default Kardex;