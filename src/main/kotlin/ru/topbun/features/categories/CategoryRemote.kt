package ru.topbun.features.categories

import io.ktor.http.*
import kotlinx.serialization.Serializable
import ru.topbun.model.category.CategoryDTO
import ru.topbun.utills.AppException
import ru.topbun.utills.Error

@Serializable
data class AddCategoryReceive(
    val name: String,
    val image: String,
){

    fun isValid() = when{
        name.length < 5 -> throw AppException(HttpStatusCode.BadRequest, Error.CATEGORY_LENGTH)
        else -> null
    }

}

@Serializable
data class AddCategoryResponse(
    val category: CategoryDTO,
)


@Serializable
data class GetCategoriesReceive(
    val q: String = "",
    val offset: Int = 0,
    val limit: Int = 20,
)

@Serializable
data class GetCategoriesResponse(
    val categories: List<CategoryDTO>,
)

@Serializable
data class GetCategoryResponse(
    val category: CategoryDTO
)
