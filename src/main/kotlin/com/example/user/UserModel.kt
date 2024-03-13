package com.example.user

import com.example.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
  @Serializable(with = UUIDSerializer::class)
  val id: UUID,
  val email: String,
  val username: String,
  val password: String
)

@Serializable
data class CreateUserPayload(
  val email: String,
  val username: String,
  val password: String
)

@Serializable
data class UserDTO(
  @Serializable(with = UUIDSerializer::class)
  val id: UUID,
  val email: String,
  val username: String
)
