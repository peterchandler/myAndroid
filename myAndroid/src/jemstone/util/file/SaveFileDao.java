package jemstone.util.file;

import java.io.Writer;

import jemstone.model.EntityManager;

public interface SaveFileDao<T extends EntityManager> extends XmlConstants {
  public void save(T manager, Writer writer) throws DaoException;
}
