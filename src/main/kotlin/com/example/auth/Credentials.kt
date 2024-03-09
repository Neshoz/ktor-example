package com.example.auth

import kotlinx.serialization.Serializable

@Serializable
data class Credentials(
  val email: String,
  val password: String
)