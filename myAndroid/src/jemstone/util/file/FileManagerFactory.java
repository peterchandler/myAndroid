package jemstone.util.file;

import java.io.File;

import jemstone.util.MyRuntimeException;
import android.content.Context;

public class FileManagerFactory implements FileManager.Factory {
  /** Name of file to be read/written */
  private String fileName = "myandroid.xml";

  private Class<LoadFileDao> loadDaoClass;
  private Class<SaveFileDao<?>> saveDaoClass;
  
  public FileManagerFactory() {
  }
  
  public FileManagerFactory(Class<LoadFileDao> loadDaoClass, 
                            Class<SaveFileDao<?>> saveDaoClass) {
    this.loadDaoClass = loadDaoClass;
    this.saveDaoClass = saveDaoClass;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setLoadDaoClass(Class<LoadFileDao> loadDaoClass) {
    this.loadDaoClass = loadDaoClass;
  }

  public void setSaveDaoClass(Class<SaveFileDao<?>> saveDaoClass) {
    this.saveDaoClass = saveDaoClass;
  }

  protected void setLoadDao(FileManager manager) {
    // Create a DAO object for the new manager
    try {
      if (loadDaoClass != null) {
        manager.setLoadDao(loadDaoClass.newInstance());
      }
    } catch (Exception e) {
      throw new MyRuntimeException(e, "Cannot create LoadDAO class");
    }
  }

  protected void setSaveDao(FileManager manager) {
    // Create a DAO object for the new manager
    try {
      if (saveDaoClass != null) {
        manager.setSaveDao(saveDaoClass.newInstance());
      }
    } catch (Exception e) {
      throw new MyRuntimeException(e, "Cannot create SaveDAO class");
    }
  }

  @Override
  public FileManager getInstance(Context context) {
    FileManager manager = new FileManager();

    // Set the path and filename to be read/written from
    manager.setPath(new File("c:/temp"));
    manager.setFileName(fileName);
    
    // Set DAO objects
    setLoadDao(manager);
    setSaveDao(manager);

    return manager;
  }
}
