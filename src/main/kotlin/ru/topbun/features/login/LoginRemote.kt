package ru.topbun.features.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginReceive(
    val email: String,
    val password: String,
)

@Serializable
data class LoginResponse(
    val token: String,
)