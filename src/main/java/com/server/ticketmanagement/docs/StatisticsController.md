# StatisticsController

This controller provides endpoints to retrieve analytical and statistical data regarding events, tickets, and validations.

## Endpoints

### Get Organizer Statistics

Retrieves the global statistics for a specific organizer within a given date range. This endpoint is secured and requires the user to have the `ORGANIZER` role. The `organizerId` is automatically extracted from the JWT token of the authenticated user.

**URL:** `/api/v1/statistics/organizer`

**Method:** `GET`

**Security:** Bearer Token (JWT), requires `ORGANIZER` role.

**Query Parameters:**
- `startDate` (required): The start date of the period to query tickets sold. Format: ISO-8601 DateTime (e.g., `2024-01-01T00:00:00Z`).
- `endDate` (required): The end date of the period to query tickets sold. Format: ISO-8601 DateTime (e.g., `2024-12-31T23:59:59Z`).

#### Example Request

```bash
curl -X GET "http://localhost:8080/api/v1/statistics/organizer?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59" \
     -H "Authorization: Bearer <your_jwt_token>"
```

#### Example Response
```json
{
  "totalSalesAmount": 1500,
  "totalTicketsValidated": 1200,
  "totalEventsCreated": 5
}
```

### Response Attributes
- `totalSalesAmount`: The total amount of tickets sold during the specified date range.
- `totalTicketsValidated`: The total number of tickets successfully validated across all events created by the organizer.
- `totalEventsCreated`: The total number of events ever created by this organizer.

