package ru.topbun.model.error

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDTO(
    val code: Int,
    val message: String,
)
