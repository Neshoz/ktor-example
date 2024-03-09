package com.example.extensions

import io.konform.validation.Invalid
import io.konform.validation.Validation

fun <T> Validation<T>.validateAndThrowOnFailure(value: T) {
  val result = validate(value)
  if (result is Invalid<T>) {
    throw IllegalArgumentException(result.errors.toString())
  }
}