package ru.topbun.features.signUp

import ru.topbun.model.error.ErrorDTO
import ru.topbun.utills.Error
import io.ktor.http.*
import kotlinx.serialization.Serializable
import ru.topbun.utills.AppException

@Serializable
data class SignUpReceive(
    val email: String,
    val username: String,
    val password: String,
){

    fun isValid() = when{
        !Regex("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$").matches(email) -> throw AppException(HttpStatusCode.BadRequest, Error.INVALID_EMAIL)
        username.length < 4 -> throw AppException(HttpStatusCode.BadRequest, Error.USERNAME_LENGTH)
        password.length < 6 -> throw AppException(HttpStatusCode.BadRequest, Error.PASSWORD_LENGTH)
        else -> null
    }


}

@Serializable
data class SignUpResponse(
    val token: String
)