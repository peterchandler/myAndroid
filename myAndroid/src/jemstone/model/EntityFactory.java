package jemstone.model;

import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.List;

import jemstone.util.NameOrderedList;


public abstract class EntityFactory<T extends HasName> implements Iterable<T> {
  private final List<T> entities = new NameOrderedList<T>();

  protected abstract T add(long id);
  protected abstract T add(String name);

  protected T add(T entity) {
    entities.add(entity);
    return entity;
  }

  public List<T> values() {
    return entities;
  }

  @Override
  public Iterator<T> iterator() {
    return entities.iterator();
  }

  public T get(long id) {
    final int size = entities.size();
    for (int i=0;  i < size;  i++) {
      T entity = entities.get(i);
      if (id == entity.getId()) {
        return entity;
      }
    }
    return null;
  }

  public T get(String name) {
    if (name != null) {
      final int size = entities.size();
      for (int i=0;  i < size;  i++) {
        T entity = entities.get(i);
        if (name.equals(entity.getName())) {
          return entity;
        }
      }
    }
    return null;
  }

  public T create(long id) {
    T entity = get(id);
    if (entity != null) {
      String name = entity.getClass().getSimpleName();
      throw new InvalidParameterException(name + " id=" + id + " already exists!");
    }
    return add(id);
  }

  public T create(String name) {
    T entity = get(name);
    return (entity != null) ? entity : add(name);
  }
  
  public boolean remove(T entity) {
    return entities.remove(entity);
  }
}
