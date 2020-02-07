package application.web.routes

import application.web.controllers.TodoController
import application.web.controllers.todos
import com.fasterxml.jackson.databind.SerializationFeature
import domain.todo.entities.TodoItem
import io.ktor.application.call
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.accept
import io.ktor.routing.route

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
    val controller = TodoController()

    route("/api/todo") {
        route("/", HttpMethod.Get) {
            accept(todoContentV1) {
                handle {
                    call.respond(controller.getAll())
                }
            }

            accept(todoContentV2) {
                handle {
                    call.respond(controller.getAllVersion2())
                }
            }
        }

        route("{id}", HttpMethod.Get) {
            accept(todoContentV1) {
                handle {
                    call.respond(controller.get(context) ?: HttpStatusCode.NotFound)
                }
            }
        }

        route("/", HttpMethod.Post) {
            accept(todoContentV1) {
                handle {
                    call.respond(HttpStatusCode.Created, controller.create(context))
                }
            }
        }

        route("{id}", HttpMethod.Put) {
            accept(todoContentV1) {
                handle {
                    call.respond(controller.update(context))
                }
            }
        }

        route("{id}", HttpMethod.Delete) {
            accept(todoContentV1) {
                handle {
                    call.respond(controller.delete(context))
                }
            }
        }
    }
}