package com.example.note

import com.example.user.UsersTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object NotesTable : UUIDTable("notes") {
  val contents = text("contents")
  val createdBy = uuid("created_by").references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
  val created = datetime("created").defaultExpression(CurrentDateTime)
  val modified = datetime("modified").defaultExpression(CurrentDateTime)

  fun toModel(row: ResultRow): NoteModel {
    return NoteModel(
      id = row[id].value,
      createdBy = row[createdBy],
      contents = row[contents],
      created = row[created],
      modified = row[modified]
    )
  }
}