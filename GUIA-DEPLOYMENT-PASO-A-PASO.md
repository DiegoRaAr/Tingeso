# 🚀 Guía Paso a Paso: Deployment en EC2 sin Keycloak

## ✅ PASO 1: Subir cambios a GitHub (En tu PC local)

```bash
# 1.1 Ir al directorio del proyecto
cd "/home/diego/Escritorio/Versiones tingeso/6Tingeso/Tingeso"

# 1.2 Agregar todos los cambios
git add .

# 1.3 Hacer commit con mensaje descriptivo
git commit -m "Remove Keycloak - Simplified deployment without authentication"

# 1.4 Subir a GitHub
git push origin main
```

**⏱️ Tiempo estimado:** 1-2 minutos

---

## 🖥️ PASO 2: Conectarse a tu EC2 (Terminal local)

```bash
# 2.1 Conectarse por SSH (reemplaza TU-CLAVE.pem y TU-IP con tus datos)
ssh -i /ruta/a/TU-CLAVE.pem ubuntu@TU-IP-EC2

# Ejemplo:
# ssh -i ~/Downloads/mi-key.pem ubuntu@54.123.45.67
```

**💡 Nota:** Si no tienes una instancia EC2, primero créala:
- Tipo: t2.medium o superior (min 4 GB RAM)
- AMI: Ubuntu 24.04 LTS
- Almacenamiento: 20 GB mínimo
- Security Group: Puertos 22 y 70 abiertos

**⏱️ Tiempo estimado:** 1 minuto

---

## 🧹 PASO 3: Limpiar instalación anterior (En EC2 - OPCIONAL)

```bash
# 3.1 Si ya tenías algo corriendo, detener contenedores
cd ~/Tingeso 2>/dev/null && docker-compose down || echo "No hay contenedores previos"

# 3.2 Limpiar Docker (opcional, libera espacio)
docker system prune -af --volumes

# 3.3 Eliminar directorio anterior si existe
rm -rf ~/Tingeso
```

**⚠️ Advertencia:** Esto elimina TODA la instalación anterior, incluyendo la base de datos.

**⏱️ Tiempo estimado:** 2-3 minutos

---

## 🔧 PASO 4: Instalar dependencias (En EC2 - Primera vez)

```bash
# 4.1 Descargar script de instalación
curl -o ec2-setup.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-setup.sh

# 4.2 Dar permisos de ejecución
chmod +x ec2-setup.sh

# 4.3 Ejecutar instalación
./ec2-setup.sh
```

**¿Qué hace este script?**
- ✅ Instala Docker
- ✅ Instala Docker Compose
- ✅ Instala Git
- ✅ Configura permisos
- ✅ Configura firewall (UFW)

**Durante la ejecución te preguntará:**
- "¿Configurar puertos UFW ahora?" → Escribe `s` (sí)
- "¿Habilitar UFW ahora?" → Escribe `s` (sí)

**⏱️ Tiempo estimado:** 3-5 minutos

---

## 🔄 PASO 5: Cerrar y reconectar sesión SSH (IMPORTANTE)

```bash
# 5.1 Cerrar sesión actual
exit

# 5.2 Reconectar (desde tu PC)
ssh -i /ruta/a/TU-CLAVE.pem ubuntu@TU-IP-EC2
```

**❓ ¿Por qué es necesario?**
Para que los permisos de Docker se apliquen correctamente. Si no lo haces, tendrás que usar `sudo` en cada comando Docker.

**⏱️ Tiempo estimado:** 30 segundos

---

## 🚀 PASO 6: Desplegar la aplicación (En EC2)

```bash
# 6.1 Descargar script de deployment
curl -o ec2-full-deploy.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-full-deploy.sh

# 6.2 Dar permisos de ejecución
chmod +x ec2-full-deploy.sh

# 6.3 Ejecutar deployment
./ec2-full-deploy.sh
```

**¿Qué hace este script?**
1. ✅ Detecta automáticamente tu IP pública
2. ✅ Clona el repositorio desde GitHub
3. ✅ Descarga las imágenes Docker (backend y frontend)
4. ✅ Inicia todos los contenedores (7 en total)
5. ✅ Verifica que todo esté funcionando

**Durante la ejecución verás:**
```
🌐 Detectando IP pública...
📥 Clonando repositorio...
🐳 Iniciando contenedores...
✅ Deployment completado!

🌐 Aplicación disponible en:
   http://TU-IP:70
```

**⏱️ Tiempo estimado:** 2-3 minutos (depende de tu conexión a Internet)

---

## ✅ PASO 7: Verificar que todo funciona (En EC2)

```bash
# 7.1 Ver contenedores corriendo
docker ps

# Deberías ver 7 contenedores:
# - mysql
# - backend1, backend2, backend3
# - frontend
# - nginx-loadbalancer

# 7.2 Ver logs en tiempo real
cd ~/Tingeso
docker-compose logs -f

# Presiona Ctrl + C para salir
```

**✅ Indicadores de éxito:**
- Todos los contenedores en estado "Up"
- No hay errores críticos en los logs
- Backend muestra: "Started Tingeso1Application"
- Frontend muestra: "nginx started"

**⏱️ Tiempo estimado:** 1 minuto

---

## 🌐 PASO 8: Probar desde tu navegador

```bash
# 8.1 Obtener la IP pública de tu EC2
curl ifconfig.me
echo ""
```

**8.2 Abre tu navegador y accede a:**
```
http://TU-IP-EC2:70
```

**Ejemplo:** `http://54.123.45.67:70`

**✅ Deberías ver:**
- La página principal de tu aplicación
- Sistema de préstamo de herramientas
- Menú de navegación funcionando

**⏱️ Tiempo estimado:** 30 segundos

---

## 🎯 RESUMEN DE COMANDOS (Copia y pega completo)

### Para instalación desde cero en EC2:

```bash
# 1. Conectarse
ssh -i tu-clave.pem ubuntu@TU-IP

# 2. Instalar dependencias
curl -o ec2-setup.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-setup.sh
chmod +x ec2-setup.sh
./ec2-setup.sh

# 3. Cerrar y reconectar
exit
ssh -i tu-clave.pem ubuntu@TU-IP

# 4. Desplegar
curl -o ec2-full-deploy.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-full-deploy.sh
chmod +x ec2-full-deploy.sh
./ec2-full-deploy.sh

# 5. Verificar
docker ps
```

---

## 🔄 ACTUALIZACIONES FUTURAS

Si haces cambios en tu código y quieres actualizar EC2:

```bash
# Conectarse a EC2
ssh -i tu-clave.pem ubuntu@TU-IP

# Ir al directorio
cd ~/Tingeso

# Detener contenedores
docker-compose down

# Actualizar código
git pull

# Descargar nuevas imágenes
docker-compose pull

# Reiniciar
docker-compose up -d

# Ver logs
docker-compose logs -f
```

**⏱️ Tiempo estimado:** 2-3 minutos

---

## 🆘 SOLUCIÓN DE PROBLEMAS RÁPIDOS

### Problema: "No puedo conectarme por SSH"
```bash
# Verifica Security Group en AWS Console:
# - Debe tener puerto 22 abierto
# - Verifica que tu IP actual tenga acceso
```

### Problema: "La aplicación no carga en puerto 70"
```bash
# En EC2, verifica el firewall:
sudo ufw status

# Si el puerto 70 no está abierto:
sudo ufw allow 70/tcp
```

### Problema: "Contenedores se reinician constantemente"
```bash
# Ver qué está fallando:
cd ~/Tingeso
docker-compose logs mysql
docker-compose logs backend1

# Solución común: Esperar a que MySQL inicie completamente
sleep 30
docker-compose restart backend1 backend2 backend3
```

### Problema: "Error 502 Bad Gateway"
```bash
# Los backends no están listos, espera 1-2 minutos
# Luego verifica:
docker-compose ps
docker-compose logs backend1
```

### Problema: "No hay espacio en disco"
```bash
# Limpiar Docker
docker system prune -af --volumes

# Ver espacio disponible
df -h
```

---

## 📊 ARQUITECTURA DESPLEGADA

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

**Características:**
- ✅ Sin autenticación (no requiere login)
- ✅ 3 instancias del backend (balanceo de carga)
- ✅ MySQL con volumen persistente
- ✅ Solo puerto 70 necesario
- ✅ 7 contenedores en total (antes eran 8 con Keycloak)

---

## ⏱️ TIEMPO TOTAL

| Paso | Descripción | Tiempo |
|------|-------------|--------|
| 1 | Subir a GitHub | 2 min |
| 2 | Conectar EC2 | 1 min |
| 3 | Limpiar anterior | 3 min |
| 4 | Instalar dependencias | 5 min |
| 5 | Reconectar | 1 min |
| 6 | Desplegar app | 3 min |
| 7 | Verificar | 1 min |
| 8 | Probar navegador | 1 min |
| **TOTAL** | **Primera instalación** | **~17 min** |

**Reinstalaciones posteriores:** Solo pasos 2, 6, 7, 8 → ~6 minutos

---

## 🎉 ¡Listo!

Tu aplicación Tingeso está corriendo en AWS EC2 sin Keycloak.

**URLs:**
- Aplicación: `http://TU-IP-EC2:70`

**Credenciales MySQL (solo interno):**
- Usuario: `diego`
- Contraseña: `diego1234`
- Base de datos: `db_tingeso`

---

**Creado por:** Diego Ramírez  
**Fecha:** 17 de febrero de 2026  
**Repositorio:** https://github.com/DiegoRaAr/Tingeso
