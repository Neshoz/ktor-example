package com.example.auth

import io.ktor.server.auth.*
import java.util.UUID

data class UserPrincipal(
  val userId: UUID
): Principal