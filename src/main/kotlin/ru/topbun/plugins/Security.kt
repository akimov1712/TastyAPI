package ru.topbun.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.sun.org.apache.bcel.internal.Const
import ru.topbun.utills.Error
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.h2.engine.User
import ru.topbun.model.error.ErrorDTO
import ru.topbun.utills.AppException
import ru.topbun.utills.Env
import java.util.*

fun Application.configureSecurity() {
    authentication {
        jwt {
            realm = Env["REALM"]
            verifier(
                JWT.require(Algorithm.HMAC256(Env["SECRET"]))
                    .withAudience(Env["AUDIENCE"])
                    .withIssuer(Env["ISSUER"])
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(Env["AUDIENCE"])) {
                    JWTPrincipal(credential.payload)
                } else null
            }
            challenge { _, _ ->
            }
        }
    }
}