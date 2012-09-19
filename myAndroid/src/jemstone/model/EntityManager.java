package jemstone.model;

import jemstone.util.Printable;

public abstract class EntityManager implements Printable {
  private static EntityManager manager = null;

  /** Private default constructor to enforce singleton behaviour */
  private EntityManager() {
  }

  public static EntityManager getInstance() {
    return manager;
  }

  public interface Factory {
    public EntityManager getInstance();
  }
}
