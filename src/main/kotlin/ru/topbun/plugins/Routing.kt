package ru.topbun.plugins

import ru.topbun.features.login.configureLoginRouting
import ru.topbun.features.signUp.configureSignUpRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.topbun.features.account.configureAccountRouting
import ru.topbun.features.categories.configureCategoryRouting
import ru.topbun.features.favorite.configureFavoriteRouting
import ru.topbun.features.recipe.configureRecipeRouting
import ru.topbun.features.upload.configureUploadRouting

fun Application.configureRouting() {
    configureLoginRouting()
    configureSignUpRouting()
    configureCategoryRouting()
    configureRecipeRouting()
    configureUploadRouting()
    configureFavoriteRouting()
    configureAccountRouting()
}
