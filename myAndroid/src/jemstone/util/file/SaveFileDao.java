package jemstone.util.file;

import java.io.Writer;

import jemstone.model.EntityManager;

public interface SaveFileDao {
  public void save(EntityManager manager, Writer writer);
}
