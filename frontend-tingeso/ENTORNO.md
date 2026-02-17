# 🔧 Configuración de Variables de Entorno

## Archivos de entorno:

### `.env.development` (npm run dev)
Usado cuando ejecutas el frontend con `npm run dev`:
- `VITE_API_URL=/api` - Usa el proxy de Vite → Nginx en Docker

### `.env.production` (Docker build)
Usado cuando construyes la imagen Docker:
- `VITE_API_URL=/api` - Peticiones relativas a través de Nginx

### `.env` (fallback)
Valores por defecto si no existe otro archivo

---

## 🚀 Flujo actual (npm run dev):

```
Frontend (localhost:5173)
    ↓ Request a: /api/client
Proxy de Vite (vite.config.js)
    ↓ Redirige a: http://localhost:80/api/client
Nginx (Docker puerto 80)
    ↓ Balancea entre backend1, backend2, backend3
Backend en Docker
    ↓ Responde
Frontend recibe datos
```

---

## ✅ Verificación rápida:

```bash
# 1. Backend funciona
curl http://localhost/api/client/

# 2. Frontend accede al proxy
# Abre DevTools → Network → deberías ver peticiones a /api/...
```
