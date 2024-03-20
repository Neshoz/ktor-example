package com.example.auth

import io.ktor.server.routing.*

fun Routing.auth(authController: AuthController = AuthController()) {
  route("login") {
    post { authController.authenticate(this.context) }
  }
  route("logout") {
    post { authController.invalidate(this.context) }
  }
}