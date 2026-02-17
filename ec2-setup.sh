#!/bin/bash
# ====================================
# Script de Instalación EC2 desde Cero
# ====================================
# Este script instala todas las dependencias necesarias para
# correr el proyecto Tingeso en una instancia EC2 de AWS

set -e  # Detener en caso de error

echo "🚀 Iniciando configuración de EC2 desde cero..."
echo ""

# 1. Actualizar el sistema
echo "📦 Actualizando sistema operativo..."
sudo yum update -y

# 2. Instalar Docker
echo ""
echo "🐳 Instalando Docker..."
sudo yum install docker -y

# 3. Iniciar servicio Docker
echo ""
echo "▶️  Iniciando servicio de Docker..."
sudo systemctl start docker
sudo systemctl enable docker

# 4. Agregar usuario ec2-user al grupo docker (para no usar sudo)
echo ""
echo "👤 Configurando permisos de Docker para usuario ec2-user..."
sudo usermod -a -G docker ec2-user

# 5. Instalar Docker Compose
echo ""
echo "🔧 Instalando Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
sudo ln -sf /usr/local/bin/docker-compose /usr/bin/docker-compose

# 6. Instalar Git
echo ""
echo "📥 Instalando Git..."
sudo yum install git -y

# 7. Instalar herramientas útiles
echo ""
echo "🛠️  Instalando herramientas adicionales..."
sudo yum install -y htop wget nano

# 8. Verificar instalaciones
echo ""
echo "✅ Verificando instalaciones..."
echo "   Docker version: $(docker --version)"
echo "   Docker Compose version: $(docker-compose --version)"
echo "   Git version: $(git --version)"

# 9. Configurar firewall (Security Group rules)
echo ""
echo "🔥 Recordatorios de configuración del Security Group en AWS:"
echo "   Asegúrate de tener estos puertos abiertos en tu Security Group:"
echo "   - Puerto 22   (SSH)"
echo "   - Puerto 70   (Nginx - Aplicación principal)"
echo "   - Puerto 8080 (Keycloak - opcional, solo si quieres acceso directo)"
echo "   - Puerto 3307 (MySQL - SOLO para desarrollo, NO recomendado en producción)"

echo ""
echo "✅ Instalación completada exitosamente!"
echo ""
echo "⚠️  IMPORTANTE: Debes cerrar sesión y volver a entrar para que los cambios"
echo "   de permisos de Docker tomen efecto. Ejecuta:"
echo "   exit"
echo "   (y vuelve a conectarte por SSH)"
echo ""
echo "📋 Siguiente paso: Ejecutar el script de deployment"
