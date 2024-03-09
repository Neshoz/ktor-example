package com.example.auth

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
  val userId: String
) : Principal