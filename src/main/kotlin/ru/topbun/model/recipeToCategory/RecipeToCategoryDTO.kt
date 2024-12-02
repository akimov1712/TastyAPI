package ru.topbun.model.recipeToCategory

import kotlinx.serialization.Serializable
import ru.topbun.model.favorite.Favorites.bool
import ru.topbun.model.favorite.Favorites.integer

@Serializable
data class RecipeToCategoryDTO(
    val id: Int,
    val recipeId: Int,
    val categoryId: Int,
)
