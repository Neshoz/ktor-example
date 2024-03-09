package com.example.post

import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Routing.posts(postController: PostController) {
  route("post") {
    authenticate {
      post { postController.create(this.context) }
    }
  }
  route("post/{id}") {
    authenticate {
      get { postController.getById(this.context) }
      delete { postController.delete(this.context) }
      put { postController.update(this.context) }
    }
  }
}