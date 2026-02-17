#!/bin/bash
# ====================================
# Script de Deployment en EC2
# ====================================
# Este script despliega la aplicación Tingeso en EC2

set -e  # Detener en caso de error

echo "🚀 Iniciando deployment de Tingeso en EC2..."
echo ""

# Obtener la IP pública de la instancia EC2 desde metadatos
echo "🌐 Obteniendo IP pública de esta instancia EC2..."
PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)

if [ -z "$PUBLIC_IP" ]; then
    echo "❌ Error: No se pudo obtener la IP pública"
    echo "   Asegúrate de estar ejecutando esto en una instancia EC2"
    exit 1
fi

echo "   ✅ IP pública detectada: $PUBLIC_IP"
echo ""

# 1. Clonar o actualizar el repositorio
echo "📥 Descargando código del proyecto..."
cd ~

if [ -d "Tingeso" ]; then
    echo "   Proyecto ya existe, actualizando..."
    cd Tingeso
    git pull
else
    echo "   Clonando proyecto desde GitHub..."
    git clone https://github.com/DiegoRaAr/Tingeso.git
    cd Tingeso
fi

# 2. Actualizar la configuración de Keycloak con la IP pública
echo ""
echo "🔧 Configurando Keycloak con IP pública..."

# Actualizar keycloak.js
cat > frontend-tingeso/src/services/keycloak.js << EOF
import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://${PUBLIC_IP}:70/auth",
  realm: "tingeso-realm",
  clientId: "frontend-app",
}); 

export default keycloak;
EOF

echo "   ✅ Configuración de Keycloak actualizada"

# 3. Actualizar .env.production
cat > frontend-tingeso/.env.production << EOF
# Configuración para producción (Docker en EC2)
# Backend a través de nginx
VITE_API_URL=/api

# Keycloak a través de nginx
VITE_KEYCLOAK_URL=http://${PUBLIC_IP}:70/auth
VITE_KEYCLOAK_REALM=tingeso-realm
VITE_KEYCLOAK_CLIENT=frontend-app
EOF

echo "   ✅ Variables de entorno actualizadas"

# 4. Si necesitas rebuilear el frontend con la nueva configuración
# (solo si no usas las imágenes de Docker Hub directamente)
# Descomenta esta sección si quieres construir las imágenes localmente
# echo ""
# echo "🏗️  Construyendo imágenes Docker localmente..."
# docker-compose build

# 5. Descargar las imágenes más recientes de Docker Hub
echo ""
echo "📦 Descargando últimas imágenes de Docker Hub..."
docker-compose pull

# 6. Detener contenedores antiguos si existen
echo ""
echo "🛑 Deteniendo contenedores antiguos..."
docker-compose down --remove-orphans || true

# 7. Iniciar la aplicación
echo ""
echo "🚀 Iniciando aplicación con Docker Compose..."
docker-compose up -d

# 8. Esperar a que los servicios estén listos
echo ""
echo "⏳ Esperando a que los servicios inicien..."
echo "   (esto puede tomar 1-2 minutos)"
sleep 60

# 9. Verificar estado de los contenedores
echo ""
echo "📊 Estado de los contenedores:"
docker-compose ps

# 10. Mostrar logs recientes
echo ""
echo "📝 Logs recientes de los servicios:"
docker-compose logs --tail=20

echo ""
echo "✅ ¡Deployment completado exitosamente!"
echo ""
echo "🌐 Tu aplicación está disponible en:"
echo "   http://${PUBLIC_IP}:70"
echo ""
echo "🔐 Panel de Keycloak (admin):"
echo "   http://${PUBLIC_IP}:70/auth"
echo "   Usuario: admin"
echo "   Contraseña: admin"
echo ""
echo "📊 Comandos útiles:"
echo "   Ver logs:           docker-compose logs -f"
echo "   Ver estado:         docker-compose ps"
echo "   Reiniciar:          docker-compose restart"
echo "   Detener:            docker-compose down"
echo "   Actualizar código:  git pull && docker-compose pull && docker-compose up -d"
