package com.example.exception

class AlreadyExistsException(
  entity: String,
  identifier: String
) : Exception("$entity with $identifier already exists")