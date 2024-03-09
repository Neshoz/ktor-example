package com.example.topic

import com.example.extensions.validateAndThrowOnFailure
import com.example.post.Post
import com.example.utils.UUIDSerializer
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Topic(
  @Serializable(with = UUIDSerializer::class)
  val id: UUID,
  @Serializable(with = UUIDSerializer::class)
  val createdBy: UUID,
  val createdAt: Long,
  val content: String
) {
  init {
    Validation {
      Topic::id required {}
      Topic::createdBy required {}
      Topic::createdAt required {}
      Topic::content required {
        minLength(12)
        maxLength(255)
      }
    }.validateAndThrowOnFailure(this)
  }
}

@Serializable
data class TopicWithoutId(
  @Serializable(with = UUIDSerializer::class)
  val createdBy: UUID,
  val createdAt: Long,
  val content: String
) {
  init {
    Validation {
      TopicWithoutId::createdAt required {}
      TopicWithoutId::createdBy required {}
      TopicWithoutId::content required {
        minLength(12)
        maxLength(255)
      }
    }.validateAndThrowOnFailure(this)
  }
}

@Serializable
data class CreateTopicRequestData(
  val content: String
) {
  init {
    Validation {
      CreateTopicRequestData::content required {
        minLength(12)
        maxLength(255)
      }
    }.validateAndThrowOnFailure(this)
  }
}