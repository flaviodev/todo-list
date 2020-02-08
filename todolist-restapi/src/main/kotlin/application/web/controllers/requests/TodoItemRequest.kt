package application.web.controllers.requests

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import domain.todo.entities.TodoItem
import domain.todo.types.Importance
import java.time.LocalDate

data class TodoItemRequest(
    val title: String,
    val details: String,
    val assignedTo: String,
    @JsonDeserialize(using = LocalDateDeserializer::class)
    val dueDate: LocalDate,
    val importance: Importance
) {
    fun toTodoItem(id: Int) = TodoItem(
        id = id,
        title = title,
        details = details,
        assignedTo = assignedTo,
        dueDate = dueDate,
        importance = importance
    )
}
