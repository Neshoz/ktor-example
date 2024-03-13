package com.example.database

import com.example.auth.SessionsTable
import com.example.note.NotesTable
import com.example.user.UsersTable
import com.example.users_notes.UsersNotesTable
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {
  fun init(config: ApplicationConfig) {
    val driverClassName = config.property("ktor.storage.driverClassName").getString()
    val jdbcUrl = config.property("ktor.storage.jdbcUrl").getString()
    val database = Database.connect(jdbcUrl, driverClassName)
    transaction(database) {
      SchemaUtils.create(SessionsTable)
      SchemaUtils.create(NotesTable)
      SchemaUtils.create(UsersTable)
      SchemaUtils.create(UsersNotesTable)
    }
  }

  suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }
}