# 🚀 Guía Paso a Paso: Análisis de Mantenibilidad con SonarQube

## 📌 Paso 1: Verificar que SonarQube esté corriendo

```bash
# Verifica que SonarQube esté activo
curl http://localhost:9000

# O abre en tu navegador:
# http://localhost:9000
```

**Credenciales por defecto:**
- Usuario: `admin`
- Contraseña: `admin`

---

## 📌 Paso 2: Configurar Proyectos en SonarQube (Primera vez)

### 2.1 Crear Proyecto Backend

1. Accede a **http://localhost:9000**
2. Click en **"Create Project"** → **"Manually"**
3. Configura:
   - **Project key**: `backend-tingeso`
   - **Display name**: `Backend Tingeso`
4. Click **"Set Up"**
5. Genera un **Token**:
   - Name: `backend-token`
   - Guarda el token generado
6. Click **"Continue"**
7. Selecciona **"Java"** y **"Maven"**

### 2.2 Crear Proyecto Frontend

1. Click en **"Create Project"** → **"Manually"**
2. Configura:
   - **Project key**: `frontend-tingeso`
   - **Display name**: `Frontend Tingeso`
3. Click **"Set Up"**
4. Genera un **Token**:
   - Name: `frontend-token`
   - Guarda el token generado
5. Click **"Continue"**
6. Selecciona **"Other"** (JavaScript)

### 2.3 Configurar Quality Gates

#### Backend Quality Gate:
1. Ve a **Administration** → **Quality Gates**
2. Click **"Create"**
3. Nombre: `Backend Tingeso QG`
4. Agrega condiciones (Click "Add Condition"):
   - **Technical Debt Ratio** → is greater than → `1.0`
   - **Maintainability Rating** → is worse than → `A`
   - **Blocker Issues** → is greater than → `0`
   - **Critical Issues** → is greater than → `0`
   - **Code Smells** → Severity: High → is greater than → `0`
   - **Code Smells** → Severity: Medium → is greater than → `0`
5. Click **"Set as Default"** (opcional)

#### Frontend Quality Gate:
1. Click **"Create"**
2. Nombre: `Frontend Tingeso QG`
3. Agrega condiciones:
   - **Technical Debt Ratio** → is greater than → `2.0`
   - **Maintainability Rating** → is worse than → `A`
   - **Blocker Issues** → is greater than → `0`
   - **Critical Issues** → is greater than → `0`
   - **Code Smells** → Severity: High → is greater than → `0`
   - **Code Smells** → Severity: Medium → is greater than → `0`

#### Asociar Quality Gates a Proyectos:
1. Ve a **Projects**
2. Click en **backend-tingeso**
3. Ve a **Project Settings** → **Quality Gate**
4. Selecciona: `Backend Tingeso QG`
5. Repite para **frontend-tingeso** con `Frontend Tingeso QG`

---

## 📌 Paso 3: Verificar Instalación de Herramientas

```bash
# Verificar Java
java -version
# Debe mostrar: java version "17" o superior

# Verificar Maven
mvn -version
# Debe mostrar: Apache Maven 3.6+ o superior

# Verificar Node.js
node -version
# Debe mostrar: v16+ o superior

# Verificar npm
npm -version

# Verificar sonar-scanner
sonar-scanner -v
# Si no está instalado:
npm install -g sonar-scanner
```

---

## 📌 Paso 4: Ejecutar Análisis Completo

### Opción A: Usando el Script Automático (MÁS FÁCIL) ⭐

```bash
# Desde el directorio raíz del proyecto
./sonar-analyze.sh
```

Este script ejecutará:
1. ✅ Análisis completo del Backend
2. ✅ Análisis completo del Frontend
3. ✅ Muestra un resumen de las métricas

### Opción B: Análisis Individual

#### Backend:
```bash
./sonar-backend.sh
```

#### Frontend:
```bash
./sonar-frontend.sh
```

---

## 📌 Paso 5: Revisar Resultados en SonarQube

### 5.1 Acceder al Dashboard

1. Abre **http://localhost:9000**
2. Verás los proyectos:
   - `backend-tingeso`
   - `frontend-tingeso`

### 5.2 Analizar Métricas del Backend

1. Click en **backend-tingeso**
2. Revisa el **Overview**:

   **Verificaciones críticas:**
   ```
   ✅ Technical Debt Ratio: <= 1.0%
   ✅ Maintainability Rating: A
   ✅ Reliability Rating: A (ideal)
   ✅ Security Rating: A (ideal)
   ✅ Code Smells (High): 0
   ✅ Code Smells (Medium): 0
   ✅ Bugs: 0 (ideal)
   ✅ Vulnerabilities: 0 (ideal)
   ```

3. Click en **"Code Smells"** para ver detalles
4. Click en **"Measures"** para ver métricas detalladas:
   - Complexity
   - Duplications
   - Size
   - Coverage

### 5.3 Analizar Métricas del Frontend

1. Click en **frontend-tingeso**
2. Revisa el **Overview**:

   **Verificaciones críticas:**
   ```
   ✅ Technical Debt Ratio: <= 2.0%
   ✅ Maintainability Rating: A
   ✅ Code Smells (High): 0
   ✅ Code Smells (Medium): 0
   ```

---

## 📌 Paso 6: Verificar Cumplimiento de Style Guides

### Backend - Google Java Style Guide

1. En SonarQube, ve a **backend-tingeso** → **Issues**
2. Filtra por **"Rule"**
3. Busca violaciones relacionadas con:
   - Indentación (4 espacios)
   - Longitud de línea (100 caracteres)
   - Nombres de variables (camelCase)
   - Nombres de constantes (UPPER_SNAKE_CASE)
   - Imports (sin wildcards)

**Reporte de Checkstyle:**
```bash
# Ver reporte HTML
open backend_tingeso/target/site/checkstyle.html
# O en Linux:
xdg-open backend_tingeso/target/site/checkstyle.html
```

### Frontend - Airbnb JavaScript Style Guide

1. En SonarQube, ve a **frontend-tingeso** → **Issues**
2. Verifica:
   - Indentación (2 espacios)
   - Uso de const/let (no var)
   - Comillas dobles
   - Semicolons
   - Arrow functions

**Reporte de ESLint:**
```bash
# Ver reporte JSON
cat frontend-tingeso/eslint-report.json
```

---

## 📌 Paso 7: Documentar Resultados

### 7.1 Capturar Screenshots

Toma capturas de pantalla de:

1. **Dashboard general** de cada proyecto
2. **Overview** mostrando métricas principales
3. **Maintainability** con Technical Debt Ratio
4. **Quality Gate** status (PASSED/FAILED)
5. **Issues** agrupados por severidad
6. **Code Smells** detallados
7. **Coverage** si aplicable

### 7.2 Crear Reporte de Análisis

Crea un documento con:

```markdown
# Reporte de Análisis de Mantenibilidad

## 1. Información General
- Fecha de análisis: [FECHA]
- Versión del proyecto: [VERSION]
- Analista: [NOMBRE]

## 2. Métricas Backend (backend-tingeso)

### 2.1 Métricas Principales
- **Technical Debt Ratio**: [X.X%] ✅/❌
- **Maintainability Rating**: [A-E] ✅/❌
- **Reliability Rating**: [A-E]
- **Security Rating**: [A-E]
- **Lines of Code**: [XXXX]
- **Code Smells**: [XX]
  - High: [X] ✅/❌
  - Medium: [X] ✅/❌
  - Low: [X]

### 2.2 Cumplimiento Google Java Style Guide
- Violaciones de estilo: [XX]
- Principales problemas encontrados:
  1. [Descripción]
  2. [Descripción]

### 2.3 Cobertura de Código
- **Coverage**: [XX%]
- **Unit Tests**: [XX] tests
- **Integration Tests**: [XX] tests

## 3. Métricas Frontend (frontend-tingeso)

### 3.1 Métricas Principales
- **Technical Debt Ratio**: [X.X%] ✅/❌
- **Maintainability Rating**: [A-E] ✅/❌
- **Lines of Code**: [XXXX]
- **Code Smells**: [XX]
  - High: [X] ✅/❌
  - Medium: [X] ✅/❌
  - Low: [X]

### 3.2 Cumplimiento Airbnb JavaScript Style Guide
- Violaciones de estilo: [XX]
- Principales problemas encontrados:
  1. [Descripción]
  2. [Descripción]

## 4. Análisis de Coherencia

### 4.1 Coherencia entre Métricas y Calidad
- ¿Las métricas reflejan la calidad estructural? [SÍ/NO]
- Justificación: [TEXTO]

### 4.2 Coherencia de Estilos de Codificación
- Backend: [CUMPLE/NO CUMPLE] Google Java Style Guide
- Frontend: [CUMPLE/NO CUMPLE] Airbnb JavaScript Style Guide

## 5. Conclusiones

### 5.1 Nivel de Mantenibilidad
El sistema presenta un nivel de mantenibilidad [ADECUADO/INADECUADO] porque:
- [Justificación basada en métricas]
- [Análisis de tendencias]

### 5.2 Debilidades Identificadas
1. [Debilidad 1]: [Descripción y evidencia]
2. [Debilidad 2]: [Descripción y evidencia]

### 5.3 Oportunidades de Mejora
1. [Mejora 1]: [Descripción y beneficio esperado]
2. [Mejora 2]: [Descripción y beneficio esperado]

## 6. Recomendaciones

### 6.1 Acciones Inmediatas
- [ ] [Acción prioritaria 1]
- [ ] [Acción prioritaria 2]

### 6.2 Mejoras a Mediano Plazo
- [ ] [Mejora 1]
- [ ] [Mejora 2]

## 7. Referencias
- [Screenshot 1]: Dashboard Backend
- [Screenshot 2]: Dashboard Frontend
- [Screenshot 3]: Quality Gates
```

---

## 📌 Paso 8: Interpretar Resultados

### ¿Qué significa cada métrica?

#### Technical Debt Ratio
- **Definición**: Tiempo estimado para arreglar Code Smells / Tiempo para reescribir el código
- **< 1% (Backend)**: Excelente mantenibilidad
- **< 2% (Frontend)**: Excelente mantenibilidad
- **5-10%**: Mantenibilidad aceptable
- **> 10%**: Problemas serios de mantenibilidad

#### Maintainability Rating
- **A**: 0-5% Technical Debt Ratio (Excelente)
- **B**: 6-10% (Bueno)
- **C**: 11-20% (Aceptable)
- **D**: 21-50% (Malo)
- **E**: > 50% (Muy malo)

#### Code Smells
- **High Severity**: Impacto significativo en mantenibilidad
- **Medium Severity**: Impacto moderado
- **Low Severity**: Impacto menor

### Ejemplos de Conclusiones

#### ✅ Sistema con Buena Mantenibilidad:
```
"El sistema presenta un nivel ADECUADO de mantenibilidad, evidenciado por:

1. Technical Debt Ratio del Backend: 0.8% (objetivo: ≤1.0%) ✅
2. Technical Debt Ratio del Frontend: 1.5% (objetivo: ≤2.0%) ✅
3. Maintainability Rating A en ambos componentes ✅
4. Ausencia de Code Smells de severidad High o Medium ✅
5. Cumplimiento del 95% de las reglas del Google Java Style Guide ✅
6. Cumplimiento del 98% de las reglas del Airbnb JavaScript Style Guide ✅

El código presenta una estructura clara, bien organizada y siguiendo
estándares de la industria, lo que facilita su mantenimiento y evolución."
```

#### ❌ Sistema con Problemas de Mantenibilidad:
```
"El sistema presenta un nivel INADECUADO de mantenibilidad, debido a:

1. Technical Debt Ratio del Backend: 3.5% (objetivo: ≤1.0%) ❌
2. Existen 15 Code Smells de severidad High ❌
3. Maintainability Rating C en el Backend ❌
4. 45 violaciones del Google Java Style Guide ❌

Debilidades identificadas:
- Métodos excesivamente largos (> 150 líneas)
- Alta duplicación de código (12%)
- Complejidad ciclomática elevada en servicios críticos

Oportunidades de mejora:
1. Refactorizar métodos largos en funciones más pequeñas
2. Eliminar código duplicado mediante abstracciones
3. Aplicar principios SOLID para reducir complejidad
4. Configurar pre-commit hooks para validar estilos"
```

---

## 📌 Paso 9: Corrección de Issues (Si aplica)

### Si el Quality Gate falla:

1. **Identificar problemas**:
   ```bash
   # En SonarQube, ve a Issues y filtra por:
   - Severity: High, Medium
   - Type: Code Smell
   ```

2. **Corregir código**:
   - Sigue las recomendaciones de SonarQube
   - Aplica las reglas del Style Guide correspondiente

3. **Volver a ejecutar análisis**:
   ```bash
   ./sonar-analyze.sh
   ```

4. **Verificar que Quality Gate pase**: ✅

---

## 📌 Paso 10: Integración Continua (Opcional)

### Configurar Jenkins

El `Jenkinsfile` ya incluye los stages de SonarQube.

**Configuración adicional en Jenkins:**

1. Instalar plugin **SonarQube Scanner**
2. Ir a **Manage Jenkins** → **Configure System**
3. Agregar **SonarQube Server**:
   - Name: `SonarQube`
   - Server URL: `http://localhost:9000`
   - Server authentication token: [Token generado]
4. Guardar configuración

**Ejecutar Pipeline:**
```bash
# En Jenkins, crear nuevo Pipeline
# Apuntar al Jenkinsfile del repositorio
# Ejecutar build
```

---

## 🎯 Checklist Final

- [ ] SonarQube corriendo en localhost:9000
- [ ] Proyectos creados (backend-tingeso, frontend-tingeso)
- [ ] Quality Gates configurados y asociados
- [ ] Todas las herramientas instaladas (Java, Maven, Node, sonar-scanner)
- [ ] Dependencias instaladas (backend y frontend)
- [ ] Análisis ejecutado exitosamente
- [ ] Backend: Technical Debt Ratio ≤ 1.0% ✅
- [ ] Backend: Maintainability Rating A ✅
- [ ] Backend: Code Smells (High/Medium) = 0 ✅
- [ ] Backend: Google Java Style Guide cumplido ✅
- [ ] Frontend: Technical Debt Ratio ≤ 2.0% ✅
- [ ] Frontend: Maintainability Rating A ✅
- [ ] Frontend: Code Smells (High/Medium) = 0 ✅
- [ ] Frontend: Airbnb Style Guide cumplido ✅
- [ ] Screenshots capturados
- [ ] Reporte de análisis documentado
- [ ] Interpretación de resultados realizada
- [ ] Conclusiones y recomendaciones documentadas

---

## 📞 Solución de Problemas Comunes

### Problema 1: "Unable to connect to SonarQube"
**Solución:**
```bash
# Verifica que SonarQube esté corriendo
curl http://localhost:9000

# Si no responde, inicia SonarQube:
cd /path/to/sonarqube/bin/linux-x86-64
./sonar.sh start
```

### Problema 2: "Quality Gate failed"
**Solución:**
- Revisa el dashboard de SonarQube
- Identifica los issues específicos
- Corrige el código
- Vuelve a ejecutar el análisis

### Problema 3: "sonar-scanner not found"
**Solución:**
```bash
npm install -g sonar-scanner
# O descarga desde: https://docs.sonarqube.org/latest/analysis/scan/sonarscanner/
```

### Problema 4: "Maven build failed"
**Solución:**
```bash
cd backend_tingeso
mvn clean install -DskipTests
# Revisa errores de compilación
```

### Problema 5: "ESLint errors"
**Solución:**
```bash
cd frontend-tingeso
npm run lint:fix
# Corrige los errores manualmente si persisten
```

---

**¡Listo!** Ahora tienes todo configurado para realizar el análisis de mantenibilidad. 🎉

**Próximos pasos sugeridos:**
1. Ejecuta `./sonar-analyze.sh`
2. Revisa los resultados en SonarQube
3. Documenta tus hallazgos
4. Presenta tu análisis

---

📝 **Documento creado:** Febrero 2026  
🔖 **Versión:** 1.0
