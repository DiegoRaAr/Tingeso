import React, { useEffect, useState } from "react";
import clientService from "../services/client.service";
import loanService from "../services/loan.service";
import toolService from "../services/tool.service";
import DatePicker, { registerLocale } from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import es from 'date-fns/locale/es';

registerLocale('es', es);

const Reports = () => {
    const [reportData, setReportData] = useState([]);
    const [reportTitle, setReportTitle] = useState("");
    const [reportType, setReportType] = useState(""); // 'tools', 'clients', 'loans', 'ranking'
    const [searched, setSearched] = useState(false);
    const [currentView, setCurrentView] = useState("menu"); // 'menu', 'tools', 'clients', 'loans'

    // Date states
    const [toolStartDate, setToolStartDate] = useState(null);
    const [toolEndDate, setToolEndDate] = useState(null);
    const [loanStartDate, setLoanStartDate] = useState(null);
    const [loanEndDate, setLoanEndDate] = useState(null);

    // Helpers
    const formatDate = (date) => date ? date.toISOString().split('T')[0] : "";
    const displayDate = (dateString) => {
        if (!dateString) return "-";
        const date = new Date(dateString);
        return date.toLocaleDateString();
    };

    const handlePrint = () => {
        setTimeout(() => window.print(), 100);
    };

    const handleViewChange = (view) => {
        setCurrentView(view);
        setReportData([]);
        setReportTitle("");
        setSearched(false);
    };

    // --- HERRAMIENTAS ---
    const getBestToolsHistoric = (isDownload = false) => {
        setSearched(true);
        const start = "2000-01-01";
        const end = formatDate(new Date(Date.now() + 24 * 60 * 60 * 1000)); // Tomorrow
        toolService.getBestToolsByRangeDate(start, end)
            .then(res => {
                setReportData(res.data);
                setReportTitle("Herramientas más prestadas históricamente");
                setReportType("ranking");
                if (isDownload) handlePrint();
            })
            .catch(err => {
                console.error(err);
                setReportData([]);
            });
    };

    const getBestToolsByDate = (isDownload = false) => {
        if (!toolStartDate || !toolEndDate) {
            alert("Seleccione ambas fechas");
            return;
        }
        setSearched(true);
        toolService.getBestToolsByRangeDate(formatDate(toolStartDate), formatDate(toolEndDate))
            .then(res => {
                setReportData(res.data);
                setReportTitle(`Herramientas más prestadas (${displayDate(toolStartDate)} - ${displayDate(toolEndDate)})`);
                setReportType("ranking");
                if (isDownload) handlePrint();
            })
            .catch(err => console.error(err));
    };

    const getAllTools = (isDownload = false) => {
        setSearched(true);
        toolService.getAllTools()
            .then(res => {
                setReportData(res.data);
                setReportTitle("Todas las Herramientas");
                setReportType("tools");
                if (isDownload) handlePrint();
            })
            .catch(err => console.error(err));
    };

    // --- CLIENTES ---
    const getClients = (filterType, title, isDownload = false) => {
        setSearched(true);
        clientService.getAllClients()
            .then(res => {
                let data = res.data;
                if (filterType === 'ACTIVO') {
                    data = data.filter(c => c.stateClient === 'ACTIVO');
                } else if (filterType === 'RESTRINGIDO') {
                    // Use specific endpoint or filter? User mentioned specific function for restricted.
                    // But if we want consistent object structure, filtering getAll is fine unless backend obj differs.
                    // Let's use getRestrictedClients for restricted to be safe, or filter.
                    // User prompt: "2.2 ... esta funcion muetra todos los clientes con state RESTRINGIDO"
                    // Existing code had clientService.getRestrictedClients(). Let's use that if filterType is Restricted.
                    if (filterType === 'RESTRINGIDO') {
                       clientService.getRestrictedClients().then(r => {
                           setReportData(r.data);
                           setReportTitle(title);
                           setReportType("clients");
                           if (isDownload) handlePrint();
                       });
                       return;
                    }
                }
                setReportData(data);
                setReportTitle(title);
                setReportType("clients");
                if (isDownload) handlePrint();
            })
            .catch(err => console.error(err));
    };

    // --- PRESTAMOS ---
    const getLoans = (stateFilter, title, isDownload = false) => {
        setSearched(true);
        loanService.getAllLoans()
            .then(res => {
                let data = res.data;
                if (stateFilter) {
                    data = data.filter(l => l.stateLoan === stateFilter);
                }
                setReportData(data);
                setReportTitle(title);
                setReportType("loans");
                if (isDownload) handlePrint();
            })
            .catch(err => console.error(err));
    };

    const getLoansByDate = (stateFilter, title, isDownload = false) => {
        if (!loanStartDate || !loanEndDate) {
            alert("Seleccione ambas fechas");
            return;
        }
        setSearched(true);
        loanService.getLoanByRangeDate(formatDate(loanStartDate), formatDate(loanEndDate))
            .then(res => {
                let data = res.data;
                if (stateFilter) {
                    data = data.filter(l => l.stateLoan === stateFilter);
                }
                setReportData(data);
                setReportTitle(title + ` (${displayDate(loanStartDate)} - ${displayDate(loanEndDate)})`);
                setReportType("loans");
                if (isDownload) handlePrint();
            })
            .catch(err => console.error(err));
    };

    return (
        <div className="container-fluid">
            <style>
                {`
                    @media print {
                        .no-print { display: none !important; }
                        .print-area { display: block !important; }
                        body { visibility: visible; }
                    }
                `}
            </style>

            <h1 className="text-center my-4">Reportes</h1>

            {/* MENÚ PRINCIPAL (Solo visible en vista 'menu' y no al imprimir) */}
            {currentView === 'menu' && (
                <div className="no-print d-flex justify-content-center align-items-center gap-4 flex-wrap" style={{ minHeight: '50vh' }}>
                    <div className="card shadow-sm border-primary text-center p-4 hover-shadow" style={{ width: '18rem', cursor: 'pointer' }} onClick={() => handleViewChange('tools')}>
                        <div className="card-body">
                            <h3 className="card-title text-primary"><i className="bi bi-tools fs-1"></i></h3>
                            <h4 className="mt-3">Herramientas</h4>
                            <p className="text-muted">Ver rankings y stock</p>
                        </div>
                    </div>
                    
                    <div className="card shadow-sm border-success text-center p-4 hover-shadow" style={{ width: '18rem', cursor: 'pointer' }} onClick={() => handleViewChange('clients')}>
                        <div className="card-body">
                            <h3 className="card-title text-success"><i className="bi bi-people fs-1"></i></h3>
                            <h4 className="mt-3">Clientes</h4>
                            <p className="text-muted">Activos y restringidos</p>
                        </div>
                    </div>

                    <div className="card shadow-sm border-info text-center p-4 hover-shadow" style={{ width: '18rem', cursor: 'pointer' }} onClick={() => handleViewChange('loans')}>
                        <div className="card-body">
                            <h3 className="card-title text-info"><i className="bi bi-journal-text fs-1"></i></h3>
                            <h4 className="mt-3">Préstamos</h4>
                            <p className="text-muted">Historial y activos</p>
                        </div>
                    </div>
                </div>
            )}

            {/* CONTROLES DE VISTAS ESPECIFICAS (No imprimir) */}
            {currentView !== 'menu' && (
                <div className="no-print mb-5 d-flex justify-content-center flex-column align-items-center">
                    <button className="btn btn-outline-secondary mb-3 align-self-start ms-5" onClick={() => handleViewChange('menu')}>
                        <i className="bi bi-arrow-left"></i> Volver al Menú
                    </button>
                    
                    <div className="row w-100 justify-content-center">
                        {/* 1. HERRAMIENTAS */}
                        {currentView === 'tools' && (
                            <div className="col-md-8 col-lg-6">
                                <div className="card h-100 shadow-sm border-primary">
                                    <div className="card-header bg-primary text-white text-center">
                                        <h5 className="mb-0">Reportes de Herramientas</h5>
                                    </div>
                                    <div className="card-body d-flex flex-column gap-3">
                                        {/* 1.1 */}
                                        <div className="border-bottom pb-3">
                                            <h6 className="fw-bold">Ranking Histórico</h6>
                                            <p className="small text-muted mb-2">Las herramientas más solicitadas desde siempre.</p>
                                            <div className="d-flex gap-2">
                                                <button className="btn btn-primary w-50" onClick={() => getBestToolsHistoric(false)}>Generar en Pantalla</button>
                                                <button className="btn btn-outline-dark w-50" onClick={() => getBestToolsHistoric(true)}>Descargar PDF</button>
                                            </div>
                                        </div>
                                        {/* 1.2 */}
                                        <div className="border-bottom pb-3">
                                            <h6 className="fw-bold">Ranking por Fecha</h6>
                                            <p className="small text-muted mb-2">Las más solicitadas en un periodo específico.</p>
                                            <div className="d-flex gap-2 mb-2">
                                                <DatePicker selected={toolStartDate} onChange={setToolStartDate} placeholderText="Desde" className="form-control" dateFormat="dd/MM/yyyy" locale="es" />
                                                <DatePicker selected={toolEndDate} onChange={setToolEndDate} placeholderText="Hasta" className="form-control" dateFormat="dd/MM/yyyy" locale="es" />
                                            </div>
                                            <div className="d-flex gap-2">
                                                <button className="btn btn-primary w-50" onClick={() => getBestToolsByDate(false)}>Generar</button>
                                                <button className="btn btn-outline-dark w-50" onClick={() => getBestToolsByDate(true)}>Descargar PDF</button>
                                            </div>
                                        </div>
                                        {/* 1.3 */}
                                        <div>
                                            <h6 className="fw-bold">Inventario Completo</h6>
                                            <p className="small text-muted mb-2">Listado de todas las herramientas registradas.</p>
                                            <div className="d-flex gap-2">
                                                <button className="btn btn-primary w-50" onClick={() => getAllTools(false)}>Ver Inventario</button>
                                                <button className="btn btn-outline-dark w-50" onClick={() => getAllTools(true)}>Descargar Inventario</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* 2. CLIENTES */}
                        {currentView === 'clients' && (
                            <div className="col-md-8 col-lg-6">
                                <div className="card h-100 shadow-sm border-success">
                                    <div className="card-header bg-success text-white text-center">
                                        <h5 className="mb-0">Reportes de Clientes</h5>
                                    </div>
                                    <div className="card-body d-flex flex-column gap-3">
                                        {/* 2.1 */}
                                        <div className="border-bottom pb-3">
                                            <h6 className="fw-bold">Clientes Activos</h6>
                                            <p className="small text-muted mb-2">Clientes habilitados para pedir préstamos.</p>
                                            <div className="d-flex gap-2">
                                                <button className="btn btn-success w-50" onClick={() => getClients('ACTIVO', 'Clientes Activos', false)}>Ver Lista</button>
                                                <button className="btn btn-outline-dark w-50" onClick={() => getClients('ACTIVO', 'Clientes Activos', true)}>Descargar PDF</button>
                                            </div>
                                        </div>
                                        {/* 2.2 */}
                                        <div className="border-bottom pb-3">
                                            <h6 className="fw-bold">Clientes Restringidos</h6>
                                            <p className="small text-muted mb-2">Clientes con bloqueo o historial negativo.</p>
                                            <div className="d-flex gap-2">
                                                <button className="btn btn-warning w-50" onClick={() => getClients('RESTRINGIDO', 'Clientes Restringidos', false)}>Ver Lista</button>
                                                <button className="btn btn-outline-dark w-50" onClick={() => getClients('RESTRINGIDO', 'Clientes Restringidos', true)}>Descargar PDF</button>
                                            </div>
                                        </div>
                                        {/* 2.3 */}
                                        <div>
                                            <h6 className="fw-bold">Todos los Clientes</h6>
                                            <p className="small text-muted mb-2">Base de datos completa de clientes.</p>
                                            <div className="d-flex gap-2">
                                                <button className="btn btn-success w-50" onClick={() => getClients('ALL', 'Todos los Clientes', false)}>Ver Todos</button>
                                                <button className="btn btn-outline-dark w-50" onClick={() => getClients('ALL', 'Todos los Clientes', true)}>Descargar PDF</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}

                        {/* 3. PRESTAMOS */}
                        {currentView === 'loans' && (
                            <div className="col-md-8 col-lg-6">
                                <div className="card h-100 shadow-sm border-info">
                                    <div className="card-header bg-info text-white text-center">
                                        <h5 className="mb-0">Reportes de Préstamos</h5>
                                    </div>
                                    <div className="card-body d-flex flex-column gap-3">
                                        {/* 3.1 & 3.2 */}
                                        <div className="border-bottom pb-3">
                                            <h6 className="fw-bold">Estado Actual</h6>
                                            <div className="d-flex gap-3 mt-2">
                                                <div className="w-50">
                                                    <p className="small fw-bold mb-1">En curso (Activos)</p>
                                                    <div className="btn-group w-100">
                                                        <button className="btn btn-info text-white" onClick={() => getLoans('ACTIVO', 'Préstamos Activos', false)}>Ver</button>
                                                        <button className="btn btn-outline-dark" onClick={() => getLoans('ACTIVO', 'Préstamos Activos', true)}><i className="bi bi-download"></i></button>
                                                    </div>
                                                </div>
                                                <div className="w-50">
                                                    <p className="small fw-bold mb-1">Finalizados</p>
                                                    <div className="btn-group w-100">
                                                        <button className="btn btn-secondary" onClick={() => getLoans('FINALIZADO', 'Préstamos Finalizados', false)}>Ver</button>
                                                        <button className="btn btn-outline-dark" onClick={() => getLoans('FINALIZADO', 'Préstamos Finalizados', true)}><i className="bi bi-download"></i></button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        {/* 3.3 & 3.4 */}
                                        <div className="border-bottom pb-3">
                                            <h6 className="fw-bold">Búsqueda por Fecha</h6>
                                            <div className="d-flex gap-2 mb-2">
                                                <DatePicker selected={loanStartDate} onChange={setLoanStartDate} placeholderText="Inicio" className="form-control" dateFormat="dd/MM/yyyy" locale="es" />
                                                <DatePicker selected={loanEndDate} onChange={setLoanEndDate} placeholderText="Fin" className="form-control" dateFormat="dd/MM/yyyy" locale="es" />
                                            </div>
                                            <div className="d-flex gap-3">
                                                <div className="w-50">
                                                    <p className="small fw-bold mb-1">Activos en rango</p>
                                                    <div className="btn-group w-100">
                                                        <button className="btn btn-info text-white" onClick={() => getLoansByDate('ACTIVO', 'Préstamos Activos en Rango', false)}>Ver</button>
                                                        <button className="btn btn-outline-dark" onClick={() => getLoansByDate('ACTIVO', 'Préstamos Activos en Rango', true)}><i className="bi bi-download"></i></button>
                                                    </div>
                                                </div>
                                                <div className="w-50">
                                                    <p className="small fw-bold mb-1">Finalizados en rango</p>
                                                    <div className="btn-group w-100">
                                                        <button className="btn btn-secondary" onClick={() => getLoansByDate('FINALIZADO', 'Préstamos Finalizados en Rango', false)}>Ver</button>
                                                        <button className="btn btn-outline-dark" onClick={() => getLoansByDate('FINALIZADO', 'Préstamos Finalizados en Rango', true)}><i className="bi bi-download"></i></button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        {/* 3.5 */}
                                        <div>
                                            <h6 className="fw-bold">Historial Completo</h6>
                                            <p className="small text-muted mb-2">Todos los préstamos registrados en el sistema.</p>
                                            <div className="d-flex gap-2">
                                                <button className="btn btn-primary w-50" onClick={() => getLoans(null, 'Todos los Préstamos', false)}>Ver Historial</button>
                                                <button className="btn btn-outline-dark w-50" onClick={() => getLoans(null, 'Todos los Préstamos', true)}>Descargar Todo</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            )}

            {/* VISTA PREVIA / AREA DE IMPRESION */}
            {reportData.length > 0 && (
                <div className="print-area mt-4">
                    <h2 className="text-center mb-4">{reportTitle}</h2>
                    
                    {reportType === 'ranking' && (
                        <table className="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Herramienta</th>
                                    <th>Categoría</th>
                                    <th>Total Préstamos/Cantidad</th>
                                </tr>
                            </thead>
                            <tbody>
                                {reportData.map((item, index) => (
                                    <tr key={index}>
                                        <td>{index + 1}</td>
                                        <td>{item.nameTool}</td>
                                        <td>{item.categoryTool}</td>
                                        {/* Assuming API returns some quantity or just the tool object. Adapt if specific field needed */}
                                        <td>{item.quantity || '-'}</td> 
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}

                    {reportType === 'tools' && (
                        <table className="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Nombre</th>
                                    <th>Categoría</th>
                                    <th>Stock</th>
                                    <th>Precio</th>
                                </tr>
                            </thead>
                            <tbody>
                                {reportData.map((tool) => (
                                    <tr key={tool.idTool}>
                                        <td>{tool.idTool}</td>
                                        <td>{tool.nameTool}</td>
                                        <td>{tool.categoryTool}</td>
                                        <td>{tool.stockTool}</td>
                                        <td>{tool.dailyCharge}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}

                    {reportType === 'clients' && (
                        <table className="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>RUT</th>
                                    <th>Nombre</th>
                                    <th>Email</th>
                                    <th>Teléfono</th>
                                    <th>Estado</th>
                                </tr>
                            </thead>
                            <tbody>
                                {reportData.map((client) => (
                                    <tr key={client.idClient}>
                                        <td>{client.idClient}</td>
                                        <td>{client.rutClient}</td>
                                        <td>{client.nameClient}</td>
                                        <td>{client.emailClient || '-'}</td>
                                        <td>{client.phoneNumberClient}</td>
                                        <td>{client.stateClient}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}

                    {reportType === 'loans' && (
                        <table className="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Cliente</th>
                                    <th>Fecha Inicio</th>
                                    <th>Fecha Fin</th>
                                    <th>Estado</th>
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                {reportData.map((loan) => (
                                    <tr key={loan.idLoan}>
                                        <td>{loan.idLoan}</td>
                                        <td>{loan.idClient?.nameClient || loan.idClient}</td> {/* Handle object or ID ref */}
                                        <td>{displayDate(loan.initDate)}</td>
                                        <td>{displayDate(loan.endDate)}</td>
                                        <td>{loan.stateLoan}</td>
                                        <td>{loan.totalLoan || '-'}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            )}
            
            {reportData.length === 0 && !searched && (
                <div className="no-print alert alert-info text-center">
                    Seleccione una opción para ver el reporte.
                </div>
            )}

            {reportData.length === 0 && searched && (
                <div className="no-print alert alert-warning text-center">
                    No se encontraron resultados.
                </div>
            )}

            {/* BOTÓN VOLVER GENERAL (Mostrado solo en Menu) */}
            {currentView === 'menu' && (
                <div className="text-center mt-5 no-print">
                     <button className="btn btn-primary" onClick={() => window.history.back()}>Volver al Inicio</button>
                </div>
            )}
        </div>
    );
};

export default Reports;