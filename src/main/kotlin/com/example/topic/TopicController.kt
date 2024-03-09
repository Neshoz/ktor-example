package com.example.topic

import com.example.auth.UserSession
import com.example.exception.InternalServerError
import com.example.exception.NotFoundException
import com.example.post.PostService
import com.example.utils.getParam
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*

class TopicController(
  private val topicService: TopicService,
  private val postService: PostService
) {
  suspend fun getAll(ctx: ApplicationCall) {
    topicService.getAll().apply {
      ctx.respond(this)
    }
  }

  suspend fun getById(ctx: ApplicationCall) {
    val id = UUID.fromString(getParam("id", ctx))
    topicService.getById(id).let {
      if (it == null) {
        throw NotFoundException(id.toString())
      } else {
        ctx.respond(it)
      }
    }
  }

  suspend fun getPosts(ctx: ApplicationCall) {
    val id = UUID.fromString(getParam("id", ctx))
    postService.getByTopicId(id).apply {
      ctx.respond(this)
    }
  }

  suspend fun create(ctx: ApplicationCall) {
    ctx.receive<CreateTopicRequestData>().apply {
      val session = ctx.authentication.principal<UserSession>()!!

      val topic = TopicWithoutId(
        UUID.fromString(session.userId),
        System.currentTimeMillis(),
        this.content
      )

      topicService.create(topic).apply {
        ctx.respond(this)
      }
    }
  }

  suspend fun update(ctx: ApplicationCall) {
    ctx.receive<Topic>().apply {
      topicService.update(this).let {
        ctx.respond(it)
      }
    }
  }

  suspend fun delete(ctx: ApplicationCall) {
    val id = UUID.fromString(getParam("id", ctx))
    topicService.delete(id).apply {
      if (!this) {
        throw InternalServerError()
      } else {
        ctx.respond(HttpStatusCode.OK)
      }
    }
  }
}