package jemstone.util.file;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import jemstone.model.EntityManager;
import jemstone.util.DaoException;
import jemstone.util.Timer;
import jemstone.util.log.Logger;

public class FileManager {
  protected final Logger log = Logger.getLogger(getClass());

  private File logPath;
  private String logFile;

  public FileManager(File logPath, String logFile) {
    this.logPath = logPath;
    this.logFile = logFile;
  }

  public synchronized void load(LoadFileDao dao) throws DaoException, IOException {
    Timer timer = new Timer();
    
    // Load the file
    File file = getFile();
    Reader reader = new FileReader(file);
    if (reader != null) {
      dao.load(reader);
      reader.close();
    
      log.info("load finished in %s: %s", timer, file);
    }
  }

  public synchronized void save(EntityManager manager, SaveFileDao dao) throws DaoException, IOException {
    log.info("Save called");
    
    // Save to file
    File file = getFile();
    try {
      Timer timer = new Timer();
    
      // Create new file
      File newFile = new File(file.toString() + ".new");
    
      Writer writer = new FileWriter(newFile);
      if (writer != null) {
        dao.save(manager, writer);
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
    return new File(logPath, logFile);
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
}
