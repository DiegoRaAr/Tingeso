# ✅ Checklist Pre-Deployment EC2

Usa este checklist antes de desplegar tu aplicación en EC2.

---

## 📋 Antes de empezar

### AWS EC2
- [ ] Tienes una instancia EC2 creada y corriendo
- [ ] Tipo de instancia: Mínimo t3.small (recomendado t3.medium)
- [ ] AMI: Amazon Linux 2023 o Amazon Linux 2
- [ ] Almacenamiento: Mínimo 20 GB
- [ ] Tienes el archivo .pem para conectarte por SSH

### Security Group
- [ ] Puerto 22 (SSH) está abierto para tu IP
- [ ] Puerto 70 (HTTP - Aplicación) está abierto para 0.0.0.0/0
- [ ] Puerto 8080 (Keycloak - opcional) está abierto para 0.0.0.0/0

### Docker Hub
- [ ] Las imágenes están publicadas en Docker Hub:
  - [ ] `diegoraar/backend-tingeso:latest`
  - [ ] `diegoraar/frontend-tingeso:latest`

### Repositorio GitHub
- [ ] Tu código está pusheado al repositorio
- [ ] La URL del repo es: https://github.com/DiegoRaAr/Tingeso

---

## 🔧 Configuración Local (Tu computadora)

### Archivos para subir a EC2
- [ ] `ec2-cleanup.sh` - Script de limpieza
- [ ] `ec2-setup.sh` - Script de instalación
- [ ] `ec2-deploy.sh` - Script de deployment
- [ ] `ec2-full-deploy.sh` - Script completo

### Subir scripts a EC2 (método alternativo)
```bash
# Desde tu computadora local
cd "/home/diego/Escritorio/Versiones tingeso/6Tingeso/Tingeso"

scp -i tu-clave.pem ec2-*.sh ec2-user@TU-IP-EC2:~
```

---

## 🚀 Pasos de Deployment

### Opción A: Deployment Completo (Recomendado)
```bash
# 1. Conectarse a EC2
ssh -i tu-clave.pem ec2-user@TU-IP-EC2

# 2. Instalar dependencias (primera vez)
./ec2-setup.sh
exit
ssh -i tu-clave.pem ec2-user@TU-IP-EC2

# 3. Desplegar
./ec2-full-deploy.sh
```

### Opción B: Paso a Paso
```bash
# 1. Conectarse a EC2
ssh -i tu-clave.pem ec2-user@TU-IP-EC2

# 2. (Opcional) Limpiar instalación anterior
./ec2-cleanup.sh

# 3. Instalar dependencias
./ec2-setup.sh
exit
ssh -i tu-clave.pem ec2-user@TU-IP-EC2

# 4. Desplegar
./ec2-deploy.sh
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
- [ ] Aplicación accesible en: `http://TU-IP-EC2:70`
- [ ] Keycloak accesible en: `http://TU-IP-EC2:70/auth`
- [ ] Login de Keycloak funciona (admin/admin)

### Logs
- [ ] No hay errores críticos en: `docker-compose logs`

---

## 🔍 Testing Básico

### Backend
```bash
# Probar endpoint de health (si existe)
curl http://localhost:70/api/
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
| No puedo conectarme por SSH | Verifica Security Group puerto 22 |
| No carga la aplicación en puerto 70 | Verifica Security Group puerto 70 |
| Contenedores se reinician | Ver logs: `docker-compose logs` |
| Error de memoria | Usa instancia más grande (t3.medium) |
| Error de disco lleno | Ejecuta: `docker system prune -a -f` |

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

---

**¡Buena suerte con el deployment! 🚀**
