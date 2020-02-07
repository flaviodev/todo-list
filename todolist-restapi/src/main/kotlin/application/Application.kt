package application

import com.fasterxml.jackson.databind.SerializationFeature
import application.web.routes.todoApi
import application.web.routes.todoContentVersions
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.response.respondText
import io.ktor.routing.Routing

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(Routing) {
        trace { application.log.trace(it.buildText()) }
        todoApi()
    }

    install(StatusPages) {
        this.exception<Throwable> { e ->
            call.respondText(e.localizedMessage, ContentType.Text.Plain)
            throw e
        }
    }

    install(ContentNegotiation) {
        todoContentVersions()

        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
}