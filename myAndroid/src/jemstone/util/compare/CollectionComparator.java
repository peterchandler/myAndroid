package jemstone.util.compare;

import java.util.Collection;
import java.util.Iterator;

public class CollectionComparator<E> extends BaseComparator<Collection<E>> {
  private final String name;
  private final BaseComparator<? super E> itemComparator;

  public CollectionComparator(boolean compareId, String name, BaseComparator<? super E> itemComparator) {
    super(compareId);
    this.name = name;
    this.itemComparator = itemComparator;
    addChild(itemComparator);
  }

  @Override
  public boolean equals(Collection<E> list1, Collection<E> list2) throws CompareException {
    if (checkNull(name, list1, list2)) {
      return (list1 == list2);
    }

    int size1 = list1.size();
    int size2 = list2.size();
    boolean result = equals(name + " Size", size1, size2);

    Iterator<E> i1 = list1.iterator();
    Iterator<E> i2 = list2.iterator();
    
    int count = (size1 < size2) ? size1 : size2;
    for (int i=0;  i < count;  i++) {
      E item1 = i1.next();
      E item2 = i2.next();
      try {
        result &= itemComparator.equals(item1, item2);
      } catch (CompareException e) {
        String className = (item1 != null) ? getClassName(item1) : getClassName(item2);
        e.add("Error comparing Collection<%1$s> item %2$s/%3$s with item %2$s/%4$s",
              className, i+1, size1, size2, name);
        throw e;
      }
    }
    return result;
  }
}
