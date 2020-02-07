package application.web.controllers

import domain.todo.entities.Importance
import domain.todo.entities.TodoItem
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import java.time.LocalDate

class TodoController {

    fun getAll(): List<TodoItem> {
        return todos
    }

    fun getAllVersion2(): List<TodoItem> {
        return todos.map { t -> t.copy(importance = Importance.HIGH) }
    }

    fun get(call: ApplicationCall): TodoItem? {
        val id = call.parameters["id"]

        return todos.firstOrNull { it.id == id?.toIntOrNull() }
    }

    suspend fun create(call: ApplicationCall): TodoItem {
        val todo = call.receive<TodoItem>()

        val newTodo = todo.copy(id = todos.last().id!! + 1)

        todos.add(newTodo)

        return newTodo
    }

    suspend fun update(call: ApplicationCall): HttpStatusCode {
        val id = call.parameters["id"]?.toIntOrNull() ?: return HttpStatusCode.BadRequest

        return todos.firstOrNull { it.id == id }?.run {
            val todo = call.receive<TodoItem>()
            val newTodo = todo.copy(id = id)

            todos.add(todos.indexOf(this), newTodo)
            todos.remove(this)

            HttpStatusCode.NoContent
        } ?: HttpStatusCode.NotFound
    }

    fun delete(call: ApplicationCall): HttpStatusCode {
        val id = call.parameters["id"]?.toIntOrNull() ?: return HttpStatusCode.BadRequest

        return todos.firstOrNull { it.id == id }?.run {
            todos.remove(this)

            HttpStatusCode.NoContent
        } ?: HttpStatusCode.NotFound
    }
}

val todo1 = TodoItem(
    id = 1,
    title = "Add RestAPI Data Access",
    details = "Add database support",
    assignedTo = "Me",
    dueDate = LocalDate.of(2020, 12, 18),
    importance = Importance.HIGH
)

val todo2 = TodoItem(
    id = 2,
    title = "Add RestAPI Service",
    details = "Add a service to get the data",
    assignedTo = "Me",
    dueDate = LocalDate.of(2020, 12, 18),
    importance = Importance.MEDIUM
)

val todos = mutableListOf(
    todo1,
    todo2
)