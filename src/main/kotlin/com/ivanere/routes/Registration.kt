package com.ivanere.routes

import com.ivanere.Users
import com.ivanere.models.User
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import javax.swing.text.StyledEditorKit.BoldAction

fun Route.registrationRouting() {
    route("/api/registration") {
        get {
            call.respond(
                transaction {
                    Users.selectAll().map {Users.toUser(it)}
                }
            )
        }
        post {
            val user = call.receive<User>()

            val users = mutableListOf<String>()

            transaction {
                Users.select() {Users.fio eq user.fio}.forEach {
                    users.add(it[Users.fio])
                }
            }

            if (users.isEmpty()) {
                transaction {
                    Users.insert {
                        it[Users.fio] = user.fio
                        it[Users.hash_pw] = user.hash_pw
                        it[Users.role] = user.role
                    }
                }
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}

fun Application.RegistrationRoutes() {
    routing {
        registrationRouting()
    }
}