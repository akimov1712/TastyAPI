package ru.topbun.model.user

data class UserDTO(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val isAdmin: Boolean
)
