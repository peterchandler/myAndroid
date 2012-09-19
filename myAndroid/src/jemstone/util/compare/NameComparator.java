package jemstone.util.compare;

import java.util.Comparator;

import jemstone.model.HasName;


public class NameComparator implements Comparator<HasName> {
  @Override
  public int compare(HasName o1, HasName o2) {
    String name1 = o1.getName();
    String name2 = o2.getName();

    if (name1 == null) {
      return (name2 == null) ? 0 : -1;
    } else if (name2 == null) {
      return 1;
    }
    return name1.compareTo(name2);
  }
}
