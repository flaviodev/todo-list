package com.flaviodev

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.call
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import java.time.LocalDate

val todoContentV1 = ContentType("application", "vnd.todoapi.v1+json")
val todoContentV2 = ContentType("application", "vnd.todoapi.v2+json")

fun ContentNegotiation.Configuration.todoContentVersions() {
    jackson(todoContentV1) {
        enable(SerializationFeature.INDENT_OUTPUT)
        disableDefaultTyping()
    }

    jackson(todoContentV2) {
        enable(SerializationFeature.INDENT_OUTPUT)
        disableDefaultTyping()
    }
}

fun Routing.todoApi() {
    route("/api") {
        accept(todoContentV1) {
            get("/todo") {
                call.respond(todos)
            }
        }

        accept(todoContentV2) {
            get("/todo") {
                call.respond(todos.map { t -> t.copy(importance = Importance.HIGH) })
            }
        }

        accept(todoContentV1) {
            get("/todo/{id}") {
                val id = call.parameters["id"]

                call.respond(todos.firstOrNull { it.id == id?.toIntOrNull() } ?: HttpStatusCode.NotFound)
            }
        }

        accept(todoContentV1) {
            post("/todo") {
                val todo = call.receive<TodoItem>()

                val newTodo = todo.copy(id = todos.last().id!! + 1)

                todos.add(newTodo)

                call.respond(HttpStatusCode.Created, newTodo)
            }
        }

        accept(todoContentV1) {
            put("/todo/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id == null)
                    call.respond(HttpStatusCode.BadRequest).also { return@put }

                todos.firstOrNull { it.id == id }?.apply {
                    val todo = call.receive<TodoItem>()
                    val newTodo = todo.copy(id = id)

                    todos.add(todos.indexOf(this), newTodo)
                    todos.remove(this)

                    call.respond(HttpStatusCode.NoContent)
                } ?: call.respond(HttpStatusCode.NotFound)
            }
        }

        accept(todoContentV1) {
            delete("/todo/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id == null)
                    call.respond(HttpStatusCode.BadRequest).also { return@delete }

                todos.firstOrNull { it.id == id }?.apply {
                    todos.remove(this)

                    call.respond(HttpStatusCode.NoContent)
                } ?: call.respond(HttpStatusCode.NotFound)
            }
        }
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

val todos = mutableListOf(todo1, todo2)