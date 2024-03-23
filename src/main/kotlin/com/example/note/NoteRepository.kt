package com.example.note

import com.example.database.DatabaseSingleton.dbQuery
import com.example.exception.InternalServerError
import com.example.users_notes.UsersNotesTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime
import java.util.UUID

object NoteRepository {
  suspend fun createNote(userId: UUID, payload: CreateNotePayload): NoteModel = dbQuery {
    val note = NotesTable.insert {
      it[contents] = payload.contents
      it[createdBy] = userId
      it[created] = LocalDateTime.now()
      it[modified] = LocalDateTime.now()
    }.resultedValues?.single()?.let(NotesTable::toModel) ?: throw InternalServerError()

    UsersNotesTable.insert {
      it[UsersNotesTable.userId] = userId
      it[noteId] = note.id
    }

    note
  }

  suspend fun addUserToNote(userId: UUID, noteId: UUID) = dbQuery {
    UsersNotesTable.insert {
      it[UsersNotesTable.userId] = userId
      it[UsersNotesTable.noteId] = noteId
    }.resultedValues?.single()
  }

  suspend fun findUserNotes(userId: UUID): List<NoteModel> = dbQuery {
    (UsersNotesTable leftJoin NotesTable)
      .selectAll().where { UsersNotesTable.userId eq userId }
      .map(NotesTable::toModel)
  }

  suspend fun findById(id: UUID): NoteModel? = dbQuery {
    NotesTable.selectAll().where { NotesTable.id eq id }
      .map(NotesTable::toModel)
      .firstOrNull()
  }

  suspend fun update(note: NoteModel): NoteModel = dbQuery {
    NotesTable.update({ NotesTable.id eq note.id }) {
      it[contents] = note.contents
      it[modified] = LocalDateTime.now()
    }.let {
      if (it > 0) note else throw InternalServerError()
    }
  }

  suspend fun delete(noteId: UUID): Boolean = dbQuery {
    NotesTable.deleteWhere { NotesTable.id eq noteId } > 0
  }
}