# Documentación: TicketController

Controlador destinado a los usuarios finales para que puedan listar y visualizar los tickets que han adquirido o de los cuales son dueños, y acceder a sus códigos QR.

## Rutas / Endpoints

### 1. Listar Entradas del Usuario
- **Método**: `GET`
- **Ruta**: `/api/v1/tickets`
- **Descripción**: Obtiene y lista todos los tickets que el usuario autenticado posee.
- **Parámetros**: 
  - `pageable` (Paginación: `page`, `size`, etc.)
- **Respuesta Exitosa**: `200 OK` devolviendo la página de tickets (`Page<ListTicketResponseDto>`).

### 2. Obtener Detalle de una Entrada
- **Método**: `GET`
- **Ruta**: `/api/v1/tickets/{ticketId}`
- **Descripción**: Permite visualizar la información detallada de un ticket específico del que el usuario es propietario.
- **Respuesta Exitosa**: `200 OK` (`GetTicketResponseDto`).
- **Respuesta de Error**: `404 Not Found` si el ticket no existe o no le pertenece al usuario.

### 3. Obtener Código QR de la Entrada
- **Método**: `GET`
- **Ruta**: `/api/v1/tickets/{ticketId}/qr-codes`
- **Descripción**: Genera o descarga la imagen del código QR asociada al ticket especificado del usuario, usualmente utilizada en los accesos.
- **Respuesta Exitosa**: `200 OK` y descarga una imagen en formato `image/png` (el arreglo de bytes representativo del archivo de imagen).

