package ru.topbun.model.favorite

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.model.category.Categories.image
import ru.topbun.model.category.Categories.name
import ru.topbun.model.category.CategoryDTO
import ru.topbun.model.recipe.RecipeDTO
import ru.topbun.model.recipe.Recipes
import ru.topbun.model.recipeToCategory.RecipeToCategories.references
import ru.topbun.model.step.Steps
import ru.topbun.model.user.Users

object Favorites: IntIdTable("favorites") {

    val userId = integer("user_id").references(Users.id)
    val recipeId = integer("recipe_id").references(Recipes.id)
    val isFavorite = bool("is_favorite")

    fun deleteFavorites(recipeId: Int) = transaction {
        Favorites.deleteWhere { Favorites.recipeId eq recipeId }
    }

    fun ResultRow.toFavorite(): FavoriteDTO {
        return FavoriteDTO(
            id = this[id].value,
            userId = this[userId],
            recipeId = this[recipeId],
            isFavorite = this[isFavorite]
        )
    }

    fun addOrUpdateFavorite(userId: Int, recipeId: Int, favoriteStatus: Boolean) {
        transaction {
            val favorite = Favorites
                .selectAll()
                .where { (Favorites.userId eq userId) and (Favorites.recipeId eq recipeId) }
                .firstOrNull()

            if (favorite == null) {
                Favorites.insert {
                    it[Favorites.userId] = userId
                    it[Favorites.recipeId] = recipeId
                    it[Favorites.isFavorite] = favoriteStatus
                }
            } else {
                Favorites.update({ Favorites.id eq favorite[Favorites.id] }) {
                    it[Favorites.isFavorite] = favoriteStatus
                }
            }
        }
    }

    fun addFavorite(favorite: FavoriteDTO) {
        transaction {
            Favorites.insert {
                it[Favorites.id] = favorite.id
                it[Favorites.userId] = favorite.userId
                it[Favorites.recipeId] = favorite.recipeId
                it[Favorites.isFavorite] = favorite.isFavorite
            }
        }
    }


    fun getFavorites(userId: Int) : List<RecipeDTO> = transaction{
        val recipeIds = Favorites.selectAll().where{ (Favorites.userId eq userId) and (Favorites.isFavorite eq true)}.map { it[recipeId] }
        recipeIds.map { Recipes.getRecipeWithId(it, userId) }
    }

    fun isFavorite(userId: Int, recipeId: Int): Boolean {
        return transaction {
            Favorites.selectAll().where{ (Favorites.userId eq userId) and (Favorites.recipeId eq recipeId) }
                .firstOrNull()?.get(Favorites.isFavorite) ?: false
        }
    }

}