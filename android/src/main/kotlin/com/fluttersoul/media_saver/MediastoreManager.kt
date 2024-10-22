package com.fluttersoul.media_saver

import android.app.RecoverableSecurityException
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream

class MediastoreManager(
    private val context: Context,
) {

    fun saveVideo(filePath: String): String? {
        return try {
            val videoFile = File(filePath)
            val values = ContentValues().apply {
                put(MediaStore.Video.Media.DISPLAY_NAME, videoFile.name)
                put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/")
            }
            val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                val videoPath = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                context.contentResolver.insert(videoPath, values)
            }

            uri?.let {
                val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
                outputStream?.use { outStream ->
                    FileInputStream(videoFile).use { inputStream ->
                        val buffer = ByteArray(1024)
                        var len: Int
                        while (inputStream.read(buffer).also { len = it } != -1) {
                            outStream.write(buffer, 0, len)
                        }
                    }
                }
                it.toString() // Return the URI as a string representing the saved path
            } ?: null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveImage(filePath: String): String? {
        return try {
            val imageFile = File(filePath)
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/")
            }
            val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                val imagePath = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                context.contentResolver.insert(imagePath, values)
            }

            uri?.let {
                val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
                outputStream?.use { outStream ->
                    FileInputStream(imageFile).use { inputStream ->
                        val buffer = ByteArray(1024)
                        var len: Int
                        while (inputStream.read(buffer).also { len = it } != -1) {
                            outStream.write(buffer, 0, len)
                        }
                    }
                }
                it.toString() // Return the URI as a string representing the saved path
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
