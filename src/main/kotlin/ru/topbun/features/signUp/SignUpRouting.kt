package ru.topbun.features.signUp

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureSignUpRouting(){
    routing {
        post("/signUp") {
            val signUpController = SignUpController(call)
            signUpController.signUp()
        }
    }
}