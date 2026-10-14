# EncuentraTuChollo.com - Fullstack Travel Platform

Plataforma fullstack para una agencia de viajes moderna: catálogo de destinos, hoteles con enlaces reales de reserva, paneles Admin/SuperAdmin, autenticación JWT, carga de imágenes, PostgreSQL y buscador inteligente con fallback local.

> Proyecto creado como demo profesional de portfolio. La aplicación no procesa pagos dentro de la plataforma: las reservas se completan mediante enlaces externos reales, principalmente Booking.

## Demo

- Live demo: `añade aqui la URL cuando la despliegues`
- API docs local: `http://localhost:8080/swagger-ui/index.html`
- Frontend local: `http://localhost:3001`
- Backend local: `http://localhost:8080`

### Demo Credentials

Usa estas credenciales solo en un entorno demo/resetable, nunca en producción real.

| Role | Email | Password | Access |
| --- | --- | --- | --- |
| Admin | `admin@encuentratuchollo.com` | `Admin123!` | Destinos, hoteles y usuarios sin privilegios SuperAdmin |
| SuperAdmin | `superadmin@encuentratuchollo.com` | `SuperAdmin123!` | Gestión completa con reglas protegidas |

## Highlights

- Vue 3 + Vuetify responsive UI, adaptada a móvil, tablet y escritorio.
- Kotlin + Spring Boot REST API.
- PostgreSQL como base de datos principal.
- JWT authentication con roles `USER`, `ADMIN` y `SUPER_ADMIN`.
- Panel Admin para gestionar destinos, hoteles e imágenes.
- Panel SuperAdmin con permisos avanzados y protección de cuentas críticas.
- Carga real de imágenes vía multipart upload.
- Enlaces externos de reserva hacia Booking.
- Buscador inteligente con OpenAI y fallback local si la API no está disponible.
- Paginación, caché frontend y endpoints optimizados.
- Reglas de negocio protegidas también en backend, no solo en la UI.

## Booking And Reservations

La web muestra hoteles y destinos reales o curados por el administrador. Cada hotel puede tener un `externalUrl` que apunta a Booking u otro proveedor externo.

El flujo es:

1. El usuario explora destinos y hoteles dentro de EncuentraTuChollo.com.
2. Pulsa **Acceder** en un hotel.
3. La app abre el enlace real externo de reserva.
4. La reserva, disponibilidad, pago y confirmación final se realizan en Booking o en el proveedor configurado.

Esto mantiene la demo honesta y profesional: la plataforma actúa como escaparate, recomendador y panel de gestión, mientras que la transacción queda delegada en un proveedor especializado.

## Screenshots

Cuando publiques el repo, añade capturas en `docs/screenshots/` y enlázalas aquí:

- Home responsive.
- Catálogo de destinos.
- Hoteles por destino.
- Detalle de hotel con enlace externo.
- Login y registro.
- Panel Admin.
- Panel SuperAdmin.
- Buscador IA.

## Architecture

```text
Frontend: Vue 3 + Vuetify + Vite
        |
        | Axios + JWT
        v
Backend: Kotlin + Spring Boot + Spring Security
        |
        | Spring Data JPA
        v
Database: PostgreSQL

External services:
- OpenAI API for intelligent search, with local fallback
- Booking/external URLs for final reservations
- Wikimedia/Wikipedia image fallback for destination images
```

## Tech Stack

### Frontend

- Vue 3
- Vuetify
- Vite
- TypeScript
- Axios
- Typed routes

### Backend

- Kotlin
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- OpenAPI/Swagger
- JUnit 5

## Main Features

### Public/User Flow

- Responsive homepage.
- Browse destinations grouped by country.
- View hotels for a destination.
- Open real external booking links.
- Login/register.
- AI-powered search with fallback.

### Admin Flow

- Create, update and delete destinations.
- Create, update and delete hotels.
- Upload and persist images.
- Manage regular users.
- Cannot edit `SUPER_ADMIN` users.
- Cannot create or assign `SUPER_ADMIN` role.
- Cannot delete users.

### SuperAdmin Flow

- Full admin access.
- Create and assign roles.
- Manage users with protected rules.
- Cannot delete `SUPER_ADMIN` accounts.
- Cannot delete their own account.

## Security Notes

- JWT-based authentication.
- Backend enforces role permissions. The UI only hides actions for better UX; security does not rely on frontend checks.
- Uploaded files are validated as images before being persisted.
- `OPENAI_API_KEY`, `JWT_SECRET` and database credentials must be provided through environment variables.
- Do not publish your real `.env` file.

## Getting Started

### Requirements

- Java 17+
- Node.js 20+
- PostgreSQL
- Gradle wrapper included in the repository

### Backend Setup

Create a local environment file from the example:

```bash
cp .env.example .env
```

Configure at least:

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/agencia_db
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=change-me
JWT_SECRET=change-me-base64-64-bytes
OPENAI_API_KEY=sk-...
CORS_ALLOWED_ORIGINS=http://localhost:3001
FILE_STORAGE_PATH=./storage
```

Run the backend:

```bash
./gradlew bootRun
```

### Frontend Setup

```bash
cd client/agencia-viajes-client
npm install
npm run dev -- --host
```

## Testing

### Backend Tests

Run the complete backend test suite:

```bash
./gradlew test
```

Run backend build without tests:

```bash
./gradlew build -x test
```

The current minimum tests cover important business rules:

- Duplicated usernames are rejected.
- User data is normalized and passwords are encoded before saving.
- `SUPER_ADMIN` accounts cannot be deleted.

### Frontend Build Check

```bash
cd client/agencia-viajes-client
npm run build
```

This runs TypeScript checks and produces the production build.

## Useful API Endpoints

### Auth

- `POST /auth/login`
- `POST /auth/register`

### Destinations

- `GET /destinations`
- `GET /destinations?page=0&size=50`
- `GET /destinations/slug/{slug}`
- `POST /destinations`
- `PUT /destinations/{id}`
- `DELETE /destinations/{id}`

### Hotels

- `GET /hotels`
- `GET /hotels?page=0&size=50`
- `GET /destination/{id}/hotels`
- `POST /hotels`
- `PUT /hotels/{id}`
- `DELETE /hotels/{id}`

### Users

- `GET /users`
- `GET /users?page=0&size=50`
- `POST /users`
- `PUT /users/{id}`
- `DELETE /users/{id}`

### Uploads

- `POST /uploads/images`
- Public image access through `/storage/**`

### AI Search

- `GET /recommendations/search?q=playa barata`

## Deployment Notes

Recommended production setup:

- Backend deployed as a Spring Boot service.
- PostgreSQL managed database.
- Frontend deployed as static assets behind a CDN or web server.
- Storage path mounted as persistent volume for uploaded images.
- Environment variables configured in the hosting provider.
- Demo database reset periodically if you expose Admin/SuperAdmin credentials publicly.

## Portfolio Summary

This project demonstrates:

- Fullstack architecture.
- REST API design.
- Authentication and authorization.
- Role-based access control.
- Real database persistence.
- File uploads.
- External provider handoff for bookings.
- AI integration with graceful fallback.
- Responsive frontend implementation.
- Business-rule testing.

## Disclaimer

This is a portfolio/demo application. External booking links may point to real third-party booking pages, but availability, prices, payments and final reservations are handled outside this application by the corresponding provider.
