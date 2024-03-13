package com.example.auth

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object SessionsTable : Table("sessions") {
  val sessionId = varchar("sessionId", 255)
  val userId = varchar("userId", 255)
  val modified = datetime("modified").defaultExpression(CurrentDateTime)

  override val primaryKey = PrimaryKey(sessionId)
}