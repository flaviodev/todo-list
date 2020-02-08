import application.module
import application.web.controllers.requests.TodoItemRequest
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import domain.todo.entities.TodoItem
import domain.todo.types.Importance
import io.ktor.config.MapApplicationConfig
import io.ktor.http.*
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.netty.handler.codec.http.HttpHeaders.addHeader
import org.amshove.kluent.`should be`
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotBeNull
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.time.LocalDate

object GetTodosSpec : Spek({
    val todo = TodoItemRequest(
        title = "Add database processing",
        details = "Add backend support to this code",
        assignedTo = "kevin",
        dueDate = LocalDate.of(2020,12,18),
        importance = Importance.HIGH
    )

    describe("Get the Todos") {
        val engine = TestApplicationEngine()
        engine.start(wait = false)

        with(engine) {
            (environment.config as MapApplicationConfig).apply {
                put("ktor.environment", "test")
            }
        }

        engine.application.module(true)
        val mapper = jacksonObjectMapper()
            .registerModule(JavaTimeModule())

        with(engine) {
            it("should be OK to get the list of todos") {
                handleRequest(HttpMethod.Get, "/api/todo").apply {
                    response.status().`should be`(HttpStatusCode.OK)
                }
            }

            it("should get the todos") {
                with(handleRequest(HttpMethod.Get, "/api/todo")) {
                    response.content
                        .shouldNotBeNull()
                        .shouldContain("database")
                }
            }

            it("should create a todo") {
                with(handleRequest(HttpMethod.Post, "/api/todo") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(mapper.writeValueAsString(todo))
                }) {
                    response.status().`should be`(HttpStatusCode.Created)
                }
            }

            it("should update a todo") {
                with(handleRequest(HttpMethod.Put, "/api/todo/1") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(mapper.writeValueAsString(todo))
                }) {
                    response.status().`should be`(HttpStatusCode.NoContent)
                }
            }

            it("should delete a todo") {
                handleRequest(HttpMethod.Delete, "/api/todo/2").apply {
                    response.status().`should be`(HttpStatusCode.NoContent)
                }
            }

            it("should get a todo") {
                with(handleRequest(HttpMethod.Get, "/api/todo/1")) {
                    response.content
                        .shouldNotBeNull()
                        .shouldContain("database")
                }
            }

            it("should return error if the id is invalid") {
                with(handleRequest(HttpMethod.Get, "/api/todo/99")) {
                    response.status().`should be`(HttpStatusCode.NotFound)
                }
            }
        }
    }
})