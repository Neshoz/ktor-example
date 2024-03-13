package com.example.auth

import com.example.exception.InsufficientCredentialsException
import com.example.user.UserDTO
import com.example.user.UserService
import com.example.utils.AES
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

class AuthController(
  private val userService: UserService
) {
  suspend fun authenticate(call: ApplicationCall) {
    val payload = call.receive<Credentials>()
    val user = userService.getByEmail(payload.email) ?: throw InsufficientCredentialsException(
      "Email or password incorrect"
    )
    if (user.password != AES.encrypt(payload.password)) {
      throw InsufficientCredentialsException("Email or password incorrect")
    }

    call.sessions.set(UserPrincipal(user.id))
    val (id, email, username) = user
    call.respond(UserDTO(
      id,
      email,
      username
    ))
  }

  suspend fun invalidate(ctx: ApplicationCall) {
    ctx.sessions.clear<UserPrincipal>()
    ctx.respond(HttpStatusCode.OK)
  }
}