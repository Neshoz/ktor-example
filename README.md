# Documentation

In this document you will find everything that is related to your task.

## Architecture & Tech stack

The project is built and started using Docker compose.
To build and start the project, run the following command from the root: `docker-compose up --build`

- Frontend: React, Vue or Angular
- Backend: Kotlin
- Database: PostgreSQL

## Backend

The backend is written in Kotlin, documentation for endpoints can be found in the [api.http file](./api.http)

## Database

The database is a PostgreSQL database, you can access pgAdmin [in the browser](http://localhost:8888).

Sign in with the following credentials
- Email: admin@admin.com
- Password: root

Add a server with the following credentials
- name (up to you)
- Host name: db
- Maintenance database: notes
- Username: postgres
- Password: root

## Your assignment

Your assignment is to implement a frontend to the API.
The only requirement that we have on tech is that you implement it using either React, Angular or Vue, we prefer React.

You are free to use any libraries you want to accomplish the task, but be ready to
explain your libraries of choice in the follow-up interview should they be questioned.

### Requirements

Below you will find a list of requirements your frontend should implement.
Requirements marked with (Bonus) are not mandatory.

- As a user, I want to be able to create an account with email, password and username.
- As a user, I want to log in with email and password.
- As a user, I want to log out from the app.
- As a user, I want to see all of my notes.
- As a user, I want to create a new note.
- As a user, I want to update a note.
- As a user, I want to delete a note.
- (Bonus): As a user, I want to add other users to a note.
- (Bonus): As a user, I want to update my email.
- (Bonus): As a user, I want to write markdown in my notes and preview the output.
- (Bonus): As an interviewer, I want to see that the candidate has written tests.