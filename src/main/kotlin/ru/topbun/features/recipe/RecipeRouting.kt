package ru.topbun.features.recipe

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.topbun.utills.getUserFromToken

fun Application.configureRecipeRouting(){
    routing {
        authenticate {
            delete("/recipe/{id}") {
                val controller = RecipeController(call)
                controller.deleteRecipe()
            }
            post("/recipe/{id}") {
                val controller = RecipeController(call)
                controller.getRecipeWithId()
            }
            post("/recipe/category/{id}") {
                val controller = RecipeController(call)
                controller.getRecipeWithCategory()
            }
            post("/recipe/my") {
                val controller = RecipeController(call)
                controller.getMyRecipeList()
            }
            post("/recipe") {
                val controller = RecipeController(call)
                controller.getRecipeList()
            }
            post("/recipe/add") {
                val controller = RecipeController(call)
                controller.addRecipe()
            }
        }
    }
}