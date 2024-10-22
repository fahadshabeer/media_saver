import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'media_saver_method_channel.dart';

abstract class MediaSaverPlatform extends PlatformInterface {
  /// Constructs a MediaSaverPlatform.
  MediaSaverPlatform() : super(token: _token);

  static final Object _token = Object();

  static MediaSaverPlatform _instance = MethodChannelMediaSaver();

  /// The default instance of [MediaSaverPlatform] to use.
  ///
  /// Defaults to [MethodChannelMediaSaver].
  static MediaSaverPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [MediaSaverPlatform] when
  /// they register themselves.
  static set instance(MediaSaverPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<String?> saveVideo(String path) {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
  Future<String?> saveImage(String path) {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

}
