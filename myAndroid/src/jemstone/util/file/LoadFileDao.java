package jemstone.util.file;

import java.io.Reader;

public interface LoadFileDao extends XmlConstants {
  public void load(Reader reader) throws DaoException;
}
