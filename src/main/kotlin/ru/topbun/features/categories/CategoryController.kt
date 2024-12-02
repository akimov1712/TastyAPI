package ru.topbun.features.categories

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import ru.topbun.features.recipe.GetRecipeListReceive
import ru.topbun.model.category.Categories
import ru.topbun.utills.*

class CategoryController(
    private val call: RoutingCall
) {

    suspend fun addCategory(){
        call.wrapperException {
            val user = call.getUserFromToken()
            if (!user.isAdmin) throw AppException(HttpStatusCode.Forbidden, Error.FORBIDDEN)
            val categoryReceive = call.receive<AddCategoryReceive>()
            val categoryIsFound = Categories.containsCategory(categoryReceive.name)
            if (categoryIsFound) throw AppException(HttpStatusCode.Conflict, Error.CATEGORY_EXISTS)
            categoryReceive.isValid()
            Categories.insertCategory(categoryReceive.name, categoryReceive.image)
            Categories.fetchCategory(categoryReceive.name)?.let {
                call.respond(HttpStatusCode.OK, AddCategoryResponse(it))
            } ?: call.respond(HttpStatusCode.BadRequest, Error.BAD_REQUEST)
        }

    }

    suspend fun getCategories() {
        call.wrapperException {
            val receiveData = call.receive<GetCategoriesReceive>()
            val categories = Categories.selectAllCategories(receiveData.q, receiveData.offset, receiveData.limit)
            call.respond(HttpStatusCode.OK, GetCategoriesResponse(categories))
        }
    }

    suspend fun getCategory() {
        call.wrapperException {
            val id = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, Error.PARAMS_INT)
            val category = Categories.selectCategory(id)
            call.respond(HttpStatusCode.OK, GetCategoryResponse(category))
        }
    }

}