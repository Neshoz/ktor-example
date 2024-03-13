package com.example.user

import com.example.exception.AlreadyExistsException
import com.example.exception.InternalServerError
import com.example.exception.NotFoundException
import com.example.utils.AES
import java.util.UUID

class UserService(private val userRepository: UserRepository) {
  suspend fun create(user: CreateUserPayload): UserDTO {
    userRepository.findByEmail(user.email).apply {
      require(this == null) {
        throw AlreadyExistsException("User", "email")
      }
    }
    return userRepository.create(
      user.copy(password = AES.encrypt(user.password))
    ) ?: throw InternalServerError()
  }

  suspend fun getById(id: UUID): UserDTO {
    return userRepository.findById(id) ?: throw NotFoundException(id.toString())
  }

  suspend fun getByEmail(email: String): User? {
    return userRepository.findByEmail(email)
  }

  suspend fun update(user: User): User {
    return userRepository.update(user)
  }

  suspend fun delete(id: UUID): Boolean {
    return userRepository.delete(id)
  }
}