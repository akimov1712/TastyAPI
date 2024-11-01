package ru.topbun.features.upload

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.readByteArray
import ru.topbun.utills.compressImage
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.imageio.ImageIO



fun Application.configureUploadRouting() {
    routing {
        post("/upload") {
            val controller = UploadController(call)
            controller.saveImage()
        }

        static("/drawable") {
            files("src/main/resources/drawable")
        }
    }
}