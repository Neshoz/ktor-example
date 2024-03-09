package com.example.post

import com.example.exception.InternalServerError
import java.util.UUID

class PostService(private val postRepository: PostRepository) {
  fun getByTopicId(topicId: UUID): List<Post> {
    return postRepository.findByTopicId(topicId)
  }

  fun getById(postId: UUID): Post {
    return postRepository.findById(postId) ?: throw InternalServerError()
  }

  fun create(post: PostWithoutId): Post {
    return postRepository.create(post) ?: throw InternalServerError()
  }

  fun update(postId: UUID, post: UpdatePostRequestData): Post {
    return postRepository.update(postId, post)
  }

  fun delete(postId: UUID): Boolean {
    return postRepository.delete(postId)
  }
}