package com.example.firstapp
import java.util.Date

data class Task(
    val id: Int,
    var title: String,
    var description: String,
    var dueDate: Date,
    var priority_level: String,
    var completion_status: Boolean
)