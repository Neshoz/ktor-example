package com.example.exception

class NotFoundException(id: String) : Exception("Entity with identifier $id not found")