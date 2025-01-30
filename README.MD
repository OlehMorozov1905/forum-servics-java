# Forum Service

# Forum Service

## Description
Forum Service is a backend API for managing forum posts, comments, and users. It allows creating, updating, and deleting posts, adding comments, and managing user roles.

## Technology Stack
- **Java 17**
- **Spring Boot 2.x**
- **Spring Security** (for authentication/authorization)
- **Spring Data MongoDB** (for MongoDB database integration)
- **ModelMapper** (for object mapping)
- **Swagger UI** (for API documentation/testing)
- **BCrypt** (for password hashing)

## Functionality
### Posts
- **Create new post**  
  `POST /forum/post/{author}` - create a new post by author.
- **Get post by ID**  
  `GET /forum/post/{id}` - retrieve post details by ID.
- **Delete post**  
  `DELETE /forum/post/{id}` - delete post by ID.
- **Update post**  
  `PUT /forum/post/{id}` - update post information (e.g., text or tags).
- **Add comment to post**  
  `PUT /forum/post/{id}/comment/{author}` - add new comment to post.
- **Like post**  
  `PUT /forum/post/{id}/like` - add like to post.

## Installation & Setup
### Clone Repository
```sh
git clone https://github.com/your-repository/forum-service.git
Database Configuration
Configure database connection in application.properties.

Build & Run
mvnw clean install
mvnw spring-boot:run

API Testing with Swagger UI
Access Swagger UI to test API endpoints:

Visit:
➡️ http://localhost:8080/swagger-ui.html

Authentication
Username: admin
Password: admin

"Documentation for Postman tests has also been added to the root of the project:
'Forum.postman_collection' and 'UserAccount.postman_collection'."

