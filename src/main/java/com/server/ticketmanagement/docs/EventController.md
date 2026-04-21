# Documentación: EventController

Este controlador maneja las operaciones CRUD para la gestión de Eventos (`Event`) por parte de los organizadores. Requiere que el usuario esté autenticado y se extrae su identificador a través del token JWT en cada petición.

## Rutas / Endpoints

### 1. Obtener listado de Eventos
- **Método**: `GET`
- **Ruta**: `/api/v1/events`
- **Descripción**: Lista los eventos creados por el organizador autenticado de forma paginada.
- **Parámetros**: Acepta parámetros de paginación (`page`, `size`, `sort`).
- **Respuesta Exitosa**: `200 OK` retornando una lista paginada (`Page<ListEventResponseDto>`).

### 2. Obtener detalles de un Evento
- **Método**: `GET`
- **Ruta**: `/api/v1/events/{eventId}`
- **Descripción**: Obtiene los detalles de un evento específico perteneciente al organizador autenticado.
- **Respuesta Exitosa**: `200 OK` (retorna `GetEventDetailsResponseDto`).
- **Respuesta de Error**: `404 Not Found` si no existe o no le pertenece.

### 3. Crear un Evento
- **Método**: `POST`
- **Ruta**: `/api/v1/events`
- **Descripción**: Crea un nuevo evento asignado al organizador autenticado.
- **Ejemplo de Petición (Request Body)**:
```json
{
  "name": "Concierto de Rock",
  "start": "2026-10-15T20:00:00",
  "end": "2026-10-15T23:00:00",
  "venue": "Estadio Nacional",
  "salesStart": "2026-05-01T10:00:00",
  "salesEnd": "2026-10-15T18:00:00",
  "status": "PUBLISHED",
  "ticketTypes": [
    {
      "name": "General",
      "price": 50.0,
      "description": "Entrada general de pie",
      "totalAvailable": 1000
    },
    {
      "name": "VIP",
      "price": 120.0,
      "description": "Ubicación en primera fila",
      "totalAvailable": 200
    }
  ]
}
```
- **Respuesta Exitosa**: `201 Created` retornando el DTO del evento creado (`CreateEventResponseDto`).

### 4. Actualizar un Evento
- **Método**: `PUT`
- **Ruta**: `/api/v1/events/{eventId}`
- **Descripción**: Actualiza los detalles de un evento existente que pertenezca al organizador.
- **Ejemplo de Petición (Request Body)** (Se debe enviar con un DTO de actualización que incluya el ID en el body y los ID de los ticketTypes):
```json
{
  "id": "11111111-2222-3333-4444-555555555555",
  "name": "Concierto de Rock Editado",
  "start": "2026-10-15T20:00:00",
  "end": "2026-10-15T23:00:00",
  "venue": "Estadio Internacional",
  "salesStart": "2026-05-01T10:00:00",
  "salesEnd": "2026-10-15T18:00:00",
  "status": "PUBLISHED",
  "ticketTypes": [
    {
      "id": "12345678-1234-1234-1234-123456789012",
      "name": "General",
      "price": 55.0,
      "description": "Entrada general de pie actualizada",
      "totalAvailable": 950
    }
  ]
}
```
- **Respuesta Exitosa**: `200 OK` (retorna `UpdateEventResponseDto`).

### 5. Eliminar un Evento
- **Método**: `DELETE`
- **Ruta**: `/api/v1/events/{eventId}`
- **Descripción**: Elimina un evento existente del organizador.
- **Respuesta Exitosa**: `204 No Content`.
