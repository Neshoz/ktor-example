package com.example.config

import com.example.auth.*
import com.example.user.*
import com.example.exception.AuthenticationException
import com.example.exception.InsufficientCredentialsException
import com.example.exception.InternalServerError
import com.example.exception.NotFoundException
import com.example.post.PostController
import com.example.post.posts
import com.example.topic.TopicController
import com.example.topic.topics
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

fun Application.module() {
  val userController by ModulesConfig.kodein.instance<UserController>()
  val authController by ModulesConfig.kodein.instance<AuthController>()
  val topicController by ModulesConfig.kodein.instance<TopicController>()
  val postController by ModulesConfig.kodein.instance<PostController>()

  DbConfig.setup()
  install(ContentNegotiation) {
    json()
  }
  install(Sessions) {
    val secretSignKey = hex("45998905450534590")
    cookie<UserSession>("session-id", SessionStorageMemory()) {
      cookie.path = "/"
      cookie.maxAgeInSeconds = 60 * 60 * 24
      cookie.httpOnly = true
      cookie.extensions["SameSite"] = "lax"
      transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
    }
  }
  install(Authentication) {
    session<UserSession> {
      validate { session ->
        session
      }
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
    exception<InsufficientCredentialsException> { call, cause ->
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
    users(userController)
    auth(authController)
    topics(topicController)
    posts(postController)
  }
}