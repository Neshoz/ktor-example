package com.example.note

import com.example.exception.InternalServerError
import com.example.utils.respondError
import com.example.utils.userId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.*
import kotlin.NoSuchElementException

class NoteController(private val noteRepository: NoteRepository = NoteRepository) {
  suspend fun create(call: ApplicationCall) {
    val payload = call.receive<CreateNotePayload>()
    val createdNote = noteRepository.createNote(call.userId, payload)
    call.respond(createdNote)
  }

  suspend fun getUserNotes(call: ApplicationCall) {
    val notes = noteRepository.findUserNotes(call.userId)
    call.respond(notes)
  }

  suspend fun addUserToNote(call: ApplicationCall) {
    val noteId = UUID.fromString(call.parameters["id"])
    val userId = UUID.fromString(call.parameters["userId"])
    noteRepository.addUserToNote(userId, noteId) ?: call.respondError(
      HttpStatusCode.InternalServerError,
      "Internal server error"
    )
    call.respond(HttpStatusCode.OK, mapOf("message" to "User $userId added to note $noteId"))
  }

  suspend fun findById(call: ApplicationCall) {
    val id = UUID.fromString(call.parameters["id"])
    val note = noteRepository.findById(id) ?: call.respondError(
      HttpStatusCode.NotFound,
      "User with $id not found"
    )
    call.respond(note)
  }

  suspend fun update(call: ApplicationCall) {
    val payload = call.receive<NoteModel>()
    val note = noteRepository.update(payload)
    call.respond(note)
  }

  suspend fun delete(call: ApplicationCall) {
    UUID.fromString(call.parameters["id"]).let {
      noteRepository.delete(it)
    }
  }
}