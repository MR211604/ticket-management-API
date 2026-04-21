# Documentación: PublishedEventsController

Controlador público (generalmente) encargado de mostrar a los usuarios los eventos publicados (es decir, el catálogo que el público puede ver y comprar).

## Rutas / Endpoints

### 1. Listar Eventos Publicados
- **Método**: `GET`
- **Ruta**: `/api/v1/published-events`
- **Descripción**: Retorna los eventos publicados y visibles para el público. Si se incluye el query param `q`, realiza una búsqueda; de lo contrario lista con normalidad.
- **Parámetros**:
  - `pageable`: Paginación (`page`, `size`, `sort`).
  - `q` *(Opcional)*: Término de búsqueda (ejemplo: `?q=concierto`).
- **Respuesta Exitosa**: `200 OK` devolviendo un objeto paginado `Page<ListPublishedEventResponseDto>`.

### 2. Detalles de Evento Publicado
- **Método**: `GET`
- **Ruta**: `/api/v1/published-events/{eventId}`
- **Descripción**: Retorna la información completa de un evento publicado específico que está disponible para la consulta del público.
- **Parámetros de Ruta**:
  - `eventId`: UUID del evento.
- **Respuesta Exitosa**: `200 OK` (`GetPublishedEventDetailsResponseDto`).
- **Respuesta de Error**: `404 Not Found` si el evento no existe o no está publicado.

