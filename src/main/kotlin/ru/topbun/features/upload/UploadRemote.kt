package ru.topbun.features.upload

import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(
    val url: String
)