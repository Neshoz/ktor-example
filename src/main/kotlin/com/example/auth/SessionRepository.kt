package com.example.auth

import com.example.database.DatabaseSingleton.dbQuery
import com.example.exception.AuthenticationException
import io.ktor.server.sessions.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime
import kotlin.NoSuchElementException

class SessionStorageRepository : SessionStorage {

  override suspend fun write(id: String, value: String): Unit = dbQuery {
    SessionsTable.upsert {
      it[sessionId] = id
      it[userId] = value
      it[modified] = LocalDateTime.now()
    }
  }

  override suspend fun read(id: String): String = dbQuery {
    val session = SessionsTable.selectAll().where { SessionsTable.sessionId eq id }.firstOrNull()
    if (session == null) {
      throw AuthenticationException()
    } else {
      session[SessionsTable.userId].toString()
    }
  }

  override suspend fun invalidate(id: String): Unit = dbQuery {
    SessionsTable.deleteWhere { sessionId eq id } > 0
  }
}