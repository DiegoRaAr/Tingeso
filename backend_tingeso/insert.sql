
INSERT INTO admin (id_admin, name_admin, password_admin, rut_admin, state_admin) VALUES
(1, "Gonzalo Martinez","dameelcodigo","19.345.721-7", "ACTIVO");

INSERT INTO employee (id_employee, name_employee, password_employee, rut_employee, state_employee) VALUES
(1, "Maria Arnaiz", "mararn123", "13.864.106-2", "ACTIVO"),
(2, "Manuel Rojas", "manu1234ro", "12.634.961-8", "ACTIVO"),
(3, "Camila Fuentes", "camiF2023", "15.987.234-1", "ACTIVO");

INSERT INTO kardex (id_kardex, date_kardex) VALUES
(1, '2025-08-28');

INSERT INTO tool (id_tool, category_tool, daily_charge, late_charge, name_tool, repair_charge, state_tool, stock_tool, total_value, id_employee) VAlUES
(1, "Manual", 4590, 5199, "Martillo", "21250", "BUENA", 11, 21250,  1),
(2, "Electrica", 18990, 29900, "Motosierra", "109900", "BUENA", 3, 120650, 2);

INSERT INTO loan (id_loan, end_date, hour_loan, init_date, penalty_loan, state_loan, id_employee, id_kardex, id_tool) VALUES
(1, '2025-09-11', '10:32:45', '2025-09-02', 0, "ACTIVO", 1, 1, 1),
(2, '2025-09-05', '11:54:12', '2025-08-15', 0, "ACTIVO", 2,1,2),
(3, '2025-09-26', '15:39:36', '2025-09-06', 15980, "FINALIZADO",3,1,2);

INSERT INTO client (id_client, email_client, name_client, phone_number_client, rut_client, state_client, id_loan) VALUES
                                                                                                                      (1, "marco.h@gmail.com", "Marco hernandez", "+56926108472", "21.307.176-k", "ACTIVO",1),
                                                                                                                      (2, "andrea.e@gmail.com", "Andrea Esteves", "+56920329105", "22.235.245-1", "ACTIVO",2),
                                                                                                                      (3, "carlos.m@gmail.com", "Carlos Muñoz", "+56981234567", "18.456.789-2", "ACTIVO",3);

INSERT INTO admin_client(id_admin,id_client) VALUES
(1,1),
(1,2),
(1,3);

INSERT INTO admin_employee(id_admin, id_employee) VALUES
(1,1),
(1,2),
(1,3);

INSERT INTO admin_loan(id_admin, id_loan) VALUES
(1,1),
(1,2),
(1,3);

INSERT INTO admin_tool(id_admin, id_tool) VALUES
(1,1),
(1,2),
(1,2);

