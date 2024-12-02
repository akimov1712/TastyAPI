package ru.topbun.model.step

import com.sun.org.apache.xerces.internal.impl.xpath.XPath
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.model.recipe.Recipes
import ru.topbun.utills.AppException
import ru.topbun.utills.Error

object Steps: IntIdTable("steps"){
    val recipeId = integer("recipe_id").references(Recipes.id)
    val text = text("text")
    val preview = text("preview").nullable()
    val order = integer("order")

    fun deleteSteps(recipeId: Int) = transaction {
        Steps.deleteWhere { Steps.recipeId eq recipeId }
    }

    fun insertSteps(recipeId: Int, steps: List<StepDTO>){
        steps.forEachIndexed { index, step ->
            addStep(
                recipeId = recipeId,
                text = step.text,
                order = index + 1,
                preview = step.preview
            )
        }
    }

    private fun addStep(recipeId: Int, text: String, order: Int, preview: String? = null){
        transaction {
            insert {
                it[this.recipeId] = recipeId
                it[this.text] = text
                it[this.preview] = preview
                it[this.order] = order
            }
        }
    }

    fun getStepsWithRecipe(recipeId: Int) = selectAll()
        .where { Steps.recipeId eq recipeId }
        .map { it.toStep() }
        .sortedBy { it.order }

    fun ResultRow.toStep() = StepDTO(
        id = this[id].value,
        text = this[text],
        preview = this[preview],
        order = this[order]
    )

}