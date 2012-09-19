package jemstone.util.compare;

import java.util.List;

public class ListComparator<E> extends BaseComparator<List<E>> {
  private final String name;
  private final BaseComparator<? super E> itemComparator;

  public ListComparator(String name, BaseComparator<? super E> itemComparator) {
    this.name = name;
    this.itemComparator = itemComparator;
    addChild(itemComparator);
  }

  @Override
  public boolean equals(List<E> list1, List<E> list2) throws CompareException {
    if (checkNull(name, list1, list2)) {
      return (list1 == list2);
    }

    int size1 = list1.size();
    int size2 = list2.size();
    boolean result = equals(name + " Size", size1, size2);

    int count = (size1 < size2) ? size1 : size2;
    for (int i=0;  i < count;  i++) {
      E item1 = list1.get(i);
      E item2 = list2.get(i);
      try {
        result &= itemComparator.equals(item1, item2);
      } catch (CompareException e) {
        String className = (item1 != null) ? getClassName(item1) : getClassName(item2);
        e.add("Error comparing List<%1$s> item %2$s/%3$s with item %2$s/%4$s",
              className, i+1, size1, size2, name);
        throw e;
      }
    }
    return result;
  }
}
