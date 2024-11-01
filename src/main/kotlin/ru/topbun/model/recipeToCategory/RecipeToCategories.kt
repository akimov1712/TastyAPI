package ru.topbun.model.recipeToCategory

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.model.category.Categories
import ru.topbun.model.ingredients.Ingredients.references
import ru.topbun.model.recipe.Recipes

object RecipeToCategories: IntIdTable("RecipeToCategories") {

    val recipeId = integer("recipe_id").references(Recipes.id)
    val categoryId = integer("category_id").references(Categories.id)

    fun insertRecipeToCategory(recipeId: Int, categoryId: Int) = transaction{
        insert {
            it[this.recipeId] = recipeId
            it[this.categoryId] = categoryId
        }
    }

}