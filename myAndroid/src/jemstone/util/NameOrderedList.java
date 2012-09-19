package jemstone.util;

import jemstone.model.HasName;
import jemstone.util.compare.NameComparator;

public class NameOrderedList<E extends HasName> extends OrderedList<E> {
  public NameOrderedList() {
    super(new NameComparator());
  }
}
