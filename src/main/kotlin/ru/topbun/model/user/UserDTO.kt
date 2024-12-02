package ru.topbun.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val isAdmin: Boolean
)
