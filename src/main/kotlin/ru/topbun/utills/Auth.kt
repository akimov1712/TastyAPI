package ru.topbun.utills

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.RegisteredClaims.AUDIENCE
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import ru.topbun.model.user.UserDTO
import ru.topbun.model.user.Users

fun passwordVerify(receivePassword: String, hashPassword: String) = BCrypt.verifyer().verify(receivePassword.toCharArray(), hashPassword).verified
fun String.toPasswordHash() = BCrypt.withDefaults().hashToString(12, this.toCharArray())

fun generateToken(email: String): String{
    return JWT.create()
        .withAudience(Env["AUDIENCE"])
        .withIssuer(Env["ISSUER"])
        .withClaim(Env["KEY_EMAIL"], email)
        .sign(Algorithm.HMAC256(Env["SECRET"]))
}

fun JWTPrincipal?.getUsernameOrThrow() = this?.payload?.getClaim(Env["KEY_EMAIL"])?.asString() ?: throw AppException(HttpStatusCode.Unauthorized, Error.UNAUTHORIZED)

fun RoutingCall.getUserFromToken(): UserDTO{
    val principal = principal<JWTPrincipal>()
    val username = principal.getUsernameOrThrow()
    val user = Users.getUserOrThrow(username)
    return user
}