import 'package:flutter_test/flutter_test.dart';
import 'package:media_saver_plus/media_saver_plus.dart';
import 'package:media_saver_plus/media_saver_platform_interface.dart';
import 'package:media_saver_plus/media_saver_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockMediaSaverPlatform
    with MockPlatformInterfaceMixin
    implements MediaSaverPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<String?> saveImage(String path) {
    // TODO: implement saveImage
    throw UnimplementedError();
  }

  @override
  Future<String?> saveVideo(String path) {
    // TODO: implement saveVideo
    throw UnimplementedError();
  }
}

void main() {
  final MediaSaverPlatform initialPlatform = MediaSaverPlatform.instance;

  test('$MethodChannelMediaSaver is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelMediaSaver>());
  });

  test('getPlatformVersion', () async {
    MediaSaver mediaSaverPlugin = MediaSaver();
    MockMediaSaverPlatform fakePlatform = MockMediaSaverPlatform();
    MediaSaverPlatform.instance = fakePlatform;

    expect(await mediaSaverPlugin.getPlatformVersion(), '42');
  });
}
