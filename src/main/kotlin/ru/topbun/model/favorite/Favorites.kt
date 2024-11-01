package ru.topbun.model.favorite

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.model.recipe.Recipes

object Favorites: IntIdTable("favorites") {

    val userId = integer("user_id")
    val recipeId = integer("recipe_id")
    val isFavorite = bool("is_favorite").default(false)

    fun addOrUpdateFavorite(userId: Int, recipeId: Int, favoriteStatus: Boolean) {
        transaction {
            val existingFavorite = Favorites.select {
                (Favorites.userId eq userId) and (Favorites.recipeId eq recipeId)
            }.singleOrNull()

            if (existingFavorite != null) {
                Favorites.update({ (Favorites.userId eq userId) and (Favorites.recipeId eq recipeId) }) {
                    it[isFavorite] = favoriteStatus
                }
            } else {
                Favorites.insert {
                    it[Favorites.userId] = userId
                    it[Favorites.recipeId] = recipeId
                    it[isFavorite] = favoriteStatus
                }
            }
        }
    }

    fun isFavorite(userId: Int, recipeId: Int): Boolean {
        return transaction {
            Favorites
                .select { (Favorites.userId eq userId) and (Favorites.recipeId eq recipeId) }
                .singleOrNull()?.get(Favorites.isFavorite) ?: false
        }
    }

}