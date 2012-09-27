package jemstone.util.file;

import java.io.Reader;

public interface LoadFileDao {
  public void load(Reader reader) throws DaoException;
}
