#!/bin/bash
# ====================================
# Script de Limpieza Completa de EC2
# ====================================
# Este script elimina todos los contenedores, imágenes, volúmenes y 
# configuraciones de Docker para empezar desde cero

echo "🧹 Iniciando limpieza completa de EC2..."

# 1. Detener todos los contenedores en ejecución
echo ""
echo "📦 Deteniendo todos los contenedores de Docker..."
docker stop $(docker ps -aq) 2>/dev/null || echo "   ℹ️  No hay contenedores corriendo"

# 2. Eliminar todos los contenedores
echo ""
echo "🗑️  Eliminando todos los contenedores..."
docker rm $(docker ps -aq) 2>/dev/null || echo "   ℹ️  No hay contenedores para eliminar"

# 3. Eliminar todas las imágenes
echo ""
echo "🗑️  Eliminando todas las imágenes de Docker..."
docker rmi $(docker images -q) -f 2>/dev/null || echo "   ℹ️  No hay imágenes para eliminar"

# 4. Eliminar todos los volúmenes
echo ""
echo "🗑️  Eliminando todos los volúmenes..."
docker volume rm $(docker volume ls -q) 2>/dev/null || echo "   ℹ️  No hay volúmenes para eliminar"

# 5. Eliminar todas las redes personalizadas
echo ""
echo "🗑️  Eliminando redes personalizadas..."
docker network prune -f

# 6. Limpieza profunda del sistema Docker
echo ""
echo "🧹 Limpieza profunda del sistema Docker..."
docker system prune -a --volumes -f

# 7. Eliminar carpetas de proyecto antiguas (si existen)
echo ""
echo "🗑️  Eliminando carpetas de proyecto antiguas..."
cd ~
rm -rf Tingeso 2>/dev/null || echo "   ℹ️  No hay carpeta Tingeso antigua"
rm -rf proyecto 2>/dev/null || echo "   ℹ️  No hay carpeta proyecto antigua"

# 8. Verificar espacio en disco
echo ""
echo "📊 Espacio en disco después de la limpieza:"
df -h /

echo ""
echo "✅ Limpieza completada exitosamente!"
echo ""
echo "📋 Puedes verificar que todo está limpio con:"
echo "   - docker ps -a     (no debe mostrar contenedores)"
echo "   - docker images    (no debe mostrar imágenes)"
echo "   - docker volume ls (no debe mostrar volúmenes)"
