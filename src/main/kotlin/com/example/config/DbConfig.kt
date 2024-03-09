package com.example.config

import org.jetbrains.exposed.sql.Database

object DbConfig {
  fun setup() {
    val driverClassName = "org.h2.Driver"
    val jdbcUrl = "jdbc:h2:file:./build/db"
    Database.connect(jdbcUrl, driverClassName)
  }
}