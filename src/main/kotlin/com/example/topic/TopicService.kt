package com.example.topic

import java.util.UUID

class TopicService(private val topicRepository: TopicRepository) {
  fun getAll(): List<Topic> {
    return topicRepository.findAll()
  }

  fun getById(id: UUID): Topic? {
    return topicRepository.findById(id)
  }

  fun create(topic: TopicWithoutId): Topic {
    return topicRepository.create(topic)
  }

  fun update(topic: Topic): Topic {
    return topicRepository.update(topic)
  }

  fun delete(id: UUID): Boolean {
    return topicRepository.delete(id)
  }
}