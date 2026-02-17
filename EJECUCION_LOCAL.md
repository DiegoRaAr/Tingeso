# 🚀 Guía de Ejecución Local vs Producción

## 🏠 Desarrollo Local

Para correr la aplicación en tu máquina local:

```bash
docker compose -f docker-compose.local.yml up -d
```

**Acceso:** http://localhost

**Características:**
- ✅ Solo HTTP (sin SSL)
- ✅ No requiere certificados
- ✅ Puerto 80
- ✅ `nginx.local.conf` simplificado

**Para detener:**
```bash
docker compose -f docker-compose.local.yml down
```

---

## ☁️ Producción (AWS EC2)

Para desplegar en el servidor con HTTPS:

```bash
docker compose up -d
```

**Acceso:** https://dirarnaiz.me

**Características:**
- ✅ HTTPS con SSL de Let's Encrypt
- ✅ Redirección automática HTTP → HTTPS
- ✅ Certificados en `/etc/letsencrypt`
- ✅ Puertos 80 y 443
- ✅ `nginx.conf` con SSL completo

---

## 📦 Comandos Útiles

### Ver logs
```bash
# Local
docker compose -f docker-compose.local.yml logs -f

# Producción
docker compose logs -f
```

### Reconstruir imágenes
```bash
# Local (si descomentaste build)
docker compose -f docker-compose.local.yml up -d --build

# Producción
docker compose up -d --build
```

### Ver contenedores activos
```bash
docker ps
```

### Reiniciar servicio específico
```bash
# Local
docker compose -f docker-compose.local.yml restart nginx-loadbalancer

# Producción
docker compose restart nginx-loadbalancer
```

---

## 🔧 Troubleshooting

### Puerto 80 ocupado en local
```bash
# Ver qué usa el puerto 80
sudo lsof -i :80

# Cambiar puerto en docker-compose.local.yml
# Cambia "80:80" por "8080:80"
# Accede en http://localhost:8080
```

### MySQL no inicia
```bash
# Limpiar volumen
docker compose -f docker-compose.local.yml down -v
docker compose -f docker-compose.local.yml up -d
```

### Ver errores de backend
```bash
docker logs backend1
docker logs backend2
docker logs backend3
```
