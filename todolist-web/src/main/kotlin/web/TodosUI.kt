package web

import UserSession
import io.ktor.application.call
import io.ktor.mustache.MustacheContent
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import model.TodoItem
import shared.User
import viewmodels.TodoVM
import java.time.LocalDate

val todo = TodoItem(
    1,
    "Add database processing",
    "Add backend support to this code",
    "Kevin",
    LocalDate.of(2018, 12, 18),
    "HIGH"
)

var todos = listOf(todo, todo)

fun Routing.todos() {

    get("/todos") {
        when {
            call.sessions.get<UserSession>() == null -> call.sessions.set(
                UserSession(
                    name = "John"
                )
            )
        }

        todos = listOf(todo, todo)

        val todoVM = TodoVM(todos, User("Kevin Smith"))

        // getClientCredential("http://localhost:5000/connect/token", "todolistClient", "superSecretPassword", listOf("todolistAPI.read", "todolistAPI.write"))
        call.respond(
            MustacheContent("todos.hbs", mapOf("todos" to todoVM))
        )
    }
}


