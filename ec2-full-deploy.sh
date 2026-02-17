#!/bin/bash
# ====================================
# Script Maestro de Deployment para Ubuntu
# ====================================
# Este script ejecuta todo el proceso completo

echo "╔════════════════════════════════════════════════════════════╗"
echo "║     🚀 TINGESO - Deployment Automático en Ubuntu          ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

echo "Este script realizará las siguientes acciones:"
echo "  1. ✅ Verificar instalaciones necesarias"
echo "  2. 🧹 Limpiar contenedores y volúmenes antiguos (opcional)"
echo "  3. 📥 Clonar/actualizar el proyecto"
echo "  4. 🔧 Configurar automáticamente con tu IP"
echo "  5. 🚀 Desplegar la aplicación"
echo ""
read -p "¿Deseas continuar? (s/n): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Ss]$ ]]; then
    exit 1
fi

# Paso 1: Verificar Docker
echo ""
echo "═══════════════════════════════════════════════════════════"
echo "📋 Paso 1/5: Verificando instalaciones"
echo "═══════════════════════════════════════════════════════════"

if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado"
    echo "   Ejecuta primero: ./ec2-setup.sh"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose no está instalado"
    echo "   Ejecuta primero: ./ec2-setup.sh"
    exit 1
fi

echo "✅ Docker: $(docker --version)"
echo "✅ Docker Compose: $(docker-compose --version)"

# Verificar permisos de Docker
if ! docker ps &> /dev/null; then
    echo "⚠️  No tienes permisos para usar Docker sin sudo"
    echo "   Ejecuta: sudo usermod -a -G docker $USER"
    echo "   Luego cierra sesión y vuelve a entrar"
    exit 1
fi

# Paso 2: Opción de limpieza
echo ""
echo "═══════════════════════════════════════════════════════════"
echo "🧹 Paso 2/5: Limpieza (opcional)"
echo "═══════════════════════════════════════════════════════════"
read -p "¿Deseas limpiar contenedores e imágenes antiguas? (s/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Ss]$ ]]; then
    echo "🗑️  Limpiando..."
    docker-compose down 2>/dev/null || true
    docker stop $(docker ps -aq) 2>/dev/null || true
    docker rm $(docker ps -aq) 2>/dev/null || true
    docker system prune -f
    echo "✅ Limpieza completada"
fi

# Paso 3: Clonar/actualizar proyecto
echo ""
echo "═══════════════════════════════════════════════════════════"
echo "📥 Paso 3/5: Obteniendo código del proyecto"
echo "═══════════════════════════════════════════════════════════"

cd ~
if [ -d "Tingeso" ]; then
    echo "Proyecto existente encontrado, actualizando..."
    cd Tingeso
    git pull
else
    echo "Clonando proyecto desde GitHub..."
    git clone https://github.com/DiegoRaAr/Tingeso.git
    cd Tingeso
fi

# Paso 4: Configuración automática
echo ""
echo "═══════════════════════════════════════════════════════════"
echo "🔧 Paso 4/5: Configuración automática"
echo "═══════════════════════════════════════════════════════════"

# Intentar detectar IP pública
PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4 2>/dev/null)

if [ -z "$PUBLIC_IP" ]; then
    # Intentar con servicios externos
    PUBLIC_IP=$(curl -s https://api.ipify.org 2>/dev/null || curl -s https://ifconfig.me 2>/dev/null || curl -s https://icanhazip.com 2>/dev/null)
fi

if [ -z "$PUBLIC_IP" ]; then
    read -p "No se pudo detectar la IP automáticamente. Ingresa tu IP pública: " PUBLIC_IP
    
    if [ -z "$PUBLIC_IP" ]; then
        echo "❌ Error: Debes proporcionar una IP pública"
        exit 1
    fi
fi

echo "📍 IP pública: $PUBLIC_IP"

# Actualizar .env.production
cat > frontend-tingeso/.env.production << EOF
# Configuración para producción (Docker en Ubuntu)
VITE_API_URL=/api
EOF

echo "✅ Archivos de configuración actualizados"

# Paso 5: Desplegar
echo ""
echo "═══════════════════════════════════════════════════════════"
echo "🚀 Paso 5/5: Desplegando aplicación"
echo "═══════════════════════════════════════════════════════════"

echo "📦 Descargando imágenes de Docker Hub..."
docker-compose pull

echo "🚀 Iniciando servicios..."
docker-compose up -d

echo ""
echo "⏳ Esperando a que los servicios inicien (60 segundos)..."
for i in {60..1}; do
    echo -ne "   $i segundos restantes...\r"
    sleep 1
done
echo ""

# Verificación final
echo ""
echo "═══════════════════════════════════════════════════════════"
echo "✅ DEPLOYMENT COMPLETADO"
echo "═══════════════════════════════════════════════════════════"
echo ""
echo "📊 Estado de los contenedores:"
docker-compose ps
echo ""
echo "🌐 Tu aplicación está disponible en:"
echo "   → http://${PUBLIC_IP}:70"
echo ""
echo " Comandos útiles:"
echo "   Ver logs:    docker-compose logs -f"
echo "   Ver estado:  docker-compose ps"
echo "   Reiniciar:   docker-compose restart"
echo "   Detener:     docker-compose down"
echo ""
echo "Para más información, consulta DEPLOYMENT-GUIDE.md"
echo "═══════════════════════════════════════════════════════════"
