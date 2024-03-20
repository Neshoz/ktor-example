package com.example.config

import com.example.auth.*
import com.example.database.DatabaseSingleton
import com.example.exception.*
import com.example.note.notes
import com.example.user.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import org.kodein.di.instance
import java.util.UUID

fun Application.module() {
  DatabaseSingleton.init(environment.config)

  install(ContentNegotiation) { json() }
  install(Sessions) {
    val secretSignKey = hex("45998905450534590")
    cookie<UserPrincipal>("session-id", storage = SessionStorageRepository()) {
      cookie.path = "/"
      cookie.maxAgeInSeconds = 60 * 60 * 24
      cookie.httpOnly = true
      cookie.extensions["SameSite"] = "lax"
      transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
    }
  }
  install(Authentication) {
    session<UserPrincipal> {
      validate { session -> session }
      challenge {
        throw AuthenticationException()
      }
    }
  }
  install(StatusPages) {
    exception<AuthenticationException> { call, cause ->
      call.respond(
        HttpStatusCode.Unauthorized,
        mapOf("message" to cause.message)
      )
    }
    exception<NotFoundException> { call, cause ->
      call.respond(
        HttpStatusCode.NotFound,
        mapOf("message" to cause.message)
      )
    }
    exception<AlreadyExistsException> { call, cause ->
      call.respond(
        HttpStatusCode.BadRequest,
        mapOf("message" to cause.message)
      )
    }
    exception<InternalServerError> { call, cause ->
      call.respond(
        HttpStatusCode.InternalServerError,
        mapOf("message" to cause.message)
      )
    }
    exception<IllegalArgumentException> { call, cause ->
      val message = cause.message ?: "Bad Request"
      call.respond(
        HttpStatusCode.BadRequest,
        mapOf("message" to message)
      )
    }
  }
  install(Routing) {
    users()
    auth()
    notes()
  }
}

