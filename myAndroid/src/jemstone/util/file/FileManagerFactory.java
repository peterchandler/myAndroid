package jemstone.util.file;

import jemstone.util.MyRuntimeException;
import jemstone.util.log.Logger;
import android.content.Context;

public class FileManagerFactory implements FileManager.Factory {
  protected final Logger log = Logger.getLogger(this);

  /** Path to read/write from */
  private String filePath = "c:\\temp";

  /** Name of file to be read/written */
  private String fileName = "myandroid.xml";

  private Class<? extends LoadFileDao> loadDaoClass;
  private Class<? extends SaveFileDao<?>> saveDaoClass;
  private Class<? extends FileManager.Listener> listenerClass;
  
  public FileManagerFactory() {
  }
  
  public FileManagerFactory(Class<? extends LoadFileDao> loadDaoClass, 
                            Class<? extends SaveFileDao<?>> saveDaoClass,
                            Class<? extends FileManager.Listener> listenerClass) {
    this.loadDaoClass = loadDaoClass;
    this.saveDaoClass = saveDaoClass;
    this.listenerClass = listenerClass;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
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

  protected void setListener(FileManager manager) {
    // Create a DAO object for the new manager
    try {
      if (listenerClass != null) {
        manager.setListener(listenerClass.newInstance());
      }
    } catch (Exception e) {
      throw new MyRuntimeException(e, "Cannot create Listener class");
    }
  }

  @Override
  public FileManager getInstance(Context context) {
    FileManager manager = createInstance(context);
    
    log.warn("Created file manager: %s", manager.getClass().getName());

    // Set the path and filename to be read/written from
    manager.setFilePath(filePath);
    manager.setFileName(fileName);
    
    // Set DAO objects and listener
    setLoadDao(manager);
    setSaveDao(manager);
    setListener(manager);

    return manager;
  }

  protected FileManager createInstance(Context context) {
    return new FileManager();
  }
}
