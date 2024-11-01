package ru.topbun.features.signUp

import ru.topbun.model.user.Users
import ru.topbun.utills.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class SignUpController(
    private val call: RoutingCall
) {

    suspend fun signUp(){
        call.wrapperException{
            val signUp = call.receive<SignUpReceive>()
            val userIsFound = Users.containsUser(signUp.email)
            if (userIsFound) throw AppException(HttpStatusCode.Conflict, Error.USER_EXISTS)
            signUp.isValid()
            val passwordHash = signUp.password.toPasswordHash()
            Users.insertUser(
                username = signUp.username,
                email = signUp.email,
                password = passwordHash
            )
            call.respond(SignUpResponse(token = generateToken(signUp.email)))
        }
    }

}