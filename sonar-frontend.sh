#!/bin/bash

# Script para análisis de SonarQube del Frontend
echo "======================================"
echo "Análisis SonarQube - Frontend Tingeso"
echo "======================================"

# Navegar al directorio del frontend
cd frontend-tingeso || exit 1

echo "📦 Instalando dependencias..."
npm install

echo "🧹 Ejecutando ESLint (Airbnb Style Guide)..."
npm run lint

echo "📝 Generando reporte de ESLint..."
npm run lint:report

echo "🔍 Ejecutando análisis de SonarQube..."
# Asegúrate de tener sonar-scanner instalado
# Puedes instalarlo con: npm install -g sonar-scanner

if ! command -v sonar-scanner &> /dev/null
then
    echo "⚠️  sonar-scanner no está instalado"
    echo "Instalando sonar-scanner..."
    npm install -g sonar-scanner
fi

sonar-scanner

echo ""
echo "✅ Análisis del Frontend completado!"
echo "📈 Verifica los resultados en: http://localhost:9000"
echo ""
echo "Métricas a verificar:"
echo "  - Technical Debt Ratio: debe ser <= 2.0%"
echo "  - Maintainability Rating: debe ser A"
echo "  - Code Smells (High/Medium): debe ser 0"
echo "  - Airbnb JavaScript Style Guide: debe cumplirse"
echo "======================================"
