#!/bin/bash
# ====================================
# Script de Verificación del Sistema
# ====================================
# Este script detecta tu sistema operativo y te dice
# si puedes usar los scripts de deployment

echo "╔════════════════════════════════════════════════════════════╗"
echo "║     🔍 Verificación del Sistema                           ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Detectar sistema operativo
if [ -f /etc/os-release ]; then
    . /etc/os-release
    echo "📋 Información del Sistema:"
    echo "   Sistema Operativo: $PRETTY_NAME"
    echo "   ID: $ID"
    echo "   Version: $VERSION"
    echo ""
else
    echo "⚠️  No se pudo detectar el sistema operativo"
    echo ""
fi

# Verificar gestor de paquetes
echo "📦 Gestor de Paquetes:"
if command -v apt-get &> /dev/null; then
    echo "   ✅ APT detectado (Debian/Ubuntu)"
    PKG_MANAGER="apt"
elif command -v yum &> /dev/null; then
    echo "   ✅ YUM detectado (Amazon Linux/RHEL/CentOS)"
    PKG_MANAGER="yum"
elif command -v dnf &> /dev/null; then
    echo "   ✅ DNF detectado (Fedora/RHEL 8+)"
    PKG_MANAGER="dnf"
else
    echo "   ❌ No se detectó un gestor de paquetes compatible"
    echo "   Los scripts están diseñados para: apt, yum, dnf"
    PKG_MANAGER="none"
fi
echo ""

# Verificar Docker
echo "🐳 Docker:"
if command -v docker &> /dev/null; then
    echo "   ✅ Docker instalado: $(docker --version)"
    if docker ps &> /dev/null; then
        echo "   ✅ Docker corriendo y accesible sin sudo"
    else
        echo "   ⚠️  Docker instalado pero necesitas permisos o no está corriendo"
        echo "      Ejecuta: sudo systemctl start docker"
        echo "      Y: sudo usermod -a -G docker $USER (luego cierra sesión)"
    fi
else
    echo "   ❌ Docker NO instalado"
    echo "      Ejecuta: ./ec2-setup.sh para instalar"
fi
echo ""

# Verificar Docker Compose
echo "🔧 Docker Compose:"
if command -v docker-compose &> /dev/null; then
    echo "   ✅ Docker Compose instalado: $(docker-compose --version)"
else
    echo "   ❌ Docker Compose NO instalado"
    echo "      Ejecuta: ./ec2-setup.sh para instalar"
fi
echo ""

# Verificar Git
echo "📥 Git:"
if command -v git &> /dev/null; then
    echo "   ✅ Git instalado: $(git --version)"
else
    echo "   ❌ Git NO instalado"
    echo "      Ejecuta: ./ec2-setup.sh para instalar"
fi
echo ""

# Verificar conectividad
echo "🌐 Conectividad:"
if curl -s --max-time 3 https://www.google.com > /dev/null 2>&1; then
    echo "   ✅ Conexión a internet disponible"
else
    echo "   ⚠️  Problemas de conexión a internet"
fi
echo ""

# Detectar si estamos en EC2
echo "☁️  Entorno:"
if curl -s --max-time 2 http://169.254.169.254/latest/meta-data/public-ipv4 &> /dev/null; then
    PUBLIC_IP=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)
    echo "   ✅ Instancia EC2 detectada"
    echo "   IP Pública: $PUBLIC_IP"
else
    # Intentar detectar IP pública con servicios externos
    PUBLIC_IP=$(curl -s https://api.ipify.org 2>/dev/null)
    if [ -n "$PUBLIC_IP" ]; then
        echo "   ℹ️  No es EC2 (VPS o servidor local)"
        echo "   IP Pública detectada: $PUBLIC_IP"
    else
        echo "   ℹ️  No es EC2 (VPS o servidor local)"
        echo "   ⚠️  No se pudo detectar IP pública (posible red local)"
    fi
fi
echo ""

# Verificar puertos
echo "🔌 Puertos (solo si tienes privilegios):"
if command -v ss &> /dev/null || command -v netstat &> /dev/null; then
    if ss -tuln 2>/dev/null | grep -q ':70 ' || netstat -tuln 2>/dev/null | grep -q ':70 '; then
        echo "   ⚠️  Puerto 70 ya está en uso"
    else
        echo "   ✅ Puerto 70 disponible"
    fi
    
    if ss -tuln 2>/dev/null | grep -q ':8080 ' || netstat -tuln 2>/dev/null | grep -q ':8080 '; then
        echo "   ⚠️  Puerto 8080 ya está en uso"
    else
        echo "   ✅ Puerto 8080 disponible"
    fi
else
    echo "   ℹ️  No se pueden verificar puertos (ss/netstat no disponible)"
fi
echo ""

# Verificar espacio en disco
echo "💾 Espacio en Disco:"
DISK_AVAILABLE=$(df -h / | awk 'NR==2 {print $4}')
echo "   Espacio disponible en /: $DISK_AVAILABLE"
echo ""

# Resumen y recomendaciones
echo "═══════════════════════════════════════════════════════════"
echo "📊 RESUMEN"
echo "═══════════════════════════════════════════════════════════"

CAN_RUN=true

if [ "$PKG_MANAGER" = "none" ]; then
    echo "❌ Sistema no compatible (gestor de paquetes no soportado)"
    CAN_RUN=false
fi

if ! command -v docker &> /dev/null; then
    echo "⚠️  Falta Docker - ejecuta: ./ec2-setup.sh"
    CAN_RUN=false
fi

if ! command -v docker-compose &> /dev/null; then
    echo "⚠️  Falta Docker Compose - ejecuta: ./ec2-setup.sh"
    CAN_RUN=false
fi

if ! command -v git &> /dev/null; then
    echo "⚠️  Falta Git - ejecuta: ./ec2-setup.sh"
    CAN_RUN=false
fi

if [ "$CAN_RUN" = true ]; then
    echo ""
    echo "✅ Tu sistema está listo para deployment!"
    echo ""
    echo "Próximos pasos:"
    echo "  1. Ejecuta: ./ec2-full-deploy.sh"
    echo "  2. O ejecuta: ./ec2-deploy.sh"
else
    echo ""
    echo "❌ Tu sistema NO está listo para deployment"
    echo ""
    echo "Próximos pasos:"
    echo "  1. Ejecuta: ./ec2-setup.sh"
    echo "  2. Cierra sesión y vuelve a entrar"
    echo "  3. Ejecuta: ./check-system.sh (para verificar)"
    echo "  4. Ejecuta: ./ec2-full-deploy.sh"
fi

echo ""
