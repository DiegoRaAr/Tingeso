#!/bin/bash
# ====================================
# Script de Instalación para Ubuntu
# ====================================
# Este script instala todas las dependencias necesarias para
# correr el proyecto Tingeso en Ubuntu (local o EC2)

set -e  # Detener en caso de error

echo "🚀 Iniciando configuración de Ubuntu desde cero..."
echo ""

# Verificar y limpiar locks si es necesario
echo "🔍 Verificando estado del sistema de paquetes..."
if ! sudo apt-get check &> /dev/null; then
    echo "⚠️  Detectados problemas con el sistema de paquetes"
    echo "   Ejecutando reparación automática..."
    sudo rm -f /var/lib/dpkg/lock-frontend /var/lib/dpkg/lock /var/cache/apt/archives/lock /var/lib/apt/lists/lock 2>/dev/null || true
    sudo dpkg --configure -a
    sudo apt-get install -f -y
fi

# 1. Actualizar el sistema
echo ""
echo "📦 Actualizando sistema operativo..."
sudo apt-get update
sudo apt-get upgrade -y

# 2. Instalar dependencias previas
echo ""
echo "📦 Instalando dependencias previas..."
sudo apt-get install -y \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

# 3. Agregar repositorio oficial de Docker
echo ""
echo "🔑 Agregando repositorio de Docker..."
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# 4. Instalar Docker
echo ""
echo "🐳 Instalando Docker..."
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# 5. Iniciar servicio Docker
echo ""
echo "▶️  Iniciando servicio de Docker..."
sudo systemctl start docker
sudo systemctl enable docker

# 6. Agregar usuario actual al grupo docker (para no usar sudo)
echo ""
CURRENT_USER=$(whoami)
echo "👤 Configurando permisos de Docker para usuario $CURRENT_USER..."
sudo usermod -a -G docker $CURRENT_USER

# 7. Instalar Docker Compose standalone
echo ""
echo "🔧 Instalando Docker Compose standalone..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 8. Instalar Git
echo ""
echo "📥 Instalando Git..."
sudo apt-get install -y git

# 9. Instalar herramientas útiles
echo ""
echo "🛠️  Instalando herramientas adicionales..."
sudo apt-get install -y htop wget nano curl net-tools

# 10. Verificar instalaciones
echo ""
echo "✅ Verificando instalaciones..."
echo "   Docker version: $(docker --version)"
echo "   Docker Compose version: $(docker-compose --version)"
echo "   Git version: $(git --version)"

# 11. Configurar firewall UFW (opcional)
echo ""
echo "🔥 Configuración del Firewall UFW:"
if command -v ufw &> /dev/null; then
    echo "   UFW detectado. ¿Quieres configurar los puertos? (s/n)"
    read -r -n 1 CONFIGURE_UFW
    echo ""
    if [[ $CONFIGURE_UFW =~ ^[Ss]$ ]]; then
        echo "   Configurando puertos en UFW..."
        sudo ufw allow 22/tcp comment 'SSH'
        sudo ufw allow 70/tcp comment 'Aplicación Tingeso'
        
        echo "   ¿Quieres habilitar UFW ahora? (s/n)"
        read -r -n 1 ENABLE_UFW
        echo ""
        if [[ $ENABLE_UFW =~ ^[Ss]$ ]]; then
            sudo ufw --force enable
            echo "   ✅ UFW habilitado"
        else
            echo "   ℹ️  UFW configurado pero no habilitado"
        fi
    else
        echo "   ⏭️  Configuración de UFW omitida"
    fi
else
    echo "   ℹ️  UFW no está instalado (esto es normal en algunas instalaciones)"
    echo "   Si estás en EC2, usa Security Groups en AWS Console"
fi

echo ""
echo "✅ Instalación completada exitosamente!"
echo ""
echo "⚠️  IMPORTANTE: Debes cerrar sesión y volver a entrar para que los cambios"
echo "   de permisos de Docker tomen efecto. Ejecuta:"
echo ""
echo "   exit"
echo ""
echo "   Luego vuelve a conectarte y ejecuta: ./check-system.sh para verificar"
echo ""
echo "📋 Siguiente paso: ./ec2-full-deploy.sh (después de reconectarte)"
