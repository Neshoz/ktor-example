package com.example.user

import com.example.exception.AlreadyExistsException
import com.example.exception.InternalServerError
import com.example.exception.NotFoundException
import com.example.utils.AES
import java.util.UUID

class UserService(private val userRepository: UserRepository) {
  fun create(user: RegisterUser): User {
    userRepository.findByEmail(user.email).apply {
      require(this == null) {
        throw AlreadyExistsException("A user", "that email")
      }
    }
    return userRepository.create(
      user.copy(password = AES.encrypt(user.password))
    ) ?: throw InternalServerError()
  }

  fun getById(id: UUID): User {
    return userRepository.findById(id) ?: throw NotFoundException(id.toString())
  }

  fun getByEmail(email: String): User? {
    return userRepository.findByEmail(email)
  }

  fun update(user: User): User {
    return userRepository.update(user)
  }

  fun delete(id: UUID): Boolean {
    return userRepository.delete(id)
  }
}