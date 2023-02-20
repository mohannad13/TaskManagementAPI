
# Task Management API

A brief description of what this project does and who it's for

Task Management API

This is a RESTful API for a task management system. The API allows you to manage tasks and comments associated with them. Users can be assigned to tasks, and comments can be added to tasks. The API also includes authentication using JWT tokens.

Running the Application

To run the application, you will need to have Java and Maven installed on your machine. Here are the steps to follow:

Clone the repository(project) to your local machine.
Open a terminal or command prompt and navigate to the project's root directory.
Run mvn spring-boot:run to start the application.
The application will start on port 8080.
Endpoints

The API includes the following endpoints:

Task Endpoints
GET /api/tasks - retrieves all tasks (requires ADMIN role)
POST /api/tasks - creates a new task (requires ADMIN role)
GET /api/tasks/assigned?userId={userId} - retrieves all tasks assigned to a specific user (requires ADMIN role or the user assigned to the task)
PUT /api/tasks/{id} - updates a task by ID (requires ADMIN role)
DELETE /api/tasks/{id} - deletes a task by ID (requires ADMIN role)
Comment Endpoints
GET /api/comments - retrieves all comments (requires ADMIN role)
GET /api/comments/task/{taskId} - retrieves all comments for a specific task (requires ADMIN role or the user assigned to the task)
POST /api/comments - creates a new comment (requires ADMIN role or the user assigned to the task)
PUT /api/comments/{id} - updates a comment by ID (requires ADMIN role or the user who created the comment)
DELETE /api/comments/{id} - deletes a comment by ID (requires ADMIN role or the user who created the comment)
Authentication Endpoint
POST /authenticate - generates a JWT token for authentication. Takes a JSON body with email and password.
Note that all endpoints except for the authentication endpoint require a JWT token to be included in the Authorization header of the HTTP request.


body example to create user:
{
"name": "John Doe",
"email": "johndoe@example.com",
"password": "password",
"active": true
}

body example to create task assigned to use:

{
    "title": "Task 1",
    "description": "Description of task 1",
    "status": "assigned",
    "assignee": {
        "id": 2
    }
}



Testing the API

You can use a tool like Postman to test the API endpoints. To test the authentication endpoint, send a POST request to http://localhost:8080/authenticate with a JSON body like this:


Copy code
{
  "email": "user@example.com",
  "password": "password123"
}
The response will include a JWT token, which you can copy and use in the Authorization header of subsequent requests to other endpoints.

# Accessing the Local Database
To access the local H2 database, you can use the H2 Console. 
1. Start the application
2. Open a web browser and go to http://localhost:8080/h2-console
3. On the login page, enter the following information:
   - Driver Class: org.h2.Driver
   - JDBC URL: jdbc:h2:mem:testdb
   - User Name: sa
   - Password: (leave blank)
4. Click "Connect" to log in to the H2 Console and access the database.
