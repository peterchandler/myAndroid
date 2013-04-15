package jemstone.model;

public interface EntityManager {
  public boolean isModified();
  public void setModified(boolean isModified);
}
