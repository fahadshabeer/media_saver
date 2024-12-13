package com.fluttersoul.media_saver

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** MediaSaverPlugin */
class MediaSaverPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
  private lateinit var channel: MethodChannel
  private lateinit var context: Context
  private var activity: Activity? = null
  private lateinit var mediaStoreManager: MediastoreManager

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "media_saver")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    val filePath = call.arguments as String
    when (call.method) {
      "saveVideo" -> {
        val savedPath = mediaStoreManager.saveVideo(filePath)
        result.success(savedPath)
      }
      "saveImage" -> {
        val savedPath = mediaStoreManager.saveImage(filePath)
        result.success(savedPath)
      }
      "deleteImage" -> {
        mediaStoreManager.onIntentSenderRequired = { intentSender, uri ->
          result.error("IntentSenderRequired", "Need user confirmation", mapOf("intentSender" to intentSender, "uri" to uri.toString()))
        }
        val success = mediaStoreManager.deleteImage(filePath)
        result.success(success)
      }
      "deleteVideo" -> {
        mediaStoreManager.onIntentSenderRequired = { intentSender, uri ->
          result.error("IntentSenderRequired", "Need user confirmation", mapOf("intentSender" to intentSender, "uri" to uri.toString()))
        }
        val success = mediaStoreManager.deleteVideo(filePath)
        result.success(success)
      }
      else -> result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  // Handle activity lifecycle to use Activity in the plugin
  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    mediaStoreManager = MediastoreManager(context, activity!!)
  }

  override fun onDetachedFromActivityForConfigChanges() {
    activity = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivity() {
    activity = null
  }
}
