# Blog Platform (Spring Boot + MongoDB)

A full-featured blogging platform built with Spring Boot, MongoDB, and JWT authentication.  
Supports user registration, login, blog CRUD, categories, comments, likes, and admin features.

---

## Features

- **User Registration & Login** (JWT authentication)
- **Blog CRUD** (Create, Read, Update, Delete)
- **Categories** (add, list, filter blogs by category)
- **Comments** (add, delete, list)
- **Likes** (like/unlike blogs)
- **Admin Panel** (manage users, categories)
- **Role-based Access Control** (`USER`, `ADMIN`)
- **RESTful API** with clear response structure

---

## Tech Stack

- **Backend:** Spring Boot, Spring Security, JWT
- **Database:** MongoDB
- **Build:** Maven
- **Other:** Lombok, SLF4J

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/blog-platform.git
cd blog-platform
```

### 2. Configure MongoDB

- Copy `src/main/resources/application.yaml.example` to `application.yaml`
- Set your MongoDB URI, username, and password:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://<username>:<password>@<cluster-url>/?retryWrites=true&w=majority
      database: blogsDB
```

### 3. Build and Run

```bash
./mvnw spring-boot:run
```
or
```bash
mvn spring-boot:run
```

The server will start on [http://localhost:8080](http://localhost:8080)

---

## API Endpoints

### Auth

- `POST /auth/register` — Register a new user  
  **Body:** `{ "username": "...", "email": "...", "password": "...", "confirmPassword": "..." }`
- `POST /auth/login` — Login and receive JWT  
  **Body:** `{ "username": "...", "password": "..." }`
- `GET /auth/profile` — Get current user profile (JWT required)

### Blogs

- `POST /blogs` — Create blog (JWT required)  
  **Body:** Blog JSON, **Query:** `categories=cat1&categories=cat2`
- `GET /blogs` — List all blogs
- `GET /blogs/{blogId}` — Get blog by ID
- `DELETE /blogs/{blogId}` — Delete blog (author or admin only)
- `PUT /blogs/{blogId}/like` — Like/unlike a blog (JWT required)
- `GET /blogs/category/{categoryName}` — Blogs by category
- `GET /blogs/my-blogs` — Blogs by current user (JWT required)

### Comments

- `POST /blogs/{blogId}/comment` — Add comment (JWT required)
- `DELETE /blogs/{blogId}/comment/{commentId}` — Delete comment (author, blog author, or admin)
- `GET /blogs/{blogId}/comments` — List comments for a blog

### Categories

- `GET /public/all-categories` — List all categories
- `POST /admin/add-category` — Add category (admin only)

### Admin

- `GET /admin/all-users` — List all users (admin only)

---

## Authentication

- Uses JWT for stateless authentication.
- Pass JWT in `Authorization: Bearer <token>` header for protected endpoints.

---

## Development

- Java 17+ recommended
- Uses Lombok — install Lombok plugin for your IDE

---

## Testing

```bash
mvn test
```

---

## License

MIT

---

## Contributing

Pull requests welcome! Please open an issue first to discuss changes.

---

## Contact

For questions or support, open an issue or contact
