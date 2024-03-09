package com.example.auth

import com.example.exception.InsufficientCredentialsException
import com.example.user.UserDTO
import com.example.user.UserService
import com.example.utils.AES
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

class AuthController(
  private val userService: UserService
) {
  suspend fun authenticate(ctx: ApplicationCall) {
    ctx.receive<Credentials>().apply {
      userService.getByEmail(this.email).let {
        requireNotNull(it) {
          throw InsufficientCredentialsException(
            "Email or password incorrect"
          )
        }
        require(it.password == AES.encrypt(this.password)) {
          throw InsufficientCredentialsException(
            "Email or password incorrect"
          )
        }
        ctx.sessions.set(UserSession(it.id.toString())).apply {
          val (id, email, username) = it
          ctx.respond(UserDTO(
            id,
            email,
            username
          ))
        }
      }
    }
  }

  suspend fun invalidate(ctx: ApplicationCall) {
    ctx.sessions.clear<UserSession>()
    ctx.respond(HttpStatusCode.OK)
  }
}