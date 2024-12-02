package ru.topbun.features.recipe

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
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

    suspend fun deleteRecipe() {
        call.wrapperException {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, Error.PARAMS_ID)
            val user = call.getUserFromToken()
            val recipe = Recipes.getRecipeWithId(id)
            if (user.id != recipe.userId) throw AppException(HttpStatusCode.Forbidden, Error.DELETE_RECIPE)
            Recipes.deleteRecipe(id)
            call.respond(HttpStatusCode.OK)
        }
    }

    suspend fun addRecipe(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val userId = if (call.getUserFromToken().isAdmin) null else user.id
            val recipeReceive = call.receive<AddRecipeReceive>()
            recipeReceive.isValid()
            val recipeId = Recipes.insertRecipe(recipeReceive, userId)
            val recipe = Recipes.getRecipeWithId(recipeId)
            call.respond(recipe)
        }
    }

    suspend fun getRecipeWithCategory(){
        call.wrapperException {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, Error.PARAMS_ID)
            val receiveData = call.receive<GetRecipeListReceive>()
            val tokenPrincipal = call.principal<JWTPrincipal>()
            if(tokenPrincipal == null){
                val recipe = Recipes.getRecipeWithCategory(id, receiveData.offset, receiveData.limit)
                call.respond(recipe)
            } else {
                val user = call.getUserFromToken()
                val recipe = Recipes.getRecipeWithCategory(id, receiveData.offset, receiveData.limit, user.id)
                call.respond(recipe)
            }

        }
    }

    suspend fun getRecipeWithId(){
        call.wrapperException {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, Error.PARAMS_ID)
            val tokenPrincipal = call.principal<JWTPrincipal>()
            if(tokenPrincipal == null){
                val recipe = Recipes.getRecipeWithId(id)
                call.respond(recipe)
            } else {
                val user = call.getUserFromToken()
                val recipe = Recipes.getRecipeWithId(id, user.id)
                call.respond(recipe)
            }
        }
    }

    suspend fun getRecipeList(){
        call.wrapperException {
            val tokenPrincipal = call.principal<JWTPrincipal>()
            if(tokenPrincipal == null){
                getRecipes()
            } else {
                val user = call.getUserFromToken()
                getRecipes(user.id)
            }
        }
    }

    suspend fun getMyRecipeList(){
        call.wrapperException {
            val user = call.getUserFromToken()
            getRecipes(user.id, true)
        }
    }

    private suspend fun getRecipes(userId: Int? = null, myRecipe: Boolean = false){
        val receiveData = call.receive<GetRecipeListReceive>()
        val response = Recipes.getRecipes(receiveData.q, receiveData.offset, receiveData.limit, userId, myRecipe)
        call.respond(response)
    }



}