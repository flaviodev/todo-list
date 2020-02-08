package web

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}

@SuppressWarnings("unused")
fun Application.module() {
    install(StatusPages) {
        when {
            isDev -> {
                this.exception<Throwable> {e ->
                    call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
                    throw e
                }
            }

            isTest -> {
                this.exception<Throwable> {e ->
                    call.response.status(HttpStatusCode.InternalServerError)
                    throw e
                }
            }

            isProd -> {
                this.exception<Throwable> {e ->
                    call.respondText(e.localizedMessage, ContentType.Text.Plain, HttpStatusCode.InternalServerError)
                    throw e
                }
            }

        }
    }

    install(Routing) {
        if(isDev) trace {
            application.log.trace(it.buildText())
        }
    }
}

val Application.envKind get() = environment.config.property("ktor.enviroment").getString()
val Application.isDev get() = envKind == "dev"
val Application.isTest get() = envKind == "test"
val Application.isProd get() = envKind != "dev" && envKind != "test"

