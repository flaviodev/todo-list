package com.flaviodev

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.call
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
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
    route("/api/todo") {
        route("/", HttpMethod.Get) {
            accept(todoContentV1) {
                handle {
                    call.respond(todos)
                }
            }

            accept(todoContentV2) {
                handle {
                    call.respond(todos.map { t -> t.copy(importance = Importance.HIGH) })
                }
            }
        }

        route("{id}", HttpMethod.Get) {
            accept(todoContentV1) {
                handle {
                    val id = call.parameters["id"]

                    call.respond(todos.firstOrNull { it.id == id?.toIntOrNull() } ?: HttpStatusCode.NotFound)
                }
            }
        }

        route("/", HttpMethod.Post) {
            accept(todoContentV1) {
                handle {
                    val todo = call.receive<TodoItem>()

                    val newTodo = todo.copy(id = todos.last().id!! + 1)

                    todos.add(newTodo)

                    call.respond(HttpStatusCode.Created, newTodo)
                }
            }
        }

        route("{id}", HttpMethod.Put) {
            accept(todoContentV1) {
                handle {
                    val id = call.parameters["id"]?.toIntOrNull()

                    if (id == null)
                        call.respond(HttpStatusCode.BadRequest).also { return@handle }

                    todos.firstOrNull { it.id == id }?.apply {
                        val todo = call.receive<TodoItem>()
                        val newTodo = todo.copy(id = id)

                        todos.add(todos.indexOf(this), newTodo)
                        todos.remove(this)

                        call.respond(HttpStatusCode.NoContent)
                    } ?: call.respond(HttpStatusCode.NotFound)
                }
            }
        }

        route("{id}", HttpMethod.Delete) {
            accept(todoContentV1) {
                handle {
                    val id = call.parameters["id"]?.toIntOrNull()

                    if (id == null)
                        call.respond(HttpStatusCode.BadRequest).also { return@handle }

                    todos.firstOrNull { it.id == id }?.apply {
                        todos.remove(this)

                        call.respond(HttpStatusCode.NoContent)
                    } ?: call.respond(HttpStatusCode.NotFound)
                }
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