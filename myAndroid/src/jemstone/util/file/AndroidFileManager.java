package jemstone.util.file;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class AndroidFileManager extends FileManager {
  private boolean externalStorageAvailable;
  private boolean externalStorageWriteable;
  
  public AndroidFileManager() {
  }

  public AndroidFileManager(Context context, boolean useExternalStorage) {
    setUseExternalStorage(context, useExternalStorage);
  }

  protected void setUseExternalStorage(Context context, boolean useExternalStorage) {
    // Determine whether external media can be written to
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state)) {
      // We can read and write the media
      externalStorageAvailable = externalStorageWriteable = true;
    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      // We can only read the media
      externalStorageAvailable = true;
      externalStorageWriteable = false;
    } else {
      // Something else is wrong. It may be one of many other states, but all we need
      //  to know is we can neither read nor write
      externalStorageAvailable = externalStorageWriteable = false;
    }
    
    // Derive the Android file path
    File path = null;
    if (useExternalStorage && externalStorageAvailable && externalStorageWriteable) {
      path = context.getExternalFilesDir(null);
      log.debug("getExternalFilesDir returned: %s", path);
    } else {
      path = context.getFilesDir();
      log.debug("getFilesDir returned: %s", path);
    }
    
    // Set the path in the parent
    setFilePath(path.getAbsolutePath());
  }
}
