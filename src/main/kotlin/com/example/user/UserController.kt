package com.example.user

import com.example.auth.UserSession
import com.example.exception.AuthenticationException
import com.example.exception.NotFoundException
import com.example.utils.getParam
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import java.util.UUID

class UserController(private val userService: UserService) {
  suspend fun register(ctx: ApplicationCall) {
    ctx.receive<RegisterUser>().apply {
      userService.create(this).apply {
        ctx.respond(UserDTO(
          this.id,
          this.email,
          this.username
        ))
      }
    }
  }

  suspend fun getById(ctx: ApplicationCall) {
    val id = UUID.fromString(getParam("id", ctx))
    userService.getById(id).let {
      ctx.respond(UserDTO(
        it.id,
        it.email,
        it.username
      ))
    }
  }

  suspend fun getCurrent(ctx: ApplicationCall) {
    val session = ctx.sessions.get<UserSession>()
    requireNotNull(session).apply {
      userService.getById(UUID.fromString(this.userId)).let {
        ctx.respond(UserDTO(
          it.id,
          it.email,
          it.username
        ))
      }
    }
  }

  suspend fun update(ctx: ApplicationCall) {
    ctx.receive<User>().also { user ->
      userService.update(user).apply {
        ctx.respond(UserDTO(
          this.id,
          this.email,
          this.username
        ))
      }
    }
  }

  suspend fun delete(ctx: ApplicationCall) {
    ctx.receive<User>().apply {
      userService.delete(this.id)
    }
  }
}