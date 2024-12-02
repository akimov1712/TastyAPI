package ru.topbun.features.favorite

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.topbun.model.favorite.Favorites
import ru.topbun.utills.*

class FavoriteController(
    private val call: RoutingCall
) {

    suspend fun fetchFavorite(){
        call.wrapperException {
            val user = call.getUserFromToken()
            val recipeId = call.parameters["id"]?.toIntOrNull() ?: throw AppException(HttpStatusCode.BadRequest, Error.PARAMS_INT)
            val categoryReceive = call.receive<FavoriteReceive>()
            Favorites.addOrUpdateFavorite(user.id, recipeId, categoryReceive.isFavorite)
            val newIsFavorite = Favorites.isFavorite(user.id, recipeId)
            call.respond(HttpStatusCode.OK, FavoriteResponse(recipeId, newIsFavorite))
        }

    }

    suspend fun getFavorites() {
        call.wrapperException {
            val user = call.getUserFromToken()
            val recipes = Favorites.getFavorites(user.id)
            call.respond(recipes)
        }
    }


}