package com.doctopdf;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.Promise;

abstract class DocToPdfSpec extends ReactContextBaseJavaModule {
  DocToPdfSpec(ReactApplicationContext context) {
    super(context);
  }

  public abstract void generateThumbnail(String filePath, String fileType, Promise promise);
}
