package com.example.user

import com.example.utils.respondError
import com.example.utils.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.UUID

class UserController(private val userRepository: UserRepository = UserRepository) {
  suspend fun create(call: ApplicationCall) {
    val payload = call.receive<CreateUserPayload>()
    val maybeUser = userRepository.findByEmail(payload.email)
    if (maybeUser != null) {
      return call.respondError(
        HttpStatusCode.BadRequest,
        "A user with that email already exist"
      )
    }
    val createdUser = userRepository.create(payload) ?: return call.respondError(
      HttpStatusCode.InternalServerError,
      "Internal server error"
    )
    call.respond(createdUser)
  }

  suspend fun searchUsers(call: ApplicationCall) {
    val searchTerm = call.request.queryParameters["q"] ?: return call.respondError(
      HttpStatusCode.BadRequest,
      "Missing parameter q"
    )
    val users = userRepository.searchUsers(searchTerm)
    call.respond(users)
  }

  suspend fun getById(call: ApplicationCall) {
    val id = UUID.fromString(call.parameters["id"])
    val user = userRepository.findById(id)
      ?: return call.respondError(
        HttpStatusCode.NotFound,
        "User with id $id not found"
      )
    call.respond(user)
  }

  suspend fun getSessionUser(call: ApplicationCall) {
    val user = userRepository.findById(call.userId)
    call.respond(user!!)
  }

  suspend fun update(call: ApplicationCall) {
    val payload = call.receive<User>()
    val user = userRepository.update(payload)
    call.respond(user)
  }

  suspend fun delete(call: ApplicationCall) {
    call.receive<User>().apply {
      userRepository.delete(this.id)
    }
  }
}