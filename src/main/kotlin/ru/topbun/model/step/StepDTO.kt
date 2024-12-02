package ru.topbun.model.step

import kotlinx.serialization.Serializable

@Serializable
data class StepDTO(
    @Transient val id: Int = 0,
    val text: String,
    val preview: String?,
    val order: Int = 0,
)
