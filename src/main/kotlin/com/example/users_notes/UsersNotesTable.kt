package com.example.users_notes

import com.example.note.NotesTable
import com.example.user.UsersTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UsersNotesTable : Table("users_notes") {
  val userId = uuid("userId").references(
    UsersTable.id,
    onDelete = ReferenceOption.CASCADE
  )
  val noteId = uuid("noteId").references(
    NotesTable.id,
    onDelete = ReferenceOption.CASCADE
  )

  override val primaryKey = PrimaryKey(userId, noteId)
}