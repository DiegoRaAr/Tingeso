# 🚀 Guía Completa de Deployment en Ubuntu

## 📋 Tabla de Contenidos
1. [Requisitos Previos](#requisitos-previos)
2. [Configuración de Firewall](#configuración-de-firewall)
3. [Paso 1: Conectarse al Servidor](#paso-1-conectarse-al-servidor)
4. [Paso 2: Verificar el Sistema](#paso-2-verificar-el-sistema)
5. [Paso 3: Instalar Dependencias](#paso-3-instalar-dependencias)
6. [Paso 4: Desplegar la Aplicación](#paso-4-desplegar-la-aplicación)
7. [Verificación y Testing](#verificación-y-testing)
8. [Comandos Útiles](#comandos-útiles)
9. [Troubleshooting](#troubleshooting)

---

## 📌 Requisitos Previos

- Un servidor Ubuntu (20.04, 22.04, o 24.04)
  - Puede ser: EC2 en AWS, VPS, o Ubuntu local
- Memoria RAM: Mínimo 4 GB (recomendado 8 GB)
- Espacio en disco: Mínimo 20 GB
- Acceso SSH o terminal local
- Tu cuenta de Docker Hub debe tener las imágenes:
  - `diegoraar/backend-tingeso:latest`
  - `diegoraar/frontend-tingeso:latest`

---

## 🔐 Configuración de Firewall

### Si estás en AWS EC2:

**Configura tu Security Group en AWS Console:**

#### Reglas de Entrada Requeridas:

| Tipo         | Puerto | Origen        | Descripción                    |
|-------------|--------|---------------|--------------------------------|
| SSH         | 22     | Tu IP / 0.0.0.0/0 | Acceso SSH                |
| Custom TCP  | 70     | 0.0.0.0/0     | Aplicación web (Nginx)         |

#### Pasos para configurar:
1. Ve a AWS Console → EC2 → Security Groups
2. Selecciona el Security Group de tu instancia
3. Click en "Edit inbound rules"
4. Agrega las reglas de la tabla anterior
5. Click en "Save rules"

### Si estás en VPS o Ubuntu Local:

**Usa UFW (Uncomplicated Firewall):**

```bash
# Permitir SSH
sudo ufw allow 22/tcp

# Permitir aplicación web
sudo ufw allow 70/tcp

# Habilitar firewall
sudo ufw enable

# Ver estado
sudo ufw status
```

---

## 🔌 Paso 1: Conectarse al Servidor

### Si es EC2 en AWS:

```bash
# Cambia los permisos de tu archivo .pem (solo la primera vez)
chmod 400 tu-clave.pem

# Conéctate por SSH
ssh -i tu-clave.pem ubuntu@tu-ip-publica-ec2

# Ejemplo:
# ssh -i tingeso-key.pem ubuntu@54.94.174.49
```

### Si es VPS u otro servidor:

```bash
# Conéctate por SSH normalmente
ssh usuario@tu-ip-servidor

# Ejemplo:
# ssh diego@192.168.1.100
```

### Si es Ubuntu Local:

```bash
# Solo abre una terminal (Ctrl + Alt + T)
# No necesitas SSH
```

Una vez conectado/en la terminal, continúa con el siguiente paso.

---

## 🔍 Paso 2: Verificar el Sistema

Antes de instalar, verifica que tu sistema sea compatible:

```bash
# Descargar script de verificación
curl -o check-system.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/check-system.sh
chmod +x check-system.sh
./check-system.sh
```

Este script te dirá:
- ✅ Qué está instalado
- ❌ Qué falta
- 📋 Qué hacer a continuación

---

## 🧹 Paso 2 (Opcional): Limpiar Instalación Anterior

Si ya tenías Docker y contenedores corriendo, puedes limpiar todo:

```bash
# Descargar el script de limpieza
curl -o cleanup.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-cleanup.sh
chmod +x cleanup.sh
./cleanup.sh
```

Este script eliminará:
- ✅ Todos los contenedores de Docker
- ✅ Todas las imágenes de Docker
- ✅ Todos los volúmenes
- ✅ Carpetas de proyectos antiguos

**⚠️ Cuidado:** Esto borra TODA la información de Docker. Solo hazlo si quieres empezar desde cero.

---

## 🛠️ Paso 3: Instalar Dependencias

Ahora vamos a instalar Docker, Docker Compose y Git:

```bash
# Descargar script de instalación
curl -o setup.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-setup.sh
chmod +x setup.sh
./setup.sh
```

Este script instalará:
- ✅ Docker (desde repositorio oficial)
- ✅ Docker Compose
- ✅ Git
- ✅ Herramientas útiles (htop, wget, nano)

Durante la instalación, te preguntará si quieres configurar UFW (firewall). Responde:
- **s** = Sí, configurar puertos automáticamente (recomendado)
- **n** = No, lo configuraré manualmente después

**⚠️ MUY IMPORTANTE:** Después de la instalación, debes **cerrar sesión y volver a entrar**:

```bash
exit

# Vuelve a conectarte
ssh -i tu-clave.pem ubuntu@tu-ip
# o si es local, solo abre otra terminal
```

Esto es necesario para que los permisos de Docker tomen efecto.

### Verificar que todo funcionó:

```bash
# Verificar Docker
docker --version
docker ps

# Verificar Docker Compose
docker-compose --version

# Verificar Git
git --version
```

Si ves las versiones sin errores, ¡estás listo!

---

## 🚀 Paso 4: Desplegar la Aplicación

Ahora sí, vamos a desplegar tu aplicación:

```bash
# Descargar script de deployment
curl -o deploy.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-full-deploy.sh
chmod +x deploy.sh
./deploy.sh
```

### ¿Qué hace este script?

1. 🌐 Detecta automáticamente tu IP pública (EC2, VPS, o local)
2. 📥 Clona/actualiza el repositorio desde GitHub
3. � Descarga las imágenes de Docker Hub
4. 🚀 Inicia todos los servicios con Docker Compose
5. ✅ Verifica que todo esté funcionando

**Este proceso tarda 1-2 minutos.** Verás el progreso en la terminal.

### Si estás en Ubuntu Local:

Cuando el script te pida la IP, escribe `localhost`:

```
Ingresa la IP pública o 'localhost': localhost
```

Luego podrás acceder en: `http://localhost:70`

---

## ✅ Verificación y Testing

### 1. Verificar que los contenedores estén corriendo:

```bash
cd ~/Tingeso
docker ps
```

Debes ver 7 contenedores corriendo:
- mysql
- backend1, backend2, backend3
- frontend
- nginx-loadbalancer

### 2. Ver logs de los servicios:

```bash
cd ~/Tingeso
docker-compose logs -f
```

Presiona `Ctrl + C` para salir de los logs.

### 3. Probar la aplicación en tu navegador:

#### Si está en servidor remoto (EC2/VPS):
- **Aplicación:** `http://TU-IP-SERVIDOR:70`

#### Si está en Ubuntu local:
- **Aplicación:** `http://localhost:70`

**Nota:** Esta aplicación no requiere autenticación.

### 4. Verificar el estado de salud:

```bash
# Ver estado de todos los servicios
docker-compose ps

# Ver uso de recursos
docker stats

# Ver logs de un servicio específico
docker-compose logs backend1
docker-compose logs frontend
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
