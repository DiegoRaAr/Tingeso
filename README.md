# Sistema de prestamo de herramientas

El presente sistema permite gestionar prestamos de herramientas a clientes.

## Flujo del código

El flujo del código se divide en dos partes:

1. Base de datos
2. Backend
3. Frontend
4. Keycloak
5. Docker
6. Jenkins

Las cuales serán explicadas a continuación:

### Base de datos
La base de datos usada es MySQL.

### Backend

El backend esta hecho en Java, usando Spring Boot.
Ademas el backend esta compuesta por:
- Entidades (Entities):
    Las entidades representan las tablas de la base de datos.
- Repositorios (Repositories):
    Los repositorios son interfaces que permiten interactuar con la base de datos.
- Servicios (Services):
    Los servicios son clases que implementan la lógica de negocio.
- Controladores (Controllers):
    Los controladores son las clases que manejan las peticiones HTTP.

### Frontend
El frontend esta hecho en React. Ademas este implementa Axios, el cual ayuda a comunicarse con el backend. El frontend esta compuesto por:
- Servicios (Services):
    Los servicios son funciones que implementan axios para usar los endpoints del backend.
- Componentes (Components):
    Los componentes son las partes visuales de la interfaz de usuario.

### Keycloak
Keycloak es un servidor de autenticación y autorización que se encarga de gestionar los usuarios y sus roles.

### Docker
Docker es un software que permite crear y gestionar contenedores. Esto se puede ver en el archivo docker-compose.yml. El cual permite crear los contenedores de la base de datos, el backend, el frontend y keycloak. 

### Jenkins
Jenkins es un software que permite automatizar tareas. En este caso se encarga de hacer el build del backend y el frontend, y subirlos a DockerHub.

### Nginx
Nginx es un software que permite gestionar el tráfico de la aplicacion. En este caso se encarga de gestionar el tráfico de la aplicacion. Esto se puede ver en el archivo nginx.conf.
