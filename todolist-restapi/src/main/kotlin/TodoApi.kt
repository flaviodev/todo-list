package com.flaviodev

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import java.time.LocalDate

fun Routing.todoApi() {
    route("/api/todo") {
        get("/") {
            call.respond(todos)
        }
    }
}

val todo1 = TodoItem(
    title = "Add RestAPI Data Access",
    details = "Add database support",
    assignedTo = "Me",
    dueDate = LocalDate.of(2020, 12, 18),
    importance = Importance.HIGH
)

val todo2 = TodoItem(
    title = "Add RestAPI Service",
    details = "Add a service to get the data",
    assignedTo = "Me",
    dueDate = LocalDate.of(2020, 12, 18),
    importance = Importance.MEDIUM
)

val todos = listOf(todo1, todo2)