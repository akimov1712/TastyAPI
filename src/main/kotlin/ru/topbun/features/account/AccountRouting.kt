package ru.topbun.features.account

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureAccountRouting(){
    routing {
        authenticate {
            get("/account") {
                val accountController = AccountController(call)
                accountController.accountInfo()
            }
        }
    }
}