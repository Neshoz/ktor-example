package com.example.note

import com.example.utils.userId
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

  suspend fun findById(call: ApplicationCall) {
    val id = UUID.fromString(call.parameters["id"])
    val note = noteRepository.findById(id) ?: throw NoSuchElementException()
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