package jemstone.util.compare;

import jemstone.model.Entity;

public abstract class EntityComparator<E extends Entity> extends BaseComparator<E> {
  public EntityComparator(boolean compareId) {
    super(compareId);
  }

  @Override
  public boolean equals(E o1, E o2) throws CompareException {
    return equals("id", o1, o2);
  }
}
