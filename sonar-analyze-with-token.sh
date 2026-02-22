#!/bin/bash

# Script para análisis de SonarQube con tokens
echo "=========================================="
echo "Análisis Completo de Mantenibilidad"
echo "Sistema Tingeso - Backend + Frontend"
echo "=========================================="

# Solicitar tokens
echo ""
echo "📝 Necesitas los tokens de SonarQube"
echo ""
read -p "Token del Backend (de SonarQube): " BACKEND_TOKEN
echo ""
read -p "Token del Frontend (de SonarQube): " FRONTEND_TOKEN

if [ -z "$BACKEND_TOKEN" ] || [ -z "$FRONTEND_TOKEN" ]; then
    echo "❌ Error: Debes proporcionar ambos tokens"
    exit 1
fi

# Análisis del Backend
echo ""
echo "🔵 PASO 1: Análisis del Backend"
echo "======================================"
cd backend_tingeso || exit 1

echo "🧹 Limpiando y compilando proyecto..."
mvn clean compile

echo "🧪 Ejecutando tests con JaCoCo..."
mvn test jacoco:report

echo "✅ Ejecutando Checkstyle..."
mvn checkstyle:checkstyle

echo "🔍 Ejecutando análisis de SonarQube..."
mvn sonar:sonar \
  -Dsonar.projectKey=backend-tingeso \
  -Dsonar.projectName="Backend Tingeso" \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=$BACKEND_TOKEN

if [ $? -ne 0 ]; then
    echo "❌ Error en el análisis del Backend"
    exit 1
fi

cd ..

# Análisis del Frontend
echo ""
echo "🟢 PASO 2: Análisis del Frontend"
echo "======================================"
cd frontend-tingeso || exit 1

echo "📦 Instalando dependencias si es necesario..."
if [ ! -d "node_modules" ]; then
    npm install
fi

echo "🧹 Ejecutando ESLint..."
npm run lint || true

echo "📝 Generando reporte de ESLint..."
npm run lint:report || true

echo "🔍 Ejecutando análisis de SonarQube..."

# Verificar si sonar-scanner está instalado
if ! command -v sonar-scanner &> /dev/null; then
    echo "⚠️  sonar-scanner no está instalado"
    echo "Instalando globalmente..."
    npm install -g sonar-scanner
fi

sonar-scanner \
  -Dsonar.projectKey=frontend-tingeso \
  -Dsonar.projectName="Frontend Tingeso" \
  -Dsonar.sources=src \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=$FRONTEND_TOKEN

if [ $? -ne 0 ]; then
    echo "❌ Error en el análisis del Frontend"
    exit 1
fi

cd ..

# Resumen final
echo ""
echo "=========================================="
echo "✅ Análisis Completo Finalizado"
echo "=========================================="
echo ""
echo "📊 Accede a SonarQube para ver los resultados:"
echo "   🌐 http://localhost:9000"
echo ""
echo "📋 Proyectos analizados:"
echo "   🔵 Backend:  http://localhost:9000/dashboard?id=backend-tingeso"
echo "   🟢 Frontend: http://localhost:9000/dashboard?id=frontend-tingeso"
echo ""
echo "=========================================="
