package ru.topbun.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.model.category.Categories
import ru.topbun.model.favorite.Favorites
import ru.topbun.model.ingredients.Ingredients
import ru.topbun.model.recipe.Recipes
import ru.topbun.model.recipeToCategory.RecipeToCategories
import ru.topbun.model.step.Steps
import ru.topbun.model.user.Users

fun Application.configureDatabases() {
    Database.connect(
        url = environment.config.property("database.url").getString(),
        user = environment.config.property("database.user").getString(),
        driver = environment.config.property("database.driver").getString(),
        password = environment.config.property("database.password").getString(),
    )
    transaction {
        SchemaUtils.create(Users, Categories, Favorites, Ingredients, Recipes, Steps, RecipeToCategories)
    }

}
