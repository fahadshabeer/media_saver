# media_saver_plus
`media_saver_plus` is a Flutter plugin that allows you to save videos and images in android gallery
using the Android `MediaStore`. This plugin provides an easy-to-use interface for interacting with
the Android system's media storage while handling Android-specific requirements.


## Features

- Save video files to the `Gallery`.
- Save image files to the `Gallery`.

## Important Note 
   - This plugin doesn't cover the permission handling for now, you need to handle all the required storage permissions

## Requirements

- **Flutter** 3.0+
- **Android** API level 26 (Android 8.0) and higher.


## Permissions

For the plugin to work correctly, you need to add the following permissions to
your `AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```


## installation

To use this plugin, add `media_saver_plus`r as a dependency in your `pubspec.yaml` file:

```yaml
dependencies:
  media_saver_plus: ^0.0.1
```

```
flutter pub get
```


## Usage

- Save a Video to MediaStore:
  ```
  void saveVideoExample() async {
   String filePath = '/path/to/your/video.mp4';
   String? savedPath = await MediaSaver.saveVideo(filePath);

   if (savedPath != null) {
      print('Video saved at: $savedPath');
   } else {
     print('Failed to save video.');
   }
  }
  ```

- Save an Image to MediaStore:
  ```
  void saveImageExample() async {
   String filePath = '/path/to/your/image.jpg';
   String? savedPath = await MediaSaver.saveImage(filePath);

   if (savedPath != null) {
     print('Image saved at: $savedPath');
   } else {
     print('Failed to save image.');
   }
  }
  ```

    
### Explanation:

- **Overview**: Describes the functionality of the plugin and the Android version it supports.
- **Usage**: Provides example code for how to use the plugin to save and delete media files.


## License

This plugin is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.


