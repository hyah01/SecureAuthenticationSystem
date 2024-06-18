# SecureAuthenticationSystem

## Introduction

This project implements a secure authentication system for managing blogs and users. It provides endpoints for CRUD operations on blogs, user registration, and administration features using Spring Security for role-based access control.

## Features

- **Blog Management**
    - Add, update, delete blogs
    - Retrieve blogs by various criteria (author, title, tags)
    - Admin-exclusive operations (e.g., get all blogs, toggle post status)
    - Sorting blogs by title

- **User Management**
    - User registration (both regular and admin users)
    - Delete user by ID
    - Retrieve all users (admin only)
    - View user details (self and admin)

- **Security**
    - Role-based access control using Spring Security annotations
    - Authentication and authorization of users for each operation

## Setup Instructions

1. Clone the repository: `git clone https://github.com/hyah01/SecureAuthenticationSystem` and then   
   `cd secure-authentication-system`
2. Configure database connection:
- Open `application.properties` located in `src/main/resources`
- Modify the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` properties according to your database settings.

3. Build the project: `mvn clean install`
4.  Run the application: `mvn spring-boot`
5. Access the application:
- By default, the application runs on `http://localhost:8080`
- Use tools like Postman to interact with the HTTP endpoints.

## Usage

### Blogs

- **Retrieve All Blogs**
- `GET /blogs/all` (Admin only)

- **Retrieve Posted Blogs**
- `GET /blogs`

- **Retrieve Unposted Blogs**
- `GET /blogs/unposted` (Admin only)

- **Retrieve Current User's Unposted Blogs**
- `GET /blogs/myunpost`

- **Retrieve Blog by ID**
- `GET /blogs/{id}` (Admin only)

- **Retrieve Blog by Title**
- `GET /blogs/title?title={title}`

- **Retrieve Blog by Author**
- `GET /blogs/author?author={author}`

- **Retrieve Blogs by Tag**
- `GET /blogs/tag/{tag}`

- **Retrieve Blogs Sorted by Title**
- `GET /blogs/sorted`

### Users

- **Register User**
- `POST /user/save`

- **Register Admin User**
- `POST /user/save/admin` (Admin only)

- **Delete User**
- `DELETE /users/{id}`

- **Retrieve All Users**
- `GET /users/all` (Admin only)

- **Retrieve Current User Details**
- `GET /user/single`