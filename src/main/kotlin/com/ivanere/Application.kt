package com.ivanere

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ivanere.models.User
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.ivanere.plugins.*
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.config.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val fio = varchar("fio", 100)
    val hash_pw = varchar("hash_pw", 255)
    val role = integer("role")

    fun toUser(row: ResultRow) : User =
        User(
            id = row[Users.id],
            fio = row[Users.fio],
            hash_pw = row[Users.hash_pw],
            role = row[Users.role]
        )
}

fun main() {
    Database.connect("jdbc:postgresql://127.0.0.1:5432/indb", driver = "org.postgresql.Driver",
        user = "inuser", password = "inpasswd")

    transaction {
        SchemaUtils.create(Users)
    }

    val config = HoconApplicationConfig(ConfigFactory.load())

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        configureSecurity()

        install(Authentication) {
            jwt("auth-jwt") {
                realm = config.property("jwt.realm").getString()

                verifier(JWT
                    .require(Algorithm.HMAC256(config.property("jwt.secret").getString()))
                    .withAudience(config.property("jwt.audience").getString())
                    .withIssuer(config.property("jwt.issuer").getString())
                    .build())

                validate { credential ->
                    if (credential.payload.getClaim("fio").asString() != "") {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                }

            }
        }
    }.start(wait = true)
}
