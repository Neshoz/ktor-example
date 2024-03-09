package com.example.config

import com.example.auth.AuthController
import com.example.auth.SessionRepository
import com.example.post.PostController
import com.example.post.PostRepository
import com.example.post.PostService
import com.example.topic.TopicController
import com.example.topic.TopicRepository
import com.example.topic.TopicService
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
  private val topicModule = DI.Module("TOPIC") {
    bindSingleton { TopicController(instance(), instance()) }
    bindSingleton { TopicService(instance()) }
    bindSingleton { TopicRepository() }
  }
  private val postModule = DI.Module("POST") {
    bindSingleton { PostController(instance()) }
    bindSingleton { PostService(instance()) }
    bindSingleton { PostRepository() }
  }

  internal val kodein = DI {
    import(userModule)
    import(authModule)
    import(topicModule)
    import(postModule)
  }
}