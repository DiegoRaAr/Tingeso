# 🚀 Quick Start - Deployment en EC2

## Inicio Rápido (3 Pasos)

### 1️⃣ Conectarse a EC2
```bash
ssh -i tu-clave.pem ec2-user@tu-ip-ec2
```

### 2️⃣ Instalar dependencias (primera vez)
```bash
curl -o setup.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-setup.sh
chmod +x setup.sh
./setup.sh

# Cerrar sesión y volver a entrar
exit
ssh -i tu-clave.pem ec2-user@tu-ip-ec2
```

### 3️⃣ Desplegar aplicación
```bash
curl -o deploy.sh https://raw.githubusercontent.com/DiegoRaAr/Tingeso/main/ec2-full-deploy.sh
chmod +x deploy.sh
./deploy.sh
```

## ✅ Listo!

Tu aplicación estará disponible en: `http://TU-IP-EC2:70`

---

## 📚 Documentación Completa

Para instrucciones detalladas, troubleshooting y comandos útiles, consulta:
- **[DEPLOYMENT-GUIDE.md](DEPLOYMENT-GUIDE.md)** - Guía completa paso a paso

---

## 🛠️ Scripts Disponibles

| Script | Descripción |
|--------|-------------|
| `ec2-cleanup.sh` | Limpia completamente la EC2 (borra todo) |
| `ec2-setup.sh` | Instala Docker, Docker Compose y Git |
| `ec2-deploy.sh` | Despliega la aplicación |
| `ec2-full-deploy.sh` | Script todo-en-uno (recomendado) |

---

## ⚙️ Configuración del Security Group

**Puertos que debes abrir en AWS:**

| Puerto | Descripción |
|--------|-------------|
| 22 | SSH |
| 70 | Aplicación web |
| 8080 | Keycloak (opcional) |

---

## 🔑 Credenciales por Defecto

**Keycloak Admin:**
- URL: `http://tu-ip:70/auth`
- Usuario: `admin`
- Contraseña: `admin`

**MySQL (solo interno):**
- Usuario: `diego`
- Contraseña: `diego1234`
- Base de datos: `db_tingeso`

---

## 📊 Comandos Útiles

```bash
# Ver logs en tiempo real
cd ~/Tingeso
docker-compose logs -f

# Ver estado de contenedores
docker-compose ps

# Reiniciar la aplicación
docker-compose restart

# Detener la aplicación
docker-compose down

# Actualizar con nuevos cambios
cd ~/Tingeso
git pull
docker-compose pull
docker-compose up -d
```

---

## 🆘 ¿Problemas?

1. Verifica que los puertos estén abiertos en el Security Group
2. Revisa los logs: `docker-compose logs -f`
3. Consulta [DEPLOYMENT-GUIDE.md](DEPLOYMENT-GUIDE.md) sección Troubleshooting

---

## 📝 Arquitectura

```
Internet → [Puerto 70] → Nginx Load Balancer
                            ↓
                    Backend Cluster (3 instancias)
                            ↓
                         MySQL
                            
Frontend + Keycloak
```

---

**Creado por:** Diego Ramírez  
**Repositorio:** https://github.com/DiegoRaAr/Tingeso
