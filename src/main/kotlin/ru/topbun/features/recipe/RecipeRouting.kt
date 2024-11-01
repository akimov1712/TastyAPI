package ru.topbun.features.recipe

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRecipeRouting(){
    routing {
        post("/recipe") {
            val controller = RecipeController(call)
            controller.getRecipeList()
        }
        post("/recipe/{id}") {
            val controller = RecipeController(call)
            controller.getRecipeWithId()
        }
        get("/recipe/category/{id}") {
            val controller = RecipeController(call)
            controller.getRecipeWithCategory()
        }
        authenticate {
            post("/recipe/my") {
                val controller = RecipeController(call)
                controller.getMyRecipeList()
            }
            post("/recipe/add") {
                val controller = RecipeController(call)
                controller.addRecipe()
            }
        }
    }
}