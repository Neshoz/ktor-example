package com.example.utils

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

const val ALGORITHM = "AES/CBC/PKCS5Padding"
val SECRET_KEY = System.getenv()[("CIPHER_KEY")] ?: "1234567890123456"

val key = SecretKeySpec(SECRET_KEY.toByteArray(), "AES")
val iv = IvParameterSpec(ByteArray(16))

object AES {
  fun decrypt(value: String): String {
    val cipher = Cipher.getInstance(ALGORITHM)
    cipher.init(Cipher.DECRYPT_MODE, key, iv)
    val plainText = cipher.doFinal(Base64.getDecoder().decode(value))
    return String(plainText)
  }

  fun encrypt(value: String): String {
    val cipher = Cipher.getInstance(ALGORITHM)
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)
    val cipherText = cipher.doFinal(value.toByteArray())
    return Base64.getEncoder().encodeToString(cipherText)
  }
}
