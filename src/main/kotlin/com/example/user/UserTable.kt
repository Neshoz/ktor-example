package com.example.user

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow

object UsersTable : UUIDTable("users") {
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

  fun toDto(row: ResultRow): UserDTO {
    return UserDTO(
      id = row[id].value,
      email =  row[email],
      username = row[username],
    )
  }
}