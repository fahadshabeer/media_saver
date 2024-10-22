
import 'media_saver_platform_interface.dart';

class MediaSaver {
  Future<String?> getPlatformVersion() {
    return MediaSaverPlatform.instance.getPlatformVersion();
  }
  static Future<String?> saveVideo(String path){
    return MediaSaverPlatform.instance.saveVideo(path);
  }

 static Future<String?> saveImage(String path){
    return MediaSaverPlatform.instance.saveImage(path);
  }
}
