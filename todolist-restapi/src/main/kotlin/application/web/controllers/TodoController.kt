package application.web.controllers

import application.web.requests.TodoItemRequest
import application.web.responses.TodoItemResponse
import domain.todo.TodoService
import domain.todo.entities.Importance
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive

class TodoController {
    val service = TodoService()

    fun getAll(): List<TodoItemResponse> {
        return service.getAll().map { TodoItemResponse(it) }
    }

    fun getAllVersion2(): List<TodoItemResponse> {
        return service.getAll().map { t -> t.copy(importance = Importance.HIGH) }.map { TodoItemResponse(it) }
    }

    fun get(call: ApplicationCall): TodoItemResponse? {
        val id = call.parameters["id"]?.toIntOrNull() ?: return null

        return service.get(id)?.let { TodoItemResponse(it) }
    }

    suspend fun create(call: ApplicationCall): TodoItemResponse {
        val todo = call.receive<TodoItemRequest>()

        val nextIndex = service.getAll().last().id + 1
        val newTodo = todo.toTodoItem(nextIndex)

        service.create(newTodo)

        return TodoItemResponse(newTodo)
    }

    suspend fun update(call: ApplicationCall): HttpStatusCode {
        val id = call.parameters["id"]?.toIntOrNull() ?: return HttpStatusCode.BadRequest

        return service.get(id)?.run {
            val todo = call.receive<TodoItemRequest>()
            val newTodo = todo.toTodoItem(id)

            service.update(newTodo)

            HttpStatusCode.NoContent
        } ?: HttpStatusCode.NotFound
    }

    fun delete(call: ApplicationCall): HttpStatusCode {
        val id = call.parameters["id"]?.toIntOrNull() ?: return HttpStatusCode.BadRequest

        return service.get(id)?.run {
            service.delete(id)

            HttpStatusCode.NoContent
        } ?: HttpStatusCode.NotFound
    }
}
