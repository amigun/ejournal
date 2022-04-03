package com.ivanere.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ivanere.models.LoginUser
import com.ivanere.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.util.*

fun Route.loginRouting() {
    val config = HoconApplicationConfig(ConfigFactory.load())

    route("/api/login") {
        post {
            val user = call.receive<LoginUser>()

            val token = JWT.create()
                .withAudience(config.property("jwt.audience").getString())
                .withIssuer(config.property("jwt.issuer").getString())
                .withClaim("fio", user.fio)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(config.property("jwt.secret").getString()))
            call.respond(hashMapOf("token" to token))
        }
    }
}

fun Application.LoginRoutes() {
    routing {
        loginRouting()
    }
}