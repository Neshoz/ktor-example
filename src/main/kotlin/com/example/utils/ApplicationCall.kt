package com.example.utils

import com.example.auth.UserPrincipal
import com.example.exception.AuthenticationException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import java.util.*

val ApplicationCall.userId: UUID
  get() = principal<UserPrincipal>()?.userId ?: throw AuthenticationException()

suspend fun ApplicationCall.respondError(
  httpStatusCode: HttpStatusCode,
  error: String
) = respond(httpStatusCode, mapOf("message" to error))