import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'media_saver_platform_interface.dart';

/// An implementation of [MediaSaverPlatform] that uses method channels.
class MethodChannelMediaSaver extends MediaSaverPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('media_saver');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  /// Save video to media store
  @override
   Future<String?> saveVideo(String filePath) async {
    final String? savedPath = await methodChannel.invokeMethod('saveVideo', filePath);
    return savedPath;
  }

  /// Save image to media store
  @override
   Future<String?> saveImage(String filePath) async {
    final String? savedPath = await methodChannel.invokeMethod('saveImage', filePath);
    return savedPath;
  }
}
