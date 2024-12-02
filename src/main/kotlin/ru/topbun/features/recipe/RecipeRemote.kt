package ru.topbun.features.recipe

import io.ktor.http.*
import kotlinx.serialization.Serializable
import ru.topbun.model.category.CategoryDTO
import ru.topbun.model.ingredients.IngredientsDTO
import ru.topbun.model.recipe.DifficultyType
import ru.topbun.model.step.StepDTO
import ru.topbun.utills.AppException
import ru.topbun.utills.Error

@Serializable
data class GetRecipeListReceive(
    val q: String = "",
    val offset: Int = 0,
    val limit: Int = 20,
)

@Serializable
data class AddRecipeReceive(
    val title: String,
    val description: String? = null,
    val image: String? = null,
    val categories: List<Int>,
    val timeCooking: Int? = null,
    val difficulty: DifficultyType? = null,
    val carbs: Int? = null,
    val fat: Int? = null,
    val protein: Int? = null,
    val kcal: Int? = null,
    val ingredients: List<IngredientsDTO>,
    val steps: List<StepDTO>,
){

    fun isValid(){
        if (title.length > 48) throw AppException(HttpStatusCode.Conflict, Error.LENGTH_TITLE)
        if ((description?.length ?: 0) > 500) throw AppException(HttpStatusCode.Conflict, Error.LENGTH_DESCR)
        if ((timeCooking ?: 0) > 14400) throw AppException(HttpStatusCode.Conflict, Error.COUNT_COOKING_TIME)
        if (ingredients.size !in (1..24)) throw AppException(HttpStatusCode.Conflict, Error.COUNT_INGREDIENTS)
        if (steps.size !in (1..20)) throw AppException(HttpStatusCode.Conflict, Error.COUNT_STEPS)
        if (listOf(protein,carbs, fat).any{ (it?:0) > 1000 }) throw AppException(HttpStatusCode.Conflict, Error.COUNT_NUTRIENTS)
    }

}

