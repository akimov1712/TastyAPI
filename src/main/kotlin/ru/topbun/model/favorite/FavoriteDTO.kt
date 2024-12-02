package ru.topbun.model.favorite

import kotlinx.serialization.Serializable
import ru.topbun.model.favorite.Favorites.bool
import ru.topbun.model.favorite.Favorites.integer

@Serializable
data class FavoriteDTO(
    val id: Int,
    val userId: Int,
    val recipeId: Int,
    val isFavorite: Boolean
)
