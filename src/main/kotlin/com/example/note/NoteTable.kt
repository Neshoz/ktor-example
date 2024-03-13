package com.example.note

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object NotesTable : UUIDTable("notes") {
  val contents = text("contents")
  val created = datetime("created").defaultExpression(CurrentDateTime)
  val modified = datetime("modified").defaultExpression(CurrentDateTime)

  fun toModel(row: ResultRow): NoteModel {
    return NoteModel(
      id = row[id].value,
      contents = row[contents],
      created = row[created],
      modified = row[modified]
    )
  }
}