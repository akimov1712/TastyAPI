package ru.topbun.model.recipe

import io.ktor.http.*
import kotlinx.serialization.Serializable
import ru.topbun.model.category.CategoryDTO
import ru.topbun.model.ingredients.IngredientsDTO
import ru.topbun.model.step.StepDTO
import ru.topbun.utills.AppException
import ru.topbun.utills.Error

@Serializable
data class RecipeDTO(
    val id: Int = 0,
    val userId: Int? = null,
    val title: String,
    val description: String? = null,
    val image: String? = null,
    val isFavorite: Boolean,
    val categoryId: List<Int>,
    val timeCooking: Int? = null,
    val difficulty: DifficultyType,
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
        if (image != null && !Regex("((http|https)://)(www.)?[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)").matches(image)) throw AppException(HttpStatusCode.Conflict, Error.INVALID_IMAGE_URL)
        if ((timeCooking ?: 0) > 14400) throw AppException(HttpStatusCode.Conflict, Error.COUNT_COOKING_TIME)
        if (ingredients.size !in (1..24)) throw AppException(HttpStatusCode.Conflict, Error.COUNT_INGREDIENTS)
        if (steps.size !in (1..20)) throw AppException(HttpStatusCode.Conflict, Error.COUNT_STEPS)
        if (listOf(protein,carbs, fat).any{ (it?:0) > 1000 }) throw AppException(HttpStatusCode.Conflict, Error.COUNT_NUTRIENTS)
    }

}
