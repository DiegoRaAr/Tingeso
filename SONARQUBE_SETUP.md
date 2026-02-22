# Análisis de Mantenibilidad con SonarQube

## 📋 Configuración del Sistema

Este proyecto implementa un análisis completo de mantenibilidad utilizando **SonarQube** para garantizar la calidad del código tanto en el Backend (Java/Spring Boot) como en el Frontend (React).

## 🎯 Objetivos de Calidad

### Backend (Java)
- ✅ **Technical Debt Ratio**: ≤ 1.0%
- ✅ **Maintainability Rating**: A
- ✅ **Code Smells** (High/Medium Impact): 0
- ✅ **Style Guide**: Google Java Style Guide

### Frontend (JavaScript/React)
- ✅ **Technical Debt Ratio**: ≤ 2.0%
- ✅ **Maintainability Rating**: A
- ✅ **Code Smells** (High/Medium Impact): 0
- ✅ **Style Guide**: Airbnb JavaScript Style Guide

## 🛠️ Prerequisitos

1. **SonarQube** corriendo en `http://localhost:9000`
   - Usuario por defecto: `admin`
   - Password por defecto: `admin`

2. **Java 17** o superior
3. **Maven 3.6+**
4. **Node.js 16+** y npm
5. **SonarScanner** para JavaScript

### Instalación de SonarScanner
```bash
npm install -g sonar-scanner
```

## 📁 Archivos de Configuración

### Backend
- `backend_tingeso/pom.xml` - Configuración de Maven con plugins
- `backend_tingeso/checkstyle.xml` - Reglas de Google Java Style Guide
- Plugins incluidos:
  - `maven-checkstyle-plugin` - Análisis de estilo de código
  - `jacoco-maven-plugin` - Cobertura de código
  - `sonar-maven-plugin` - Integración con SonarQube

### Frontend
- `frontend-tingeso/.eslintrc.json` - Configuración de ESLint con Airbnb
- `frontend-tingeso/sonar-project.properties` - Configuración de SonarQube
- `frontend-tingeso/package.json` - Scripts de análisis

## 🚀 Ejecución del Análisis

### Opción 1: Análisis Completo (Recomendado)
```bash
./sonar-analyze.sh
```

### Opción 2: Análisis Individual

#### Backend
```bash
./sonar-backend.sh
```

#### Frontend
```bash
./sonar-frontend.sh
```

### Opción 3: Manual

#### Backend Manual
```bash
cd backend_tingeso
mvn clean test jacoco:report
mvn checkstyle:checkstyle
mvn sonar:sonar
```

#### Frontend Manual
```bash
cd frontend-tingeso
npm install
npm run lint:report
sonar-scanner
```

## 📊 Visualización de Resultados

1. Accede a SonarQube: `http://localhost:9000`
2. Busca los proyectos:
   - `backend-tingeso`
   - `frontend-tingeso`
3. Revisa las métricas en el dashboard

## 🔍 Métricas Clave a Verificar

### En el Dashboard de SonarQube:

#### 1. Maintainability (Mantenibilidad)
- **Technical Debt Ratio**: Porcentaje de tiempo necesario para corregir problemas
- **Maintainability Rating**: Clasificación de A a E
- **Code Smells**: Problemas de mantenibilidad en el código

#### 2. Reliability (Confiabilidad)
- **Bugs**: Errores que pueden afectar el comportamiento
- **Reliability Rating**: Clasificación de A a E

#### 3. Security (Seguridad)
- **Vulnerabilities**: Problemas de seguridad
- **Security Rating**: Clasificación de A a E

#### 4. Coverage (Cobertura)
- **Coverage**: Porcentaje de código cubierto por tests
- **Line Coverage**: Líneas de código probadas

#### 5. Duplications (Duplicaciones)
- **Duplicated Lines**: Porcentaje de líneas duplicadas
- **Duplicated Blocks**: Bloques de código duplicados

## 📈 Quality Gates Configurados

### Backend Quality Gate
```yaml
Conditions:
  - Technical Debt Ratio ≤ 1.0%
  - Maintainability Rating = A
  - Blocker Issues = 0
  - Critical Issues = 0
  - Code Smells (High Severity) = 0
  - Code Smells (Medium Severity) = 0
```

### Frontend Quality Gate
```yaml
Conditions:
  - Technical Debt Ratio ≤ 2.0%
  - Maintainability Rating = A
  - Blocker Issues = 0
  - Critical Issues = 0
  - Code Smells (High Severity) = 0
  - Code Smells (Medium Severity) = 0
```

## 🔧 Configuración de Quality Gates en SonarQube

1. Accede a **Administration → Quality Gates**
2. Crea un nuevo Quality Gate: "Tingeso Backend"
3. Agrega las siguientes condiciones:
   - Technical Debt Ratio is greater than 1.0%
   - Maintainability Rating is worse than A
   - Blocker Issues is greater than 0
   - Critical Issues is greater than 0

4. Crea otro Quality Gate: "Tingeso Frontend"
5. Agrega condiciones similares pero con Technical Debt Ratio ≤ 2.0%

6. Asocia cada Quality Gate a su proyecto respectivo:
   - Project Settings → Quality Gate

## 🔄 Integración con Jenkins

El archivo `Jenkinsfile` incluye stages para:

1. **Checkout**: Obtiene el código del repositorio
2. **Backend Tests & Coverage**: Ejecuta tests con JaCoCo
3. **Backend Checkstyle**: Verifica Google Java Style Guide
4. **Backend SonarQube Analysis**: Analiza el código
5. **Backend Quality Gate**: Valida los criterios de calidad
6. **Frontend ESLint**: Verifica Airbnb Style Guide
7. **Frontend SonarQube Analysis**: Analiza el código
8. **Frontend Quality Gate**: Valida los criterios de calidad
9. **Build**: Construye los artefactos si todo es correcto

## 📝 Interpretación de Resultados

### Technical Debt Ratio
- **Bueno**: < 1% (Backend), < 2% (Frontend)
- **Aceptable**: 1-5%
- **Malo**: > 5%

### Maintainability Rating
- **A**: Technical Debt Ratio ≤ 5%
- **B**: 6-10%
- **C**: 11-20%
- **D**: 21-50%
- **E**: > 50%

### Code Smells Impact Severity
- **High**: Problemas que afectan significativamente la mantenibilidad
- **Medium**: Problemas que pueden dificultar el mantenimiento
- **Low**: Problemas menores de estilo

## 🐛 Solución de Problemas

### Error: "sonar-scanner: command not found"
```bash
npm install -g sonar-scanner
```

### Error: "Unable to connect to SonarQube"
- Verifica que SonarQube esté corriendo: `http://localhost:9000`
- Revisa el archivo de configuración `sonar-project.properties`

### Error: "Quality Gate failed"
- Revisa el dashboard de SonarQube
- Identifica los Code Smells y errores
- Corrige el código según las recomendaciones
- Vuelve a ejecutar el análisis

## 📚 Recursos

- [SonarQube Documentation](https://docs.sonarqube.org/)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [Airbnb JavaScript Style Guide](https://github.com/airbnb/javascript)
- [Checkstyle Documentation](https://checkstyle.org/)
- [ESLint Documentation](https://eslint.org/)

## ✅ Checklist de Verificación

- [ ] SonarQube corriendo en localhost:9000
- [ ] Proyectos creados en SonarQube (backend-tingeso, frontend-tingeso)
- [ ] Quality Gates configurados
- [ ] Scripts de análisis ejecutables (chmod +x)
- [ ] Dependencias instaladas (Maven, npm, sonar-scanner)
- [ ] Análisis ejecutado exitosamente
- [ ] Métricas revisadas y documentadas
- [ ] Quality Gates pasados
- [ ] Interpretación de resultados documentada

---

**Fecha de configuración**: Febrero 2026
**Versión**: 1.0
