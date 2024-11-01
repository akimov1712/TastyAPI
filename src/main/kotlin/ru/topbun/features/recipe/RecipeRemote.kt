package ru.topbun.features.recipe

import kotlinx.serialization.Serializable

@Serializable
data class GetRecipeListReceive(
    val q: String = "",
    val offset: Int = 0,
    val limit: Int = 20,
)
