package jemstone.model;

import jemstone.util.Printable;
import jemstone.util.Printer;

public abstract class Entity implements HasId, Printable {
  private long id;

  protected Entity(long id) {
    this.id = id;
  }

  protected void setId(long id) {
    this.id = id;
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public final String toString() {
    return Printer.toString(this);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (id ^ (id >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Entity other = (Entity) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
