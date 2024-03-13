package com.example.config

import com.example.auth.AuthController
import com.example.user.UserController
import com.example.user.UserRepository
import com.example.user.UserService
import org.kodein.di.*

object ModulesConfig {
  private val userModule = DI.Module("USER") {
    bindSingleton { UserController(instance()) }
    bindSingleton { UserService(instance()) }
    bindSingleton { UserRepository() }
  }
  private val authModule = DI.Module("AUTH") {
    bindSingleton { AuthController(instance()) }
  }

  internal val kodein = DI {
    import(userModule)
    import(authModule)
  }
}