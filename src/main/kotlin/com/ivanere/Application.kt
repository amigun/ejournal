package com.ivanere

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.ivanere.plugins.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val fio = varchar("fio", 100)
    val hash_pw = varchar("hash_pw", 255)
    val role = integer("role")
}

fun main() {
    Database.connect("jdbc:postgresql://127.0.0.1:5432/indb", driver = "org.postgresql.Driver",
        user = "inuser", password = "inpasswd")

    transaction {
        SchemaUtils.create(Users)
    }

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        configureSecurity()
    }.start(wait = true)
}
