package com.example.user

import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Routing.users(userController: UserController = UserController()) {
  route("user") {
    post { userController.create(this.context) }
  }
  route("user/{id}") {
    authenticate {
      delete { userController.delete(this.context) }
      get { userController.getById(this.context) }
    }
  }
  route("me") {
    authenticate {
      get { userController.getSessionUser(this.context) }
      put { userController.update(this.context) }
    }
  }
}