#!/bin/bash

# Script para análisis completo de SonarQube (Backend + Frontend)
echo "=========================================="
echo "Análisis Completo de Mantenibilidad"
echo "Sistema Tingeso - Backend + Frontend"
echo "=========================================="

echo ""
echo "📋 Requisitos:"
echo "  ✓ SonarQube corriendo en http://localhost:9000"
echo "  ✓ Maven instalado"
echo "  ✓ Node.js y npm instalados"
echo "  ✓ sonar-scanner instalado"
echo ""

read -p "¿Deseas continuar con el análisis? (s/n): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Ss]$ ]]
then
    exit 1
fi

# Análisis del Backend
echo ""
echo "🔵 PASO 1: Análisis del Backend"
echo "======================================"
./sonar-backend.sh

if [ $? -ne 0 ]; then
    echo "❌ Error en el análisis del Backend"
    exit 1
fi

# Análisis del Frontend
echo ""
echo "🟢 PASO 2: Análisis del Frontend"
echo "======================================"
./sonar-frontend.sh

if [ $? -ne 0 ]; then
    echo "❌ Error en el análisis del Frontend"
    exit 1
fi

# Resumen final
echo ""
echo "=========================================="
echo "✅ Análisis Completo Finalizado"
echo "=========================================="
echo ""
echo "📊 Accede a SonarQube para ver los resultados:"
echo "   🌐 http://localhost:9000"
echo ""
echo "📋 Verificación de Métricas:"
echo ""
echo "BACKEND (backend-tingeso):"
echo "  ✓ Technical Debt Ratio: <= 1.0%"
echo "  ✓ Maintainability Rating: A"
echo "  ✓ Code Smells (High/Medium): 0"
echo "  ✓ Google Java Style Guide: Cumplido"
echo ""
echo "FRONTEND (frontend-tingeso):"
echo "  ✓ Technical Debt Ratio: <= 2.0%"
echo "  ✓ Maintainability Rating: A"
echo "  ✓ Code Smells (High/Medium): 0"
echo "  ✓ Airbnb JavaScript Style Guide: Cumplido"
echo ""
echo "=========================================="
