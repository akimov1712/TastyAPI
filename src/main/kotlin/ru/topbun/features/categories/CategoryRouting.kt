package ru.topbun.features.categories

import io.ktor.http.*
import ru.topbun.features.categories.CategoryController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.topbun.utills.Error
import ru.topbun.utills.createError

fun Application.configureCategoryRouting(){
    routing {
        authenticate {
            post("/category/add") {
                val categoryController = CategoryController(call)
                categoryController.addCategory()
            }
        }
        post("/category") {
            val categoryController = CategoryController(call)
            categoryController.getCategories()
        }
        get("/category/{id}") {
            val categoryController = CategoryController(call)
            categoryController.getCategory()
        }
    }
}