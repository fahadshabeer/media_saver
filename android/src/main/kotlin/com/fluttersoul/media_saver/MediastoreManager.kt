package com.fluttersoul.media_saver

import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream

class MediastoreManager(
    private val context: Context,
    private val activity: Activity,
) {

    // Callback to send the IntentSender back to Flutter when deletion needs user confirmation
    var onIntentSenderRequired: ((IntentSender, Uri) -> Unit)? = null

    // Save Video and return either the path (older versions) or Uri (API 29+)
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

                // For API 29+ return the Uri as string, for older versions return file path
                getRealPathFromURI(it)
            } ?: null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Save Image and return either the path (older versions) or Uri (API 29+)
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

                // For API 29+ return the Uri as string, for older versions return file path
                getRealPathFromURI(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Delete Video by file path, handling security exceptions
    fun deleteVideo(filePath: String): Boolean {
        return try {
            val uri = getMediaUri(filePath, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            uri?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    try {
                        context.contentResolver.delete(it, null, null)
                        true
                    } catch (e: RecoverableSecurityException) {
                        onIntentSenderRequired?.invoke(e.userAction.actionIntent.intentSender, it)
                        false
                    }
                } else {
                    context.contentResolver.delete(it, null, null) > 0
                }
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Delete Image by file path, handling security exceptions
    fun deleteImage(filePath: String): Boolean {
        return try {
            val uri = getMediaUri(filePath, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            uri?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    try {
                        context.contentResolver.delete(it, null, null)
                        true
                    } catch (e: RecoverableSecurityException) {
                        onIntentSenderRequired?.invoke(e.userAction.actionIntent.intentSender, it)
                        false
                    }
                } else {
                    context.contentResolver.delete(it, null, null) > 0
                }
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Method to get the MediaStore Uri for a given file path
    private fun getMediaUri(filePath: String, contentUri: Uri): Uri? {
        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.MediaColumns.DATA}=?"
        val selectionArgs = arrayOf(filePath)

        val cursor: Cursor? =
            context.contentResolver.query(contentUri, projection, selection, selectionArgs, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                return Uri.withAppendedPath(contentUri, id.toString())
            }
        }
        return null
    }

    // Method to get file path from URI for Android versions below API 29
    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            it.moveToFirst()
            return it.getString(columnIndex)
        }
        return null
    }
}
