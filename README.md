# Documentation

In this document you will find everything that is related to your task.

## BEFORE YOU GET STARTED

Before you get started on your task, fork the repository, and work towards the forked version.

## Start the backend

The backend is built with Docker, so if you do not already have Docker installed on your system, you can install
it here https://www.docker.com/products/docker-desktop/

To start the backend, run `docker-compose up --build` from the root of the project.
The backend is running on `localhost:8000`, and there is no CORS configured.

## Your assignment

Your assignment is to implement a frontend to [the API](./api.http).
The only requirement that we have on tech is that you implement it using either React, Angular or Vue; we prefer React.

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
- As a user, I want to switch between light theme and dark theme.
- As a user, I want to come back to the last not I was working with after I close and visit the app again.
- (Bonus): As a user, I want to add other users to a note.
- (Bonus): As a user, I want to update my email.
- (Bonus): As an interviewer, I want to see that the candidate has written tests.