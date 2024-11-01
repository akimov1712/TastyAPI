package ru.topbun.model.category

import io.ktor.http.*
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.model.recipe.Recipes.getRecipeWithId
import ru.topbun.model.recipe.Recipes.title
import ru.topbun.model.recipeToCategory.RecipeToCategories
import ru.topbun.model.recipeToCategory.RecipeToCategories.recipeId
import ru.topbun.utills.AppException
import ru.topbun.utills.Error

object Categories : IntIdTable("Categories") {
    val name = text("name")
    val image = text("image")

    fun fetchCategory(name: String): CategoryDTO? =
        transaction { selectAll().where { Categories.name eq name }.firstOrNull()?.toCategory() }

    fun containsCategory(name: String): Boolean = fetchCategory(name) != null

    fun insertCategory(name: String, image: String) {
        transaction { insert {
            it[Categories.name] = name
            it[Categories.image] = image
        } }
    }

    fun getCategoriesWithRecipe(recipeId: Int) = transaction {
        val categoryIds = RecipeToCategories.selectAll().where { RecipeToCategories.recipeId eq recipeId }.map { it[RecipeToCategories.categoryId] }
        categoryIds.map { selectCategory(it) }
    }

    fun selectAllCategories(q: String, offset: Int, limit: Int) = transaction {
        selectAll()
            .limit(n = limit, offset = offset.toLong())
            .where{name.lowerCase() like "%${q.lowercase()}%" }
            .map{ it.toCategory() }
            .distinctBy { it.name }
    }

    fun selectCategory(id: Int) = transaction {
        selectAll().where(RecipeToCategories.id eq id).firstOrNull()?.toCategory() ?: throw AppException(HttpStatusCode.NotFound, Error.CATEGORY_NOT_FOUND)
    }

    private fun ResultRow.toCategory(): CategoryDTO {
        return CategoryDTO(
            id = this[id].value,
            name = this[name],
            image = this[image],
        )
    }

}