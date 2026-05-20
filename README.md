# Document Management System (DMS)

## Project Overview
Document Management System backend built using Spring Boot. Users can upload, manage, organize, search, and share documents securely.

---

# Features

- User Registration & Login
- JWT Authentication
- Upload Documents
- Download Documents
- Soft Delete & Restore
- Folder Management
- Document Search
- Pagination
- Role-Based Access
- Document Ownership
- Document Sharing
- Version Control
- Swagger Documentation

---

# Tech Stack

- Java 17
- Spring Boot 3
- Spring Security
- JWT
- Spring Data JPA
- MySQL
- Maven
- Swagger/OpenAPI

---

# Project Structure

```text
src/main/java/com/dms
│
├── controller
├── service
├── repository
├── entity
├── dto
├── security
├── config
└── util

#Authentication APIs
Register User
POST /auth/register
Login User
POST /auth/login

#Document APIs

Upload Document
POST /documents/upload
Get All Documents
GET /documents
Search Documents
GET /documents/search
Download Document
DELETE /documents/{id}
Restore Document
PUT /documents/{id}/restore
POST/document/{id}/share
GET /document/download/{id}
#Folder APIs

Create Folder
POST /folders
Get Documents By Folder
GET /documents/folder/{folderId}

#Swagger URL
http://localhost:8080/swagger-ui/index.html

Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/dms_db
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update

file.upload-dir=uploads/
Run Project
Clone Project
git clone <repository-url>
Run Application
mvn spring-boot:run

#Postman Testing Flow
Register User
Login User
Copy JWT Token
Add Bearer Token
Upload Document
Create Folder
Search Documents
Download Document

Author

Mahesh Kapilavai