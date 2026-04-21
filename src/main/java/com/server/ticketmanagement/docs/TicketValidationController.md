# Documentación: TicketValidationController

Este controlador gestiona la validación o escaneo de tickets (entradas), permitiendo verificar si son válidos mediante entrada manual o escaneo de código QR.

## Rutas / Endpoints

### 1. Validar Ticket
- **Método**: `POST`
- **Ruta**: `/api/v1/ticket-validations`
- **Descripción**: Valida el estado de un ticket. Dependiendo del método de validación especificado (`MANUAL` o `QR_CODE`), el servicio ejecutará el tipo de verificación correspondiente sobre la base de datos.
- **Body de Petición (`TicketValidationRequestDto`)**:
  - `id`: UUID identificador del ticket o la validación.
  - `method`: Forma en que se validó. Valores admitidos: `MANUAL`, `QR_CODE`.
- **Ejemplo de Petición (Request Body)**:
```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "method": "QR_CODE"
}
```
- **Respuesta Exitosa**: `200 OK` devolviendo `TicketValidationResponseDto` con la resolución de la validación.

