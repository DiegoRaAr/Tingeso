# 🆘 Soluciones a Errores Comunes

## Error: "Sub-process /usr/bin/dpkg returned an error code (1)"

Este error indica problemas con el gestor de paquetes de Ubuntu.

### Solución rápida:

```bash
# Ejecuta el script de reparación
./fix-ubuntu.sh
```

### Solución manual:

```bash
# 1. Limpiar locks
sudo rm -f /var/lib/dpkg/lock-frontend
sudo rm -f /var/lib/dpkg/lock
sudo rm -f /var/cache/apt/archives/lock
sudo rm -f /var/lib/apt/lists/lock

# 2. Reconfigurar paquetes
sudo dpkg --configure -a

# 3. Reparar dependencias rotas
sudo apt-get update
sudo apt-get install -f -y

# 4. Limpiar y actualizar
sudo apt-get autoremove -y
sudo apt-get autoclean -y
sudo apt-get update

# 5. Intentar instalar de nuevo
./ec2-setup.sh
```

---

## Error: "Could not get lock /var/lib/dpkg/lock-frontend"

Otro proceso está usando apt/dpkg.

### Solución:

```bash
# Esperar o matar procesos
sudo killall apt apt-get dpkg
sleep 5

# Limpiar locks
sudo rm -f /var/lib/dpkg/lock-frontend
sudo rm -f /var/lib/dpkg/lock

# Reintentar
./ec2-setup.sh
```

---

## Error: "E: Unable to locate package docker-ce"

El repositorio de Docker no está configurado correctamente.

### Solución:

```bash
# Agregar repositorio de Docker manualmente
sudo apt-get update
sudo apt-get install -y ca-certificates curl gnupg lsb-release

sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io
```

---

## Error: "permission denied while connecting to Docker daemon"

No tienes permisos para usar Docker sin sudo.

### Solución:

```bash
# Agregar tu usuario al grupo docker
sudo usermod -a -G docker $USER

# Cerrar sesión y volver a entrar (IMPORTANTE)
exit

# Volver a conectar y verificar
docker ps
```

---

## Error: "Cannot connect to the Docker daemon"

Docker no está corriendo.

### Solución:

```bash
# Iniciar Docker
sudo systemctl start docker
sudo systemctl enable docker

# Verificar estado
sudo systemctl status docker
```

---

## Error: "Port 70 is already in use"

Ya hay algo corriendo en el puerto 70.

### Solución:

```bash
# Ver qué está usando el puerto
sudo lsof -i :70
# o
sudo netstat -tulpn | grep :70

# Detener el proceso (reemplaza PID con el número que viste)
sudo kill -9 PID

# O detener tu aplicación anterior
cd ~/Tingeso
docker-compose down
```

---

## Error: "docker: command not found" después de instalar

No cerraste sesión después de instalar Docker.

### Solución:

```bash
# Cerrar sesión
exit

# Volver a conectar
ssh usuario@servidor
# o abrir nueva terminal si es local

# Verificar
docker --version
```

---

## Error: "Connection refused" al acceder a la aplicación

El firewall está bloqueando el puerto o los contenedores no están corriendo.

### Solución:

```bash
# Verificar que los contenedores estén corriendo
docker ps

# Abrir puerto en UFW
sudo ufw allow 70/tcp
sudo ufw reload

# Si es EC2, verifica el Security Group en AWS Console

# Verificar logs
cd ~/Tingeso
docker-compose logs -f
```

---

## Error: "no space left on device"

Se acabó el espacio en disco.

### Solución:

```bash
# Ver espacio disponible
df -h

# Limpiar Docker
docker system prune -a --volumes -f

# Limpiar APT cache
sudo apt-get clean
sudo apt-get autoclean

# Si aún falta espacio, aumenta el disco en AWS/VPS
```

---

## Error: Contenedores se reinician constantemente

Hay un error en la aplicación o falta memoria.

### Solución:

```bash
# Ver logs para identificar el problema
cd ~/Tingeso
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs backend1
docker-compose logs mysql

# Ver uso de recursos
docker stats

# Si es problema de memoria:
# - Usa una instancia más grande
# - O reduce las instancias del backend en docker-compose.yml
```

---

## Error: "Cannot pull image from Docker Hub"

Problemas de red o la imagen no existe.

### Solución:

```bash
# Verificar conexión
ping google.com

# Verificar que las imágenes existen en Docker Hub
curl https://hub.docker.com/v2/repositories/diegoraar/backend-tingeso/tags/

# Intentar pull manual
docker pull diegoraar/backend-tingeso:latest
docker pull diegoraar/frontend-tingeso:latest

# Si funcionó, intenta el deployment de nuevo
cd ~/Tingeso
docker-compose up -d
```

---

## Error: "Git command not found"

Git no está instalado o no está en el PATH.

### Solución:

```bash
# Instalar Git manualmente
sudo apt-get update
sudo apt-get install -y git

# Verificar
git --version
```

---

## 🔧 Script de Diagnóstico Completo

Si tienes múltiples problemas, ejecuta:

```bash
# Verificar todo el sistema
./check-system.sh

# Reparar problemas de paquetes
./fix-ubuntu.sh

# Reinstalar todo
./ec2-setup.sh
```

---

## 📞 Ayuda Adicional

Si ninguna solución funciona:

1. **Ver todos los logs:**
   ```bash
   cd ~/Tingeso
   docker-compose logs > logs.txt
   cat logs.txt
   ```

2. **Verificar servicios del sistema:**
   ```bash
   sudo systemctl status docker
   sudo systemctl status docker.socket
   ```

3. **Reinicio completo:**
   ```bash
   sudo reboot
   ```

4. **Reinstalación limpia:**
   ```bash
   ./ec2-cleanup.sh
   ./fix-ubuntu.sh
   ./ec2-setup.sh
   exit  # cerrar sesión
   # volver a conectar
   ./ec2-full-deploy.sh
   ```
