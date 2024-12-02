package ru.topbun.model.recipeToCategory

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.model.category.Categories
import ru.topbun.model.ingredients.Ingredients
import ru.topbun.model.ingredients.Ingredients.references
import ru.topbun.model.ingredients.IngredientsDTO
import ru.topbun.model.recipe.Recipes
import ru.topbun.model.step.Steps

object RecipeToCategories: IntIdTable("recipe_to_categories") {

    val recipeId = integer("recipe_id").references(Recipes.id)
    val categoryId = integer("category_id").references(Categories.id)

    fun ResultRow.toRecipeToCategory() = RecipeToCategoryDTO(
        id = this[id].value,
        recipeId = this[recipeId],
        categoryId = this[categoryId],
    )

    fun deleteRecipeToCategories(recipeId: Int) = transaction {
        RecipeToCategories.deleteWhere { RecipeToCategories.recipeId eq recipeId }
    }



    fun insertRecipeToCategory(recipeId: Int, categoriesId: List<Int>) = transaction{
        categoriesId.forEach { categoryId ->
            insert {
                it[this.recipeId] = recipeId
                it[this.categoryId] = categoryId
            }
        }
    }

}