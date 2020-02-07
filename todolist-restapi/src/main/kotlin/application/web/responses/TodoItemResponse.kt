package application.web.responses

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import domain.todo.entities.Importance
import domain.todo.entities.TodoItem
import java.time.LocalDate

data class TodoItemResponse(
    val id: Int,
    val title: String,
    val details: String,
    val assignedTo: String,
    @JsonSerialize(using = ToStringSerializer::class)
    val dueDate: LocalDate,
    val importance: Importance
) {
    constructor(todoItem: TodoItem) : this(
        id = todoItem.id,
        title = todoItem.title,
        details = todoItem.details,
        assignedTo = todoItem.assignedTo,
        dueDate = todoItem.dueDate,
        importance = todoItem.importance
    )
}


