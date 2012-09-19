package jemstone.util.compare;

import java.util.Comparator;

import jemstone.model.HasId;


public class IdComparator implements Comparator<HasId> {
  @Override
  public int compare(HasId o1, HasId o2) {
    int id1 = o1.getId();
    int id2 = o2.getId();
    return (id1 < id2 ? -1 : (id1 == id2 ? 0 : 1));
  }
}
