package com.example.note

import com.example.utils.DateTimeSerializer
import com.example.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class NoteModel(
  @Serializable(with = UUIDSerializer::class)
  val id: UUID,
  @Serializable(with = UUIDSerializer::class)
  val createdBy: UUID,
  val contents: String,
  @Serializable(with = DateTimeSerializer::class)
  val created: LocalDateTime,
  @Serializable(with = DateTimeSerializer::class)
  val modified: LocalDateTime
)

@Serializable
data class CreateNotePayload(
  val contents: String,
)