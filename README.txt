# Communication App

## Overview
The Communication App is a RESTful API designed to manage and deliver communications across various channels. It supports CRUD operations, file scanning for new communications, scheduled processing, and asynchronous delivery via configurable channels such as email and WhatsApp.

---

## Features
1. **RESTful API** for managing communications (CRUD operations).
2. **File Scanning** to automatically import communications from a specified directory.
3. **Scheduled Processing** for handling unprocessed communications.
4. **Asynchronous Delivery** of communications via channels like email, WhatsApp, etc.
    - Fake implementations for non-email channels (e.g., WhatsApp) with logging and random delays.
5. **Validation** for creating and updating communications to ensure data integrity.

---

## Setup Instructions

### 1. Build the Project
```bash
mvn clean package
```

### 2. Start Docker Containers
```bash
docker-compose up --build
```

### 3. Access the Application
- **Admin Console**: [http://localhost:9990](http://localhost:9990)
  Use the credentials: `admin/admin` (configured in the Dockerfile).
- **Application API**: [http://localhost:8080/communication-app](http://localhost:8080/communication-app)

---

## Endpoints

### 1. Fetch All Communications
**GET /communications**
Fetches a list of all stored communications. Returns `204 No Content` if no communications are found.

**Example Curl:**
```bash
curl -X GET http://localhost:8080/communication-app/api/communications
```

---

### 2. Create a New Communication
**POST /communications**
Creates a new communication. The `type` field is mandatory.

**Example Request Body:**
```json
{
    "name": "New Communication",
    "body": "This is the body of the communication.",
    "deliverySettings": "Standard",
    "size": 1024,
    "status": "LOADED",
    "type": "Email"
}
```

**Example Curl:**
```bash
curl -X POST http://localhost:8080/communication-app/api/communications \
-H "Content-Type: application/json" \
-d '{
    "name": "New Communication",
    "body": "This is the body of the communication.",
    "deliverySettings": "Standard",
    "size": 1024,
    "status": "LOADED",
    "type": "Email"
}'
```

---

### 3. Fetch a Communication by ID
**GET /communications/{id}**
Fetches a specific communication by its ID. Returns `404 Not Found` if the ID does not exist.

**Example Curl:**
```bash
curl -X GET http://localhost:8080/communication-app/api/communications/1
```

---

### 4. Update a Communication
**PUT /communications/{id}**
Updates an existing communication. The `type` field is mandatory.

**Example Request Body:**
```json
{
    "name": "Updated Communication",
    "body": "This is the updated body of the communication.",
    "deliverySettings": "Express",
    "size": 2048,
    "status": "PROCESSED",
    "type": "SMS"
}
```

**Example Curl:**
```bash
curl -X PUT http://localhost:8080/communication-app/api/communications/1 \
-H "Content-Type: application/json" \
-d '{
    "name": "Updated Communication",
    "body": "This is the updated body of the communication.",
    "deliverySettings": "Express",
    "size": 2048,
    "status": "PROCESSED",
    "type": "SMS"
}'
```

---

### 5. Delete a Communication
**DELETE /communications/{id}**
Deletes a specific communication by its ID. Returns `404 Not Found` if the ID does not exist.

**Example Curl:**
```bash
curl -X DELETE http://localhost:8080/communication-app/api/communications/1
```

---

### 6. Deliver a Communication
**POST /communications/{id}/deliver**
Delivers a specific communication asynchronously.

**Example Curl:**
```bash
curl -X POST http://localhost:8080/communication-app/api/communications/1/deliver
```

---

### 7. Batch Deliver Communications
**POST /communications/batch_delivery**
Processes and delivers all eligible communications.

**Example Curl:**
```bash
curl -X POST http://localhost:8080/communication-app/api/communications/batch_delivery
```

---

## Validation Rules

1. **Mandatory Fields for `POST` and `PUT`:**
   - `name`: Must not be blank.
   - `body`: Must not be blank.
   - `deliverySettings`: Must not be blank.
   - `size`: Must not be null.
   - `status`: Must not be null.
   - `type`: Must not be blank.

2. **Response Codes:**
   - `200 OK`: Successful operation.
   - `201 Created`: Resource created successfully.
   - `204 No Content`: No data found for the request.
   - `400 Bad Request`: Validation error.
   - `404 Not Found`: Resource not found.
   - `500 Internal Server Error`: Unexpected server error.

---

### Troubleshooting

1. **Table Not Found (`relation "communications" does not exist`)**:
   Ensure the database schema is initialized correctly by running the `init.sql` script in your `resources/db` folder. Restart Docker with `docker-compose down -v` to recreate the database if necessary.

2. **Validation Errors**:
   If validation fails, check the error message in the response body for details about the invalid fields.

3. **Admin Console Not Accessible**:
   Ensure WildFly is running on port `9990` and your browser is pointing to `http://localhost:9990`.

---

