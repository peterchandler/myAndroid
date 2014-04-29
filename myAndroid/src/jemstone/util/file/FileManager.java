package jemstone.util.file;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import jemstone.model.EntityManager;
import jemstone.util.Timer;
import jemstone.util.log.Logger;
import android.content.Context;

@SuppressWarnings("rawtypes")
public class FileManager {
  protected final Logger log = Logger.getLogger(getClass());
  
  /** Listen for events */
  private Listener listener;

  /** Path where files will be read and written to */
  private String filePath = "c:/temp";

  /** Name of file to be read/written */
  private String fileName = "myandroid.xml";
  
  /** The name of the DAO that will read the file */
  private LoadFileDao loadDao;

  /** The name of the DAO that will write the file */
  private SaveFileDao saveDao;
  
  /** Used by tests to simulate an exception being thrown during {@link #load()} or {@link #save(EntityManager)} */
  private boolean throwTestException;

  public FileManager() {
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String path) {
    this.filePath = path;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public LoadFileDao getLoadDao() {
    return loadDao;
  }

  public void setLoadDao(LoadFileDao loadDao) {
    this.loadDao = loadDao;
  }

  public SaveFileDao getSaveDao() {
    return saveDao;
  }

  public void setSaveDao(SaveFileDao saveDao) {
    this.saveDao = saveDao;
  }

  public Listener getListener() {
    return listener;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }

  protected boolean isThrowTestException() {
    return throwTestException;
  }

  protected void setThrowTestException(boolean throwTestException) {
    this.throwTestException = throwTestException;
  }

  public synchronized void load() throws DaoException, IOException {
    File file = getFile();

    // Notify listener
    if (listener != null) {
      listener.onPreLoad(file);
    }
    
    log.info("Load started: %s", file);
    Timer timer = new Timer();
    
    // Load the file
    Reader reader = new FileReader(file);
    if (reader != null) {
      try {
        loadDao.load(reader);
      } finally {
        reader.close();
      }
    }
    
    // Notify listener
    if (listener != null) {
      listener.onPostLoad();
    }

    log.info("Load finished in %s: %s", timer, file);
  }

  @SuppressWarnings("unchecked")
  public synchronized void save(EntityManager manager) throws DaoException, IOException {
    File file = getFile();

    // Notify listener
    if (listener != null) {
      listener.onPreSave(file);
    }
    
    log.info("Save started");
    
    // Save to file
    try {
      Timer timer = new Timer();

      // Create new file
      File newFile = new File(file.toString() + ".new");

      Writer writer = new FileWriter(newFile);
      if (writer != null) {
        saveDao.save(manager, writer);
        writer.close();

        // Rename the new file to the one we want
        createBackup(file);
        newFile.renameTo(file);
        
        // Mark entity manager as not modified
        manager.setModified(false);

        log.info("Save finished in %s: %s", timer, file);
      }
    } catch (Exception e) {
      throw new DaoException(e, "Error writing file: %s", file);
    }

    // Notify listener
    if (listener != null) {
      listener.onPostSave();
    }
  }

  protected File getFile() {
    File file = new File(filePath, fileName);
    return file;
  }

  protected void createBackup(File file) throws IOException {
    if (file.exists()) {
      File newFile = new File(file.toString() + ".bak");
      if (newFile.exists()) {
        if (!newFile.delete()) {
          throw new IOException("Cannot delete file [" + newFile.getAbsolutePath() + "]");
        }
      }

      log.debug("createBackup: Renaming [%s] to [%s]", file, newFile);

      if (!file.renameTo(newFile)) {
        throw new IOException("Cannot rename file [" + file.getAbsolutePath() + "] to [" + newFile.getAbsolutePath() + "]");
      }
    }
  }
  
  /**
   * Events fired by the FileManager
   */
  public interface Listener {
    public void onPreLoad(File file);

    public void onPostLoad();

    public void onPreSave(File file);
    
    public void onPostSave();
  }

  /**
   * Use a factory to create instances of the FileManager for different 
   * applications and environments.
   */
  public interface Factory {
    public FileManager getInstance(Context context);
  }
}
