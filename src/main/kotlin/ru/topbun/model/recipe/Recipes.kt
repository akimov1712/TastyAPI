package ru.topbun.model.recipe

import io.ktor.http.*
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.features.recipe.AddRecipeReceive
import ru.topbun.model.category.Categories
import ru.topbun.model.favorite.Favorites
import ru.topbun.model.ingredients.Ingredients
import ru.topbun.model.recipe.Recipes.difficulty
import ru.topbun.model.recipeToCategory.RecipeToCategories
import ru.topbun.model.step.Steps
import ru.topbun.model.step.Steps.references
import ru.topbun.model.user.Users
import ru.topbun.utills.AppException
import ru.topbun.utills.Error

object Recipes: IntIdTable("recipes") {

    val userId = integer("user_id").references(Users.id).nullable()
    val title = text("title")
    val description = text("description").nullable()
    val image = text("image").nullable()
    val timeCooking = integer("time_cooking").nullable()
    val difficulty = text("difficulty").nullable().default(null)
    val carbs = integer("carbs").nullable()
    val fat = integer("fat").nullable()
    val protein = integer("protein").nullable()
    val kcal = integer("kcal").nullable()

    fun deleteRecipe(id: Int) = transaction {
        Favorites.deleteFavorites(id)
        Steps.deleteSteps(id)
        Ingredients.deleteIngr(id)
        RecipeToCategories.deleteRecipeToCategories(id)
        Recipes.deleteWhere { Recipes.id eq id }
    }

    fun addRecipe(recipe: RecipeDTO) = transaction {
        insert {
            it[id] = recipe.id
            it[userId] = recipe.userId
            it[title] = recipe.title
            it[description] = recipe.description
            it[image] = recipe.image
            it[timeCooking] = recipe.timeCooking
            it[difficulty] = recipe.difficulty?.name
            it[carbs] = recipe.carbs
            it[fat] = recipe.fat
            it[protein] = recipe.protein
            it[kcal] = recipe.kcal
        }
    }

    fun insertRecipe(recipe: AddRecipeReceive, userId: Int?) = transaction {
        val id = insert {
            it[Recipes.title] = recipe.title
            it[Recipes.userId] = userId
            it[Recipes.title] = recipe.title
            it[Recipes.description] = recipe.description
            it[Recipes.image] = recipe.image
            it[Recipes.timeCooking] = recipe.timeCooking
            it[Recipes.difficulty] = recipe.difficulty?.name
            it[Recipes.carbs] = recipe.carbs
            it[Recipes.fat] = recipe.fat
            it[Recipes.protein] = recipe.protein
            it[Recipes.kcal] = recipe.kcal
        }[Recipes.id].value
        RecipeToCategories.insertRecipeToCategory(recipeId = id, categoriesId = recipe.categories)
        Ingredients.insertIngredients(id, recipe.ingredients)
        Steps.insertSteps(id, recipe.steps)
        return@transaction id
    }

    fun getRecipes(q: String, offset: Int, limit: Int, userId: Int? = null, myRecipe: Boolean = false) = transaction {
        selectAll().limit(n = limit, offset = offset.toLong()).where{
            (title.lowerCase() like "%${q.lowercase()}%") and (if (userId != null && myRecipe) Recipes.userId eq userId else Op.TRUE)
        }.map { it.toRecipe(userId) }
    }

    fun getRecipeWithCategory(categoryId: Int, offset: Int, limit: Int, userId: Int? = null) = transaction {
        val recipeIds = RecipeToCategories.selectAll().limit(n = limit, offset = offset.toLong())
            .where { RecipeToCategories.categoryId eq categoryId }.map { it[RecipeToCategories.recipeId] }
        recipeIds.map { getRecipeWithId(it, userId) }
    }

    fun getRecipeWithId(recipeId: Int, userId: Int? = null) = transaction {
        selectAll().where { Recipes.id eq recipeId }.firstOrNull()?.toRecipe(userId)
            ?: throw AppException(HttpStatusCode.NotFound, Error.RECIPE_NOT_FOUND)
    }

    fun ResultRow.toRecipe(userId: Int?): RecipeDTO{
        val recipeId = this[Recipes.id].value
        val categories = Categories.getCategoriesWithRecipe(recipeId)
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
            categories = categories,
            timeCooking = this[Recipes.timeCooking],
            difficulty = this[difficulty]?.let { DifficultyType.valueOf(it) },
            carbs = this[Recipes.carbs],
            fat = this[Recipes.fat],
            protein = this[Recipes.protein],
            kcal = this[Recipes.kcal],
            ingredients = ingredients,
            steps = steps
        )
    }

}