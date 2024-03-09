package com.example.user

import com.example.exception.InternalServerError
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

internal object Users : UUIDTable("users") {
  val email = varchar("email", 200).uniqueIndex()
  val username = varchar("username", 100).uniqueIndex()
  val password = varchar("password", 150)

  fun toDomain(row: ResultRow): User {
    return User(
      id = row[id].value,
      email =  row[email],
      username = row[username],
      password = row[password]
    )
  }
}

class UserRepository {
  init {
    transaction {
      SchemaUtils.create(Users)
    }
  }

  fun findByEmail(email: String): User? {
    return transaction {
      Users.select { Users.email eq email }
        .map { Users.toDomain(it) }
        .firstOrNull()
    }
  }

  fun findById(id: UUID): User? {
    return transaction {
      Users.select { Users.id eq id }
        .map{ Users.toDomain(it) }
        .firstOrNull()
    }
  }

  fun create(user: RegisterUser): User? {
    return transaction {
      Users.insert {
        it[email] = user.email
        it[username] = user.username
        it[password] = user.password
      }.resultedValues?.firstOrNull()?.let { Users.toDomain(it) }
    }
  }

  fun update(user: User): User {
    return transaction {
      Users.update({ Users.id eq user.id }) {
        it[email] = user.email
        it[username] = user.username
        it[password] = user.password
      }.let {
        if (it > 0) user else throw InternalServerError()
      }
    }
  }

  fun delete(userId: UUID): Boolean {
    return transaction {
      Users.deleteWhere { Users.id eq userId } > 0
    }
  }
}