package com.example.post

import com.example.exception.InternalServerError
import com.example.exception.NotFoundException
import com.example.topic.Topics
import com.example.user.Users
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

internal object Posts : UUIDTable("posts") {
  val topicId = uuid("topic-id").references(
    Topics.id,
    onDelete = ReferenceOption.CASCADE,
  )
  val createdBy = uuid("created-by").references(
    Users.id,
    onDelete = ReferenceOption.CASCADE
  )
  val createdAt = long("created-at")
  val content = text("content")

  fun toDomain(row: ResultRow): Post {
    return Post(
      id = row[id].value,
      topicId = row[topicId],
      createdBy = row[createdBy],
      createdAt = row[createdAt],
      content = row[content]
    )
  }
}

class PostRepository {
  init {
    transaction {
      SchemaUtils.create(Posts)
    }
  }

  fun findByTopicId(topicId: UUID): List<Post> {
    return transaction {
      Posts
        .select { Posts.topicId eq topicId }
        .map { Posts.toDomain(it) }
    }
  }

  fun findById(id: UUID): Post? {
    return transaction {
      Posts.select { Posts.id eq id }
        .map { Posts.toDomain(it) }
        .firstOrNull()
    }
  }

  fun create(post: PostWithoutId): Post? {
    return transaction {
      Posts.insert {
        it[topicId] = post.topicId
        it[createdBy] = post.createdBy
        it[createdAt] = post.createdAt
        it[content] = post.content
      }.resultedValues?.firstOrNull()?.let { Posts.toDomain(it) }
    }
  }

  fun update(postId: UUID, post: UpdatePostRequestData): Post {
    return transaction {
      Posts.update({ Posts.id eq postId }) {
        it[content] = post.content
      }.let {
        if (it > 0) {
          findById(postId) ?: throw NotFoundException(postId.toString())
        } else {
          throw InternalServerError()
        }
      }
    }
  }

  fun delete(id: UUID): Boolean {
    return transaction {
      Topics.deleteWhere { Posts.id eq id } > 0
    }
  }
}