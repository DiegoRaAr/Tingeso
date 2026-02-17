#!/bin/bash
# ====================================
# Script de Reparación de Ubuntu
# ====================================
# Este script soluciona problemas comunes de apt/dpkg
# Ejecuta esto ANTES de ec2-setup.sh si tienes errores

echo "🔧 Iniciando reparación del sistema Ubuntu..."
echo ""

# 1. Verificar si somos root o tenemos sudo
if ! sudo -v &> /dev/null; then
    echo "❌ Este script requiere permisos de sudo"
    exit 1
fi

# 2. Detener cualquier proceso de apt en ejecución
echo "🛑 Deteniendo procesos de apt en ejecución..."
sudo killall apt apt-get 2>/dev/null || true
sleep 2

# 3. Limpiar locks de apt/dpkg
echo ""
echo "🔓 Limpiando locks del sistema de paquetes..."
sudo rm -f /var/lib/dpkg/lock-frontend
sudo rm -f /var/lib/dpkg/lock
sudo rm -f /var/cache/apt/archives/lock
sudo rm -f /var/lib/apt/lists/lock

# 4. Reconfigurar dpkg
echo ""
echo "⚙️  Reconfigurando dpkg..."
sudo dpkg --configure -a

# 5. Resolver conflictos de Docker Compose
echo ""
echo "🐳 Resolviendo conflictos de Docker Compose..."
if dpkg -l | grep -q docker-compose-v2; then
    echo "   Removiendo docker-compose-v2 conflictivo..."
    sudo apt-get remove -y docker-compose-v2 || true
fi
if dpkg -l | grep -q docker-compose-plugin; then
    echo "   Removiendo docker-compose-plugin conflictivo..."
    sudo apt-get remove -y docker-compose-plugin || true
fi

# 6. Reparar paquetes rotos
echo ""
echo "🔨 Reparando paquetes rotos..."
sudo apt-get update
sudo apt-get install -f -y

# 7. Limpiar paquetes obsoletos
echo ""
echo "🧹 Limpiando paquetes obsoletos..."
sudo apt-get autoremove -y
sudo apt-get autoclean -y

# 8. Actualizar base de datos de paquetes
echo ""
echo "📦 Actualizando base de datos de paquetes..."
sudo apt-get update

# 9. Verificar el estado
echo ""
echo "✅ Verificando el estado del sistema..."
if sudo apt-get check &> /dev/null; then
    echo "   ✅ Sistema de paquetes verificado correctamente"
else
    echo "   ⚠️  Aún hay algunos problemas. Ejecuta: sudo apt-get check"
fi

echo ""
echo "🎉 ¡Reparación completada!"
echo ""
echo "📋 Próximos pasos:"
echo "   1. Ejecuta: ./ec2-setup.sh"
echo "   2. Si aún tienes problemas, ejecuta:"
echo "      sudo apt-get update"
echo "      sudo apt-get upgrade -y"
echo ""
