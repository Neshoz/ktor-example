package com.example.topic

import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Routing.topics(topicController: TopicController) {
  route("topic") {
    authenticate {
      post { topicController.create(this.context) }
      put { topicController.update(this.context) }
      get { topicController.getAll(this.context) }
    }
  }
  route("topic/{id}") {
    authenticate {
      get { topicController.getById(this.context) }
      delete { topicController.delete(this.context) }

      route("posts") {
        authenticate {
          get { topicController.getPosts(this.context) }
        }
      }
    }
  }
}