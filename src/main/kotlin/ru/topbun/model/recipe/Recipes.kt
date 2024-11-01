package ru.topbun.model.recipe

import io.ktor.http.*
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.model.category.Categories
import ru.topbun.model.favorite.Favorites
import ru.topbun.model.ingredients.Ingredients
import ru.topbun.model.ingredients.Ingredients.getIngredientsFromId
import ru.topbun.model.recipe.Recipes.difficulty
import ru.topbun.model.recipeToCategory.RecipeToCategories
import ru.topbun.model.recipeToCategory.RecipeToCategories.categoryId
import ru.topbun.model.recipeToCategory.RecipeToCategories.recipeId
import ru.topbun.model.step.Steps
import ru.topbun.utills.AppException
import ru.topbun.utills.Error

object Recipes: IntIdTable("Recipes") {

    val userId = integer("user_id").nullable()
    val title = text("title")
    val description = text("description").nullable()
    val image = text("image").nullable()
    val timeCooking = integer("time_cooking").nullable()
    val difficulty = text("difficulty")
    val carbs = integer("carbs").nullable()
    val fat = integer("fat").nullable()
    val protein = integer("protein").nullable()
    val kcal = integer("kcal").nullable()

    fun insertRecipe(recipe: RecipeDTO, userId: Int?) = transaction {
        insert {
            it[Recipes.title] = recipe.title
            it[Recipes.userId] = userId
            it[Recipes.title] = recipe.title
            it[Recipes.description] = recipe.description
            it[Recipes.image] = recipe.image
            it[Recipes.timeCooking] = recipe.timeCooking
            it[Recipes.difficulty] = recipe.difficulty.name
            it[Recipes.carbs] = recipe.carbs
            it[Recipes.fat] = recipe.fat
            it[Recipes.protein] = recipe.protein
            it[Recipes.kcal] = recipe.kcal
        }[Recipes.id].value
    }

    fun getRecipes(q: String, offset: Int, limit: Int, userId: Int? = null) = transaction {
        val recipeRows = selectAll().limit(n = limit, offset = offset.toLong()).where{ title.lowerCase() like "%${q.lowercase()}%" }
        userId?.let { recipeRows.where { Recipes.userId eq userId }.map { it.toRecipe(userId) }
        } ?: recipeRows.map { it.toRecipe(userId) }
    }

    fun getRecipeWithCategory(categoryId: Int) = transaction {
        val recipeIds = RecipeToCategories.selectAll().where { RecipeToCategories.categoryId eq categoryId }.map { it[RecipeToCategories.recipeId] }
        recipeIds.map { getRecipeWithId(it) }
    }

    fun getRecipeWithId(recipeId: Int) = transaction {
        selectAll().where { Recipes.id eq recipeId }.firstOrNull()?.toRecipe(null)
            ?: throw AppException(HttpStatusCode.NotFound, Error.RECIPE_NOT_FOUND)
    }

    fun ResultRow.toRecipe(userId: Int?): RecipeDTO{
        val recipeId = this[Recipes.id].value
        val categories = Categories.getCategoriesWithRecipe(recipeId).map { it.id }
        val ingredients = Ingredients.getIngredientsFromId(recipeId)
        val steps = Steps.getStepsWithRecipe(recipeId)
        val isFavorite = userId?.let { Favorites.isFavorite(userId, recipeId) } ?: false
        return RecipeDTO(
            id = recipeId,
            userId = this[Recipes.userId],
            title = this[Recipes.title],
            description = this[Recipes.description],
            image = this[Recipes.image],
            isFavorite = isFavorite,
            categoryId = categories,
            timeCooking = this[Recipes.timeCooking],
            difficulty = DifficultyType.valueOf(this[difficulty]),
            carbs = this[Recipes.carbs],
            fat = this[Recipes.fat],
            protein = this[Recipes.protein],
            kcal = this[Recipes.kcal],
            ingredients = ingredients,
            steps = steps
        )
    }

}