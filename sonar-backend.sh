#!/bin/bash

# Script para análisis de SonarQube del Backend
echo "======================================"
echo "Análisis SonarQube - Backend Tingeso"
echo "======================================"

# Navegar al directorio del backend
cd backend_tingeso || exit 1

echo "🧹 Limpiando proyecto..."
mvn clean

echo "📦 Compilando proyecto..."
mvn compile

echo "🧪 Ejecutando tests con JaCoCo..."
mvn test

echo "✅ Ejecutando Checkstyle (Google Java Style Guide)..."
mvn checkstyle:check

echo "📊 Generando reportes..."
mvn checkstyle:checkstyle
mvn jacoco:report

echo "🔍 Ejecutando análisis de SonarQube..."
mvn sonar:sonar

echo ""
echo "✅ Análisis del Backend completado!"
echo "📈 Verifica los resultados en: http://localhost:9000"
echo ""
echo "Métricas a verificar:"
echo "  - Technical Debt Ratio: debe ser <= 1.0%"
echo "  - Maintainability Rating: debe ser A"
echo "  - Code Smells (High/Medium): debe ser 0"
echo "  - Google Java Style Guide: debe cumplirse"
echo "======================================"
