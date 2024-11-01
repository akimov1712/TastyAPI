package ru.topbun.features.recipe

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.topbun.model.ingredients.Ingredients
import ru.topbun.model.recipe.RecipeDTO
import ru.topbun.model.recipe.Recipes
import ru.topbun.model.step.Steps
import ru.topbun.utills.AppException
import ru.topbun.utills.Error
import ru.topbun.utills.getUserFromToken
import ru.topbun.utills.wrapperException

class RecipeController(
    val call: RoutingCall
) {

    suspend fun addRecipe(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val userId = if (call.getUserFromToken().isAdmin) null else user.id
            val recipeReceive = call.receive<RecipeDTO>()
            recipeReceive.isValid()
            val recipeId = Recipes.insertRecipe(recipeReceive, userId)
            Ingredients.insertIngredients(recipeId, recipeReceive.ingredients)
            Steps.insertSteps(recipeId, recipeReceive.steps)
            val recipe = Recipes.getRecipeWithId(recipeId)
            call.respond(recipe)
        }
    }

    suspend fun getRecipeWithCategory(){
        call.wrapperException {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, Error.PARAMS_ID)
            val recipe = Recipes.getRecipeWithCategory(id)
            call.respond(recipe)
        }
    }

    suspend fun getRecipeWithId(){
        call.wrapperException {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, Error.PARAMS_ID)
            val recipe = Recipes.getRecipeWithId(id)
            call.respond(recipe)
        }
    }

    suspend fun getRecipeList(){
        call.wrapperException {
            getRecipes()
        }
    }

    suspend fun getMyRecipeList(){
        call.wrapperException {
            val user = call.getUserFromToken()
            getRecipes(user.id)
        }
    }

    private suspend fun getRecipes(userId: Int? = null){
        val receiveData = call.receive<GetRecipeListReceive>()
        val response = Recipes.getRecipes(receiveData.q, receiveData.offset, receiveData.limit, userId)
        call.respond(response)
    }

}