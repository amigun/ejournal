package com.ivanere.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ivanere.models.LoginUser
import io.ktor.config.*
import java.util.*

class TokenManager(val config: HoconApplicationConfig) {
    fun generationJWTToken(user: LoginUser): String {
        val secret = config.property("jwt.secret").getString()
        val issuer = config.property("jwt.issuer").getString()

        val token = JWT.create()
            .withIssuer(issuer)
            .withClaim("username", user.fio)
            .withClaim("userId", user.id)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(secret))

        return token
    }
}