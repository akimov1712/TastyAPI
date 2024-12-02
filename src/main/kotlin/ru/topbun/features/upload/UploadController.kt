package ru.topbun.features.upload

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.readByteArray
import ru.topbun.utills.AppException
import ru.topbun.utills.Error
import ru.topbun.utills.compressImage
import ru.topbun.utills.wrapperException
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.imageio.ImageIO

class UploadController(private val call: RoutingCall) {

    suspend fun saveImage() {
        call.wrapperException {
            val multipartData = call.receiveMultipart()
            var savedFileName: String? = null

            multipartData.forEachPart { part ->
                if (part is PartData.FileItem) {
                    try {
                        val fileBytes = withContext(Dispatchers.IO) { part.provider().readRemaining().readByteArray() }
                        validateFileSize(fileBytes)
                        val format = getFileFormat(part)
                        val image = loadAndValidateImage(fileBytes)

                        val compressedImage = compressImageIfNeeded(image)
                        val directory = createImageDirectory()
                        savedFileName = if (format == "png") {
                            saveImageInOriginalFormat(compressedImage, directory, "png")
                        } else {
                            saveImageInJpgFormat(compressedImage, directory)
                        }
                    } finally {
                        part.dispose()
                    }
                }
            }
            if (savedFileName != null) {
                call.respond(HttpStatusCode.OK, UploadResponse("drawable/$savedFileName"))
            } else {
                throw AppException(HttpStatusCode.BadRequest, Error.NO_FILE_UPLOADED)
            }
        }
    }

    private fun createImageDirectory(): File {
        val directory = File("src/main/resources/drawable")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }

    private fun validateFileSize(fileBytes: ByteArray) {
        if (fileBytes.size > 8 * 1024 * 1024) {
            throw AppException(HttpStatusCode.BadRequest, Error.FILE_SIZE_8_MB)
        }
    }

    private fun getFileFormat(part: PartData.FileItem): String {
        return part.contentType?.contentSubtype?.lowercase() ?: throw AppException(HttpStatusCode.BadRequest, Error.NO_SUPPORT_FORMAT_FILE)
    }

    private fun loadAndValidateImage(fileBytes: ByteArray): BufferedImage {
        val image = ImageIO.read(ByteArrayInputStream(fileBytes))
            ?: throw AppException(HttpStatusCode.BadRequest, Error.INVALID_IMAGE_FILE)
        return image
    }

    private fun compressImageIfNeeded(image: BufferedImage): BufferedImage {
        return if (image.width > 1080 || image.height > 1080) {
            compressImage(image, 1080, 1080)
        } else {
            image
        }
    }

    private fun saveImageInOriginalFormat(image: BufferedImage, directory: File, format: String): String {
        val uniqueFileName = "${UUID.randomUUID()}_${System.currentTimeMillis()}.$format"
        val file = File(directory, uniqueFileName)
        FileOutputStream(file).use { output ->
            ImageIO.write(image, format, output)
        }
        return uniqueFileName
    }

    private fun saveImageInJpgFormat(image: BufferedImage, directory: File): String {
        val uniqueFileName = "${UUID.randomUUID()}_${System.currentTimeMillis()}.jpg"
        val file = File(directory, uniqueFileName)
        FileOutputStream(file).use { output ->
            ImageIO.write(image, "jpg", output)
        }
        return uniqueFileName
    }
}
