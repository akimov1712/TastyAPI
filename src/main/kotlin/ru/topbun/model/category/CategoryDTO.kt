package ru.topbun.model.category

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

@Serializable
data class CategoryDTO(
    val id: Int = 0,
    val name: String,
    val image: String
)