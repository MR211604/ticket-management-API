# Documentación: TicketTypeController

Controlador encargado de la lógica sobre los tipos de tickets (`TicketType`) y las compras correspondientes bajo eventos específicos.

## Rutas / Endpoints

### 1. Comprar un Ticket (Purchase Ticket)
- **Método**: `POST`
- **Ruta**: `/api/v1/events/{eventId}/ticket-types/{ticketTypeId}/tickets`
- **Descripción**: Permite a un usuario autenticado adquirir o comprar un ticket de un evento, seleccionando el tipo de ticket asociado.
- **Parámetros de Ruta**:
  - `eventId`: UUID del evento (notar que de momento el controlador extrae `ticketTypeId` mediante el path).
  - `ticketTypeId`: UUID de la categoría / tipo de ticket que se planea comprar.
- **Ejemplo de Petición (Request)**: 
*(Este Endpoint no recibe Request Body. La información va en la ruta y en el token de autenticación del usuario)*
```http
POST /api/v1/events/123e4567-e89b-12d3-a456-426614174000/ticket-types/3fa85f64-5717-4562-b3fc-2c963f66afa6/tickets HTTP/1.1
Authorization: Bearer <TUP_JWT_TOKEN>
```
- **Respuesta Exitosa**: `204 No Content`. Indica que la compra se procesó exitosamente sin retornar contenido en el payload.

