package ru.topbun.features.account

import ru.topbun.model.user.Users
import ru.topbun.utills.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.topbun.features.signUp.SignUpReceive
import ru.topbun.features.signUp.SignUpResponse

class AccountController(
    private val call: RoutingCall
) {

    suspend fun accountInfo(){
        call.wrapperException{
            val user = call.getUserFromToken()
            call.respond(user)
        }
    }

}