package com.example.utils

import io.ktor.server.application.*

fun getParam(
  param: String,
  call: ApplicationCall
): String {
  return call.parameters[param] ?: throw IllegalArgumentException("Missing ID in path")
}