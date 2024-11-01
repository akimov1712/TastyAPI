package ru.topbun

import ru.topbun.plugins.*
import ru.topbun.utills.generateToken
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.model.category.Categories
import ru.topbun.model.category.CategoryDTO
import ru.topbun.model.favorite.Favorites
import ru.topbun.model.ingredients.Ingredients
import ru.topbun.model.ingredients.IngredientsDTO
import ru.topbun.model.recipe.DifficultyType
import ru.topbun.model.recipe.RecipeDTO
import ru.topbun.model.recipe.Recipes
import ru.topbun.model.recipeToCategory.RecipeToCategories
import ru.topbun.model.step.StepDTO
import ru.topbun.model.step.Steps
import java.io.File

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureSecurity()
    configureRouting();

}
