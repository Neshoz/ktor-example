package com.example.note

import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Routing.notes(noteController: NoteController = NoteController()) {
  authenticate {
    route("notes") {
      post { noteController.create(this.context) }
      get { noteController.getUserNotes(this.context) }

      route("{id}") {
        get { noteController.findById(this.context) }
        put { noteController.update(this.context) }
        delete { noteController.delete(this.context) }

        route("invite/{userId}") {
          post { noteController.addUserToNote(this.context) }
        }
      }
    }
  }
}