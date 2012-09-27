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
  
  private static Factory factory;

  /** Path where files will be read and written to */
  private File path;

  /** Name of file to be read/written */
  private String fileName = "myandroid.xml";
  
  /** The name of the DAO that will read the file */
  private LoadFileDao loadDao;

  /** The name of the DAO that will write the file */
  private SaveFileDao saveDao;

  public FileManager() {
  }

  public File getPath() {
    return path;
  }

  public void setPath(File path) {
    this.path = path;
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

  public synchronized void load() throws DaoException, IOException {
    Timer timer = new Timer();
    
    // Load the file
    File file = getFile();
    Reader reader = new FileReader(file);
    if (reader != null) {
      loadDao.load(reader);
      reader.close();
    
      log.info("load finished in %s: %s", timer, file);
    }
  }

  @SuppressWarnings("unchecked")
  public synchronized void save(EntityManager manager) throws DaoException, IOException {
    log.info("Save called");
    
    // Save to file
    File file = getFile();
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
    
        log.info("save finished in %s: %s", timer, file);
      }
    } catch (IOException e) {
      throw new DaoException(e, "Error writing external file: %s", file);
    }
    
    // Clear save flag
    setSavedFlag(manager);
  }

  protected void setSavedFlag(EntityManager manager) {
    synchronized (manager) {
    }
  }

  protected File getFile() {
    File file = new File(getPath(), fileName);
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
   * Use a factory to create instances of the FileManager for different 
   * applications and environments.
   */
  public interface Factory {
    public FileManager getInstance(Context context);
  }

  public static Factory getFactory() {
    return factory;
  }

  public static void setFactory(Factory factory) {
    FileManager.factory = factory;
  }
  
  public static FileManager getInstance(Context context) {
    return getFactory().getInstance(context);
  }
}
