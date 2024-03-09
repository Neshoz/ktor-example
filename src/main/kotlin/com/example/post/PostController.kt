package com.example.post

import com.example.auth.UserSession
import com.example.exception.InternalServerError
import com.example.utils.getParam
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import java.util.UUID

class PostController(private val postService: PostService) {
  suspend fun getById(ctx: ApplicationCall) {
    val postId = UUID.fromString(getParam("id", ctx))
    postService.getById(postId).apply {
      ctx.respond(this)
    }
  }

  suspend fun create(ctx: ApplicationCall) {
    ctx.receive<CreatePostRequestData>().apply {
      val createdBy = ctx.sessions.get<UserSession>()!!.userId
      val createdAt = System.currentTimeMillis()

      val post = PostWithoutId(
        topicId = this.topicId,
        createdBy = UUID.fromString(createdBy),
        createdAt = createdAt,
        content = this.content
      )
      postService.create(post).apply {
        ctx.respond(this)
      }
    }
  }

  suspend fun update(ctx: ApplicationCall) {
    val id = UUID.fromString(getParam("id", ctx))
    ctx.receive<UpdatePostRequestData>().apply {
      postService.update(id, this).apply {
        ctx.respond(this)
      }
    }
  }

  suspend fun delete(ctx: ApplicationCall) {
    val id = UUID.fromString(getParam("id", ctx))
    postService.delete(id).apply {
      if (this) {
        ctx.respond(HttpStatusCode.InternalServerError)
      } else {
        throw InternalServerError()
      }
    }
  }
}