package ru.topbun.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import ru.topbun.utills.Const
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
import java.util.*

fun Application.configureSecurity() {
    authentication {
        jwt {
            realm = Const.Jwt.REALM
            verifier(
                JWT.require(Algorithm.HMAC256(Const.Jwt.SECRET))
                    .withAudience(Const.Jwt.AUDIENCE)
                    .withIssuer(Const.Jwt.ISSUER)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(Const.Jwt.AUDIENCE)) {
                    JWTPrincipal(credential.payload)
                } else null
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, ErrorDTO(HttpStatusCode.Unauthorized.value, Error.UNAUTHORIZED) )
            }
        }
    }
}