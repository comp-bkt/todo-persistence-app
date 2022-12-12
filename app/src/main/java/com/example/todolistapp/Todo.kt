package com.example.todolistapp

import java.util.*

data class Todo (
    var id: UUID = UUID.randomUUID(),
    var title: String? = null,
    var detail: String? = null,
    var date: Date = Date(),
    var isComplete:Boolean = false) {

    init {
        date = Date()
    }

    val photoFilename: String
        get() = "img_$id.jpg"
}