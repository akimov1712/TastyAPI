package ru.topbun.features.favorite

import io.ktor.http.*
import kotlinx.serialization.Serializable
import ru.topbun.model.category.CategoryDTO
import ru.topbun.utills.AppException
import ru.topbun.utills.Error

@Serializable
data class FavoriteReceive(
    val isFavorite: Boolean
)

@Serializable
data class FavoriteResponse(
    val recipeId: Int,
    val isFavorite: Boolean
)

