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
    val categories: List<CategoryDTO>,
    val timeCooking: Int? = null,
    val difficulty: DifficultyType? = null,
    val carbs: Int? = null,
    val fat: Int? = null,
    val protein: Int? = null,
    val kcal: Int? = null,
    val ingredients: List<IngredientsDTO>,
    val steps: List<StepDTO>,
)