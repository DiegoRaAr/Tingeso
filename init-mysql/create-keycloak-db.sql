-- Crear la base de datos para Keycloak
CREATE DATABASE IF NOT EXISTS keycloak_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- En MySQL 8.0+, el usuario 'diego' ya se crea via MYSQL_USER en docker-compose
-- Solo necesitamos darle permisos sobre keycloak_db
GRANT ALL PRIVILEGES ON keycloak_db.* TO 'diego'@'%';
FLUSH PRIVILEGES;