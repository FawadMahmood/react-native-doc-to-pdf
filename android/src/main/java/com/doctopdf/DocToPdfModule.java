package com.doctopdf;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;

public class DocToPdfModule extends DocToPdfSpec {
  public static final String NAME = "DocToPdf";
  ReactApplicationContext context;


  DocToPdfModule(ReactApplicationContext context) {
    super(context);
    this.context = context;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void generateThumbnail(String filePath, String fileType, Promise promise) {
    switch(fileType){
      case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
      case "application/vnd.ms-excel":
        ExcelFileHandler excelFileHandler = new ExcelFileHandler(filePath, fileType, this.context, promise);
        excelFileHandler.handleExcelFile(filePath);
        break;
    }
  }
}
