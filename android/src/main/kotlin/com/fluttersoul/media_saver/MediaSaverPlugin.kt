package com.fluttersoul.media_saver

import android.app.Activity
import android.content.Context
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** MediaSaverPlugin */
class MediaSaverPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the
  // Activity
  private lateinit var channel : MethodChannel
  private lateinit var context: Context
  private var activity: Activity? = null

  private lateinit var mediaStoreManager: MediastoreManager

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "media_saver")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
    mediaStoreManager = MediastoreManager(context)
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
      else -> result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }
}
