package model

import java.time.LocalDate

data class TodoItem(
    val id: Int,
    val title: String,
    val details: String,
    val assignedTo: String,
    val dueDate: LocalDate,
    val importance: String
)