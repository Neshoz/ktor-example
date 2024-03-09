package com.example.post

import com.example.extensions.validateAndThrowOnFailure
import com.example.utils.UUIDSerializer
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Post(
  @Serializable(with = UUIDSerializer::class)
  val id: UUID,
  @Serializable(with = UUIDSerializer::class)
  val topicId: UUID,
  @Serializable(with = UUIDSerializer::class)
  val createdBy: UUID,
  val createdAt: Long,
  val content: String
) {
  init {
    Validation {
      Post::id required {}
      Post::topicId required {}
      Post::createdBy required {}
      Post::createdAt required {}
      Post::content required {
        minLength(12)
        maxLength(255)
      }
    }.validateAndThrowOnFailure(this)
  }
}

@Serializable
data class PostWithoutId(
  @Serializable(with = UUIDSerializer::class)
  val topicId: UUID,
  @Serializable(with = UUIDSerializer::class)
  val createdBy: UUID,
  val createdAt: Long,
  val content: String
) {
  init {
    Validation<PostWithoutId> {
      PostWithoutId::topicId required {}
      PostWithoutId::createdBy required {}
      PostWithoutId::createdAt required {}
      PostWithoutId::content required {
        minLength(12)
        maxLength(255)
      }
    }.validateAndThrowOnFailure(this)
  }
}

@Serializable
data class CreatePostRequestData(
  @Serializable(with = UUIDSerializer::class)
  val topicId: UUID,
  val content: String,
) {
  init {
    Validation {
      CreatePostRequestData::topicId required {}
      CreatePostRequestData::content required {}
    }
  }
}

@Serializable
data class UpdatePostRequestData(
  val content: String
) {
  init {
    Validation {
      UpdatePostRequestData::content required {
        minLength(12)
        maxLength(255)
      }
    }
  }
}