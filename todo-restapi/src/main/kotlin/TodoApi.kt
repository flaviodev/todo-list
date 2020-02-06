package com.flaviodev

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route

fun Routing.todoApi() {
    route("/api/todo") {
        get("/") {
            call.respond(todos)
        }
    }
}