# ✅ Checklist Pre-Deployment Ubuntu

Usa este checklist antes de desplegar tu aplicación en Ubuntu.

---

## 📋 Antes de empezar

### Servidor Ubuntu
- [ ] Tienes un servidor Ubuntu (20.04, 22.04, o 24.04)
  - [ ] EC2 en AWS, o
  - [ ] VPS (DigitalOcean, Linode, etc.), o
  - [ ] Ubuntu local
- [ ] RAM: Mínimo 4 GB (recomendado 8 GB)
- [ ] Disco: Mínimo 20 GB libre
- [ ] Acceso: SSH o terminal local

### Firewall

#### Si es EC2:
- [ ] Puerto 22 (SSH) está abierto en Security Group
- [ ] Puerto 70 (HTTP - Aplicación) está abierto en Security Group
- [ ] Puerto 8080 (Keycloak - opcional) está abierto en Security Group

#### Si es VPS o Local:
- [ ] UFW configurado (o lo configurarás durante la instalación)

### Docker Hub
- [ ] Las imágenes están publicadas en Docker Hub:
  - [ ] `diegoraar/backend-tingeso:latest`
  - [ ] `diegoraar/frontend-tingeso:latest`

### Repositorio GitHub
- [ ] Tu código está pusheado al repositorio
- [ ] La URL del repo es: https://github.com/DiegoRaAr/Tingeso

---

## 🔧 Configuración Local (Tu computadora)

### Si necesitas subir scripts manualmente a un servidor remoto:
```bash
# Desde tu computadora local
cd "/home/diego/Escritorio/Versiones tingeso/6Tingeso/Tingeso"

# Si es EC2:
scp -i tu-clave.pem *.sh ubuntu@TU-IP:~

# Si es VPS:
scp *.sh usuario@TU-IP:~
```

---

## 🚀 Pasos de Deployment

### Opción A: Deployment Completo (Recomendado)
```bash
# 1. Conectarse al servidor (si es remoto)
ssh -i tu-clave.pem ubuntu@TU-IP-SERVIDOR
# O si es local, solo abre una terminal

# 2. Verificar sistema (opcional)
curl -o check-system.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/check-system.sh
chmod +x check-system.sh
./check-system.sh

# 3. Instalar dependencias (primera vez)
curl -o setup.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-setup.sh
chmod +x setup.sh
./setup.sh

# 4. Cerrar sesión y reconectar (IMPORTANTE)
exit
ssh -i tu-clave.pem ubuntu@TU-IP-SERVIDOR

# 5. Desplegar
curl -o deploy.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-full-deploy.sh
chmod +x deploy.sh
./deploy.sh
```

### Opción B: Si ya tienes los scripts descargados
```bash
# 1. Conectarse
ssh -i tu-clave.pem ubuntu@TU-IP-SERVIDOR

# 2. Verificar (opcional)
./check-system.sh

# 3. Instalar (primera vez)
./ec2-setup.sh
exit
ssh -i tu-clave.pem ubuntu@TU-IP-SERVIDOR

# 4. Desplegar
./ec2-full-deploy.sh
```

---

## ✅ Verificación Post-Deployment

### Contenedores
- [ ] Ejecutar: `docker ps`
- [ ] Deben estar corriendo 8 contenedores:
  - [ ] mysql
  - [ ] backend1
  - [ ] backend2
  - [ ] backend3
  - [ ] keycloak
  - [ ] frontend
  - [ ] nginx-loadbalancer

### Acceso Web
- [ ] Aplicación accesible en: `http://TU-IP:70` (o `http://localhost:70` si es local)
- [ ] Keycloak accesible en: `http://TU-IP:70/auth` (o `http://localhost:70/auth`)
- [ ] Login de Keycloak funciona (admin/admin)

### Logs
- [ ] No hay errores críticos en: `docker-compose logs`

---

## 🔍 Testing Básico

### Backend
```bash
# Probar endpoint del backend a través de nginx
curl http://localhost:70/api/

# Si es remoto
curl http://TU-IP:70/api/
```

### Frontend
```bash
# Ver logs del frontend
docker-compose logs frontend
```

### Keycloak
```bash
# Ver logs de Keycloak
docker-compose logs keycloak
```

### Base de Datos
```bash
# Conectarse a MySQL (desde la EC2)
docker exec -it mysql mysql -u diego -pdiego1234 db_tingeso

# Listar tablas
SHOW TABLES;

# Salir
EXIT;
```

---

## 🆘 Troubleshooting Rápido

| Problema | Solución |
|----------|----------|
| No puedo conectarme por SSH | Verifica Security Group puerto 22 (EC2) o UFW (local) |
| No carga la aplicación en puerto 70 | Verifica Security Group puerto 70 o UFW: `sudo ufw status` |
| Contenedores se reinician | Ver logs: `docker-compose logs -f` |
| Error de memoria | Usa instancia más grande o cierra otros procesos |
| Error de disco lleno | Ejecuta: `docker system prune -a -f` |
| Docker comando no funciona sin sudo | Cierra sesión y vuelve a entrar después de instalar |

---

## 📞 Soporte

Si todo falla:
1. Revisa [DEPLOYMENT-GUIDE.md](DEPLOYMENT-GUIDE.md)
2. Ejecuta: `docker-compose logs -f` y busca errores
3. Verifica: `docker-compose ps` - todos deben estar "Up"

---

## 🎯 Resultado Esperado

Al finalizar deberías poder:
- ✅ Acceder a la aplicación desde tu navegador
- ✅ Hacer login en Keycloak
- ✅ Usar todas las funcionalidades de la aplicación
- ✅ Ver los 8 contenedores corriendo sin problemas

### URLs finales:

**Si es servidor remoto:**
- Aplicación: `http://TU-IP:70`
- Keycloak: `http://TU-IP:70/auth`

**Si es Ubuntu local:**
- Aplicación: `http://localhost:70`
- Keycloak: `http://localhost:70/auth`

---

**¡Buena suerte con el deployment! 🚀**
