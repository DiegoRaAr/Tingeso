# 🚀 Guía Completa de Deployment en AWS EC2

## 📋 Tabla de Contenidos
1. [Requisitos Previos](#requisitos-previos)
2. [Configuración del Security Group en AWS](#configuración-del-security-group)
3. [Paso 1: Conectarse a la EC2](#paso-1-conectarse-a-la-ec2)
4. [Paso 2: Limpiar la EC2 (Formateo)](#paso-2-limpiar-la-ec2)
5. [Paso 3: Instalar Dependencias](#paso-3-instalar-dependencias)
6. [Paso 4: Desplegar la Aplicación](#paso-4-desplegar-la-aplicación)
7. [Verificación y Testing](#verificación-y-testing)
8. [Comandos Útiles](#comandos-útiles)
9. [Troubleshooting](#troubleshooting)

---

## 📌 Requisitos Previos

- Una instancia EC2 en AWS (recomendado: Amazon Linux 2023 o Amazon Linux 2)
- Memoria RAM: Mínimo 4 GB (recomendado 8 GB)
- Espacio en disco: Mínimo 20 GB
- Acceso SSH a la instancia con tu archivo .pem
- Tu cuenta de Docker Hub debe tener las imágenes:
  - `diegoraar/backend-tingeso:latest`
  - `diegoraar/frontend-tingeso:latest`

---

## 🔐 Configuración del Security Group

**IMPORTANTE:** Antes de empezar, configura tu Security Group en AWS Console:

### Reglas de Entrada Requeridas:

| Tipo         | Puerto | Origen        | Descripción                    |
|-------------|--------|---------------|--------------------------------|
| SSH         | 22     | Tu IP / 0.0.0.0/0 | Acceso SSH                |
| Custom TCP  | 70     | 0.0.0.0/0     | Aplicación web (Nginx)         |
| Custom TCP  | 8080   | 0.0.0.0/0     | Keycloak (opcional)            |

### Pasos para configurar:
1. Ve a AWS Console → EC2 → Security Groups
2. Selecciona el Security Group de tu instancia
3. Click en "Edit inbound rules"
4. Agrega las reglas de la tabla anterior
5. Click en "Save rules"

---

## 🔌 Paso 1: Conectarse a la EC2

Desde tu computadora local, conéctate a tu EC2:

```bash
# Cambia los permisos de tu archivo .pem (solo la primera vez)
chmod 400 tu-clave.pem

# Conéctate por SSH (reemplaza con tus datos)
ssh -i tu-clave.pem ec2-user@tu-ip-publica-ec2

# Ejemplo:
# ssh -i tingeso-key.pem ec2-user@54.94.174.49
```

Una vez conectado, verás un prompt como: `[ec2-user@ip-xxx-xxx-xxx-xxx ~]$`

---

## 🧹 Paso 2: Limpiar la EC2

Si ya tenías cosas instaladas y quieres empezar de cero, ejecuta:

```bash
# Descargar el script de limpieza
curl -o cleanup.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-cleanup.sh

# Dar permisos de ejecución
chmod +x cleanup.sh

# Ejecutar limpieza
./cleanup.sh
```

**O si tienes los scripts localmente, súbelos:**

```bash
# Desde tu computadora local (otra terminal):
scp -i tu-clave.pem ec2-cleanup.sh ec2-user@tu-ip-ec2:~
scp -i tu-clave.pem ec2-setup.sh ec2-user@tu-ip-ec2:~
scp -i tu-clave.pem ec2-deploy.sh ec2-user@tu-ip-ec2:~
```

El script eliminará:
- ✅ Todos los contenedores de Docker
- ✅ Todas las imágenes de Docker
- ✅ Todos los volúmenes
- ✅ Carpetas de proyectos antiguos

---

## 🛠️ Paso 3: Instalar Dependencias

Ahora vamos a instalar todo lo necesario desde cero:

```bash
# Si subiste el script desde tu computadora local:
chmod +x ec2-setup.sh
./ec2-setup.sh

# O descárgalo desde GitHub:
curl -o setup.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-setup.sh
chmod +x setup.sh
./setup.sh
```

Este script instalará:
- ✅ Docker
- ✅ Docker Compose
- ✅ Git
- ✅ Herramientas útiles (htop, wget, nano)

**⚠️ IMPORTANTE:** Después de la instalación, debes **cerrar sesión y volver a entrar**:

```bash
exit

# Vuelve a conectarte
ssh -i tu-clave.pem ec2-user@tu-ip-ec2
```

Esto es necesario para que los permisos de Docker tomen efecto.

---

## 🚀 Paso 4: Desplegar la Aplicación

Ahora sí, vamos a desplegar tu aplicación:

```bash
# Si subiste el script:
chmod +x ec2-deploy.sh
./ec2-deploy.sh

# O descárgalo desde GitHub:
curl -o deploy.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-deploy.sh
chmod +x deploy.sh
./deploy.sh
```

### ¿Qué hace este script?

1. 🌐 Detecta automáticamente la IP pública de tu EC2
2. 📥 Clona/actualiza el repositorio desde GitHub
3. 🔧 Configura Keycloak con la IP correcta
4. 📦 Descarga las imágenes de Docker Hub
5. 🚀 Inicia todos los servicios con Docker Compose
6. ✅ Verifica que todo esté funcionando

**Este proceso tarda 1-2 minutos.** Verás el progreso en la terminal.

---

## ✅ Verificación y Testing

### 1. Verificar que los contenedores estén corriendo:

```bash
docker ps
```

Debes ver 8 contenedores corriendo:
- mysql
- backend1, backend2, backend3
- keycloak
- frontend
- nginx-loadbalancer

### 2. Ver logs de los servicios:

```bash
cd ~/Tingeso
docker-compose logs -f
```

Presiona `Ctrl + C` para salir de los logs.

### 3. Probar la aplicación en tu navegador:

Abre tu navegador y ve a:
- **Aplicación:** `http://TU-IP-EC2:70`
- **Keycloak Admin:** `http://TU-IP-EC2:70/auth`
  - Usuario: `admin`
  - Contraseña: `admin`

Por ejemplo: `http://54.94.174.49:70`

### 4. Verificar el estado de salud:

```bash
# Ver estado de todos los servicios
docker-compose ps

# Ver uso de recursos
docker stats
```

---

## 🛠️ Comandos Útiles

### Gestión de la Aplicación

```bash
cd ~/Tingeso

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f backend1
docker-compose logs -f keycloak
docker-compose logs -f frontend

# Reiniciar todos los servicios
docker-compose restart

# Reiniciar un servicio específico
docker-compose restart backend1

# Detener la aplicación
docker-compose down

# Detener y eliminar volúmenes (CUIDADO: borra la BD)
docker-compose down -v

# Iniciar la aplicación
docker-compose up -d

# Ver estado de los contenedores
docker-compose ps
docker ps -a
```

### Actualizar la Aplicación

```bash
cd ~/Tingeso

# Obtener últimos cambios del código
git pull

# Descargar últimas imágenes de Docker Hub
docker-compose pull

# Reiniciar con nuevas imágenes
docker-compose down
docker-compose up -d
```

### Monitoreo

```bash
# Ver uso de CPU/RAM de contenedores
docker stats

# Ver espacio en disco
df -h

# Limpiar imágenes antiguas
docker image prune -a

# Ver logs del sistema
sudo journalctl -u docker
```

---

## 🔧 Troubleshooting

### Problema: "No se puede conectar a la aplicación"

**Solución:**
1. Verifica que el puerto 70 esté abierto en el Security Group
2. Verifica que los contenedores estén corriendo: `docker ps`
3. Revisa los logs: `docker-compose logs -f`

### Problema: "Error de conexión a la base de datos"

**Solución:**
```bash
# Reiniciar MySQL y los backends
docker-compose restart mysql
sleep 10
docker-compose restart backend1 backend2 backend3
```

### Problema: "Keycloak no carga"

**Solución:**
```bash
# Ver logs de Keycloak
docker-compose logs keycloak

# Reiniciar Keycloak
docker-compose restart keycloak
```

### Problema: "Memory out of error"

**Solución:**
Tu instancia EC2 no tiene suficiente RAM. Opciones:
1. Subir a una instancia más grande (t3.medium o t3.large)
2. Reducir el número de instancias del backend en docker-compose.yml

### Problema: "No space left on device"

**Solución:**
```bash
# Limpiar Docker
docker system prune -a --volumes -f

# Si aún así no hay espacio, aumenta el tamaño del disco en AWS
```

### Problema: Los contenedores se reinician constantemente

**Solución:**
```bash
# Ver logs para detectar el error
docker-compose logs

# Verificar el estado
docker-compose ps

# Revisar logs del sistema
sudo journalctl -u docker -f
```

---

## 🔄 Actualizar Imágenes en Docker Hub

Si haces cambios en el código y quieres actualizar:

### Backend:

```bash
# Local en tu computadora
cd backend_tingeso
mvn clean package -DskipTests
docker build -t diegoraar/backend-tingeso:latest .
docker push diegoraar/backend-tingeso:latest
```

### Frontend:

```bash
# Local en tu computadora
cd frontend-tingeso
docker build -t diegoraar/frontend-tingeso:latest .
docker push diegoraar/frontend-tingeso:latest
```

Luego en la EC2:
```bash
cd ~/Tingeso
docker-compose pull
docker-compose down
docker-compose up -d
```

---

## 📊 Arquitectura del Sistema

```
Internet
    ↓
[Puerto 70] → Nginx Load Balancer
                ↓
    ┌──────────┼───────────┐
    ↓          ↓           ↓
Backend1   Backend2   Backend3
    └──────────┼───────────┘
               ↓
           [MySQL]
    
[Frontend] ← Nginx Load Balancer
               ↓
          [Keycloak] ← MySQL
```

---

## 📝 Notas Importantes

1. **Seguridad:**
   - Cambia las contraseñas por defecto en producción
   - No expongas el puerto 3307 (MySQL) en el Security Group
   - Considera usar HTTPS con certificado SSL

2. **Backups:**
   - La base de datos está en un volumen Docker
   - Para backup: `docker exec mysql mysqldump -u root -proot db_tingeso > backup.sql`

3. **Costos:**
   - No olvides detener las instancias EC2 cuando no las uses
   - `docker-compose down` para liberar recursos

4. **Escalabilidad:**
   - Puedes agregar más instancias del backend en docker-compose.yml
   - Nginx balanceará automáticamente la carga

---

## 🎉 ¡Listo!

Tu aplicación Tingeso ahora está corriendo en AWS EC2. Si tienes problemas, revisa la sección de [Troubleshooting](#troubleshooting).

Para soporte adicional, contacta al equipo o revisa los logs con `docker-compose logs -f`.
