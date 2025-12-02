# 🩺 Turnera Médica – Sistema de Gestión de Turnos

Sistema web desarrollado en **Java + Spring Boot** para gestionar turnos médicos, agendas, usuarios y roles. Incluye autenticación JWT, panel administrador, módulo médico y módulo paciente.

---

## 🚀 Funcionalidades Principales

### 🔐 Autenticación y Roles
- Login con **JWT**.
- Roles: `ADMIN`, `MEDICO`, `PACIENTE`, `USUARIO`.
- Control de acceso por permisos.
- Carga automática de:
  - Permisos  
  - Roles  
  - Relaciones rol–permiso  
  - Usuario administrador inicial (`admin@gmail.com / 1234`)

### 🩺 Módulo Médico
- Registro de médicos (especialidad y matrícula).
- Gestión de agenda médica.
- Generación automática de turnos diarios.
- Listado y filtrado de turnos.
- Marcar turnos como realizados.

### 👤 Módulo Paciente
- Solicitar turnos disponibles.
- Ver historial y turnos asignados.

### 🛠️ Panel Administrador
- CRUD de usuarios.
- Asignación y eliminación de roles.
- Filtro de usuarios por rol.

---

## 🧰 Tecnologías Utilizadas

### Backend
- Java 17  
- Spring Boot 3  
- Spring Security  
- JWT  
- JPA / Hibernate  
- MySQL  

### Frontend (Thymeleaf)
- HTML5  
- Bootstrap 5  
- JavaScript  

---

## 📁 Estructura del Proyecto

```

turnera/
├── config/
├── dto/
├── exception/
├── entity/
├── controller/
├── repository/
├── service/
├── validation/
├── resources/
│   ├── static/
│   ├── templates/
│   └── application.properties
└── data.sql   <-- Inserta roles, permisos y admin solo una vez

````

---

## ⚙️ Instalación y Configuración

### 1️⃣ Clonar el repositorio
```bash
git clone https://github.com/alexrodriguezalejo/TurneraClinica
cd TurneraClinica
````

### 2️⃣ Crear base de datos en MySQL

```sql
CREATE DATABASE turnera_db;
```

### 3️⃣ Configurar `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/turnera_db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 4️⃣ Ejecutar el backend

```bash
mvn spring-boot:run
```

Spring Boot:

* crea las tablas
* ejecuta `data.sql`
* genera permisos, roles, relaciones
* crea el usuario admin

---

## 🔓 Usuario Administrador por defecto

```
email: admin@gmail.com
password: 1234
```

---

## 📦 Colección de Postman

Importar el archivo:

```
Turnera.postman_collection.json
```

Incluye:

* Login
* CRUD de usuarios
* CRUD de médicos
* Turnos
* Asignación de roles

---

## 🔒 Roles y Permisos

| Rol          | Acceso permitido          |
| ------------ | ------------------------- |
| **ADMIN**    | Gestión total del sistema |
| **MEDICO**   | Agenda y turnos propios   |
| **PACIENTE** | Solicitar y ver turnos    |
| **USUARIO**  | Acceso básico             |

---

## 👨‍💻 Autor

**Alexander Rodríguez**
Desarrollador Backend Java Jr.
📍 Argentina

---

## 📜 Licencia

Licencia MIT. Libre para uso académico y educativo.

```

