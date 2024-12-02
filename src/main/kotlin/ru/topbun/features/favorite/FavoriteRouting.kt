package ru.topbun.features.favorite

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureFavoriteRouting(){
    routing {
        authenticate {
            post("/favorite/{id}") {
                val favoriteController = FavoriteController(call)
                favoriteController.fetchFavorite()
            }
            get("/favorite") {
                val favoriteController = FavoriteController(call)
                favoriteController.getFavorites()
            }
        }
    }
}