package com.ivanere.plugins

import com.ivanere.routes.LoginRoutes
import com.ivanere.routes.RegistrationRoutes
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {

    routing {
        try {
            authenticate("auth-jwt") {
                get("/") {
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                    call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
                        //call.respondText("Hello, World!")
                }
            }
        } catch (e:Exception) {
            println(e.message)
        }

    }

    RegistrationRoutes()
    LoginRoutes()
}
