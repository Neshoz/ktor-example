package com.example.user

import com.example.database.DatabaseSingleton.dbQuery
import com.example.exception.InternalServerError
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class UserRepository {
  suspend fun findByEmail(email: String): User? = dbQuery {
    UsersTable.selectAll().where { UsersTable.email eq email }
      .map(UsersTable::toDomain)
      .firstOrNull()
  }

  suspend fun findById(id: UUID): UserDTO? = dbQuery {
    UsersTable.selectAll().where { UsersTable.id eq id }
      .map(UsersTable::toDto)
      .firstOrNull()
  }

  suspend fun create(user: CreateUserPayload): UserDTO? = dbQuery {
    UsersTable.insert {
      it[email] = user.email
      it[username] = user.username
      it[password] = user.password
    }.resultedValues?.first()?.let(UsersTable::toDto)
  }

  suspend fun update(user: User): User = dbQuery {
    UsersTable.update({ UsersTable.id eq user.id }) {
      it[email] = user.email
      it[username] = user.username
      it[password] = user.password
    }.let {
      if (it > 0) user else throw InternalServerError()
    }
  }

  suspend fun delete(userId: UUID): Boolean = dbQuery {
    UsersTable.deleteWhere{ UsersTable.id eq userId } > 0
  }
}