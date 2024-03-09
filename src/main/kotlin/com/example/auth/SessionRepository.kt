package com.example.auth

import com.example.exception.AuthenticationException
import com.example.extensions.upsert
import io.ktor.server.sessions.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.NoSuchElementException

internal object Sessions : Table("sessions") {
  private val id = uuid("id").autoGenerate()
  val sessionId = varchar("sessionId", 255)
  val userId = varchar("userId", 255)

  override val primaryKey = PrimaryKey(id)

  fun toDomain(row: ResultRow): UserSession {
    return UserSession(
      userId = row[userId]
    )
  }
}

class SessionRepository : SessionStorage {
  init {
    transaction {
      SchemaUtils.create(Sessions)
    }
  }

  override suspend fun invalidate(id: String) {
    transaction {
      Sessions.deleteWhere { Sessions.sessionId eq id }
    }
  }

  override suspend fun read(id: String): String {
    return transaction {
      Sessions.select { Sessions.sessionId eq id }
        .firstOrNull().let {
          if (it == null) {
            throw NoSuchElementException()
          } else {
            Sessions.toDomain(it).userId
          }
        }
    }
  }

  /*
    TODO: Write gets called every time read gets called, look in to why KTOR does this.
    TODO: Calling update before a session has been inserted, will lead to an error saying sessions
     aren't yet ready. Look in to an upsert statement.
   */
  override suspend fun write(id: String, value: String) {
    transaction {
      Sessions.insert {
        it[sessionId] = id
        it[userId] = id
      }
    }
  }
}