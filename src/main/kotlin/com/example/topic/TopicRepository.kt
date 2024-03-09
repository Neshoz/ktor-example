package com.example.topic

import com.example.exception.InternalServerError
import com.example.post.Posts
import com.example.user.Users
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

internal object Topics : UUIDTable("topics") {
  val createdBy = uuid("created-by").references(
    Users.id,
    onDelete = ReferenceOption.CASCADE
  )
  val createdAt = long("created-at")
  val content = text("content")

  fun toDomain(row: ResultRow): Topic {
    return Topic(
      id = row[id].value,
      createdBy = row[createdBy],
      createdAt = row[createdAt],
      content = row[content]
    )
  }
}

class TopicRepository {
  init {
    transaction {
      SchemaUtils.create(Topics)
    }
  }

  fun findAll(): List<Topic> {
    return transaction {
      Topics.selectAll()
        .map { Topics.toDomain(it) }
    }
  }

  fun findById(id: UUID): Topic? {
    return transaction {
      Topics
        .select { Topics.id eq id }
        .map {
          Topics.toDomain(it)
        }
        .firstOrNull()
    }
  }

  fun create(topic: TopicWithoutId): Topic {
    return transaction {
      Topics.insert {
        it[createdBy] = topic.createdBy
        it[createdAt] = topic.createdAt
        it[content] = topic.content
      }.let {
        val first = it.resultedValues?.firstOrNull()
        if (first == null) {
          throw InternalServerError()
        } else {
          Topics.toDomain(first)
        }
      }
    }
  }

  fun update(topic: Topic): Topic {
    return transaction {
      Topics.update({ Topics.id eq topic.id }) {
        it[content] = topic.content
      }.let {
        if (it > 0) topic else throw InternalServerError()
      }
    }
  }

  fun delete(id: UUID): Boolean {
    return transaction {
      Topics.deleteWhere { Topics.id eq id } > 0
    }
  }
}