package jemstone.util.file;

import java.io.File;

import jemstone.util.MyRuntimeException;

public class FileManagerTestFactory extends FileManagerFactory {
  private static final String FACTORY_CLASSNAME = "jemstone.ui.AndroidFileManagerFactory";

  public static FileManager.Factory getFactory() {
    File file = new File("c:/temp");
    if (file.exists()) {
      return new FileManagerTestFactory();
    }

    try {
      Class<?> clazz = Class.forName(FACTORY_CLASSNAME);
      FileManager.Factory factory = (FileManager.Factory) clazz.newInstance();
      return factory;
    } catch (Exception e) {
     throw new MyRuntimeException(e);
    }
  }
}
