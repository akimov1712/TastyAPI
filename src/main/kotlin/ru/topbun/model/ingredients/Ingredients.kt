package ru.topbun.model.ingredients

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.model.recipe.Recipes
import ru.topbun.model.step.Steps
import ru.topbun.model.step.Steps.references

object Ingredients: IntIdTable("ingredients") {

    val name = text("name")
    val value = text("value")
    val recipeId = integer("recipeId").references(Recipes.id)

    fun deleteIngr(recipeId: Int) = transaction {
        Ingredients.deleteWhere { Ingredients.recipeId eq recipeId }
    }

    fun insertIngredients(recipeId: Int, ingredients: List<IngredientsDTO>) {
        ingredients.forEach {
            addIngredient(
                name = it.name,
                value = it.value,
                recipeId = recipeId
            )
        }
    }

    fun addIngredient(name: String, value: String, recipeId: Int) = transaction{
        insert {
            it[this.name] = name
            it[this.value] = value
            it[this.recipeId] = recipeId
        }
    }

    fun getIngredientsFromId(recipeId: Int) = transaction{
        select(Ingredients.id, Ingredients.name, Ingredients.value).where(Ingredients.recipeId eq recipeId).map { it.toIngredient() }
    }

    fun ResultRow.toIngredient() = IngredientsDTO(
        id = this[Ingredients.id].value,
        name = this[Ingredients.name],
        value = this[Ingredients.value],
    )

}