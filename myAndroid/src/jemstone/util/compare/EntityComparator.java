package jemstone.util.compare;

import jemstone.model.Entity;

public abstract class EntityComparator<E extends Entity> extends BaseComparator<E> {
  private boolean compareId = true;
  
  public EntityComparator() {
    super();
  }
  
  public EntityComparator(boolean compareId) {
    super();
    this.compareId = compareId;
  }

  @Override
  public boolean equals(E o1, E o2) throws CompareException {
    return compareId ? equals("id", o1, o2) : true;
  }
}
