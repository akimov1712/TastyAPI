package ru.topbun.model.ingredients

import kotlinx.serialization.Serializable

@Serializable
data class IngredientsDTO(
    @Transient val id: Int = 0,
    val name: String,
    val value: String,
)
