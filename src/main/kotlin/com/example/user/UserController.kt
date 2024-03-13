package com.example.user

import com.example.config.userId
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.UUID

class UserController(private val userService: UserService) {
  suspend fun register(call: ApplicationCall) {
    val payload = call.receive<CreateUserPayload>()
    val createdUser = userService.create(payload)
    call.respond(createdUser)
  }

  suspend fun getById(call: ApplicationCall) {
    val id = UUID.fromString(call.parameters["id"])
    val user = userService.getById(id)
    call.respond(user)
  }

  suspend fun getSessionUser(call: ApplicationCall) {
    val user = userService.getById(call.userId)
    call.respond(user)
  }

  suspend fun update(call: ApplicationCall) {
    val payload = call.receive<User>()
    val user = userService.update(payload)
    call.respond(user)
  }

  suspend fun delete(call: ApplicationCall) {
    call.receive<User>().apply {
      userService.delete(this.id)
    }
  }
}