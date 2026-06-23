# SiReSe — Sistema de Registro de Aspirantes
## UPIIZ-IPN | Tecnologías para Web 2026

---

## Estructura del Proyecto

```
sirese/
├── registro.html          ← Pág. pública de registro de aspirantes
├── login.html             ← Login del administrador (solo admin)
├── index.html             ← Dashboard admin
├── carreras.html          ← CRUD de Carreras (DataTable + AJAX)
├── aspirantes.html        ← Gestión de Aspirantes (DataTable + AJAX)
├── constancias.html       ← Historial de constancias PDF
├── js/
│   └── sirese-layout.js   ← Sidebar/navbar compartido + session guard
└── assets/                ← Plantilla adminHMD (CSS, JS, Bootstrap)

sirese-backend/
├── pom.xml
├── schema.sql             ← Diseño de la BD MySQL
└── src/main/java/mx/ipn/upiiz/sirese/
    ├── SireseApplication.java
    ├── entities/           ← Carrera, Aspirante, Administrador
    ├── repositories/       ← JPA Repositories
    ├── controllers/        ← REST Controllers (AJAX endpoints)
    └── resources/
        └── application.properties
```

---

## Endpoints REST (Spring Boot)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/carreras` | Listar carreras |
| GET | `/api/carreras/{id}` | Obtener carrera |
| POST | `/api/carreras` | Crear carrera |
| PUT | `/api/carreras/{id}` | Actualizar carrera |
| DELETE | `/api/carreras/{id}` | Eliminar carrera |
| GET | `/api/aspirantes` | Listar aspirantes |
| GET | `/api/aspirantes/check-email?email=` | Validar email (AJAX) |
| POST | `/api/aspirantes/registrar` | Registro público |
| GET | `/api/aspirantes/{id}` | Ver aspirante |
| GET | `/api/aspirantes/{id}/constancia.pdf` | Generar PDF |
| POST | `/api/auth/login` | Login admin |
| POST | `/api/correo/individual` | Correo individual |
| POST | `/api/correo/masivo` | Correo masivo |
| GET | `/api/dashboard/stats` | Stats del dashboard |

---

## Configuración de la Base de Datos

1. Crear BD en MySQL:
```sql
-- Ejecutar schema.sql
source schema.sql;
```

2. Editar `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sirese_db
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

---

## Configuración de Correo (Gmail)

1. Activar verificación en 2 pasos en Gmail
2. Generar "App Password" en Seguridad de Google
3. Configurar en `application.properties`:
```properties
spring.mail.username=tu_correo@gmail.com
spring.mail.password=xxxx xxxx xxxx xxxx   # App Password
```

---

## Ejecutar localmente

```bash
# Backend Spring Boot
cd sirese-backend
mvn spring-boot:run

# Frontend: abrir en navegador
# abrir sirese/registro.html  (página pública)
# abrir sirese/login.html     (admin: usuario=admin, pass=admin en modo demo)
```

---

## Despliegue en Render

### Backend (Spring Boot)
1. Subir el proyecto a GitHub
2. En Render: **New → Web Service**
   - Runtime: **Java**
   - Build Command: `mvn clean package -DskipTests`
   - Start Command: `java -jar target/sirese-1.0.0.jar`
3. Variables de entorno en Render:
   ```
   SPRING_DATASOURCE_URL=jdbc:mysql://...
   SPRING_DATASOURCE_USERNAME=...
   SPRING_DATASOURCE_PASSWORD=...
   SPRING_MAIL_USERNAME=...
   SPRING_MAIL_PASSWORD=...
   ```

### Base de datos en Render
- Render ofrece MySQL gratuito o usar **Railway / PlanetScale**
- Exportar schema.sql y ejecutarlo en la BD de producción

### Frontend (HTML estático)
- En Render: **New → Static Site**
- Directorio: `sirese/`
- Actualizar `API_BASE` en cada HTML:
  ```javascript
  const API_BASE = 'https://tu-backend.onrender.com/api';
  ```

### GitHub
```bash
git init
git add .
git commit -m "SiReSe - Proyecto Final Tecnologías Web 2026"
git remote add origin https://github.com/tu-usuario/sirese.git
git push -u origin main
```

---

## Tecnologías Utilizadas

| Capa | Tecnología |
|------|-----------|
| Frontend | HTML5, Bootstrap 5, adminHMD Template |
| Interactividad | JavaScript vanilla, AJAX (Fetch API) |
| Tablas | DataTables 1.13 con botones CSV/Excel |
| Backend | Spring Boot 3.2, Spring Data JPA |
| Base de datos | MySQL 8 |
| PDF | OpenPDF (Java) |
| Correo | JavaMail / Spring Mail (Gmail SMTP) |
| Despliegue | Render (backend + static) + GitHub |
