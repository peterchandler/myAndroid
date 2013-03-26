package jemstone.util.compare;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jemstone.model.HasId;

public abstract class BaseComparator<E> {
  protected final boolean compareId;
  
  private List<BaseComparator<?>> children = new ArrayList<BaseComparator<?>>();

  public BaseComparator(boolean compareId) {
    this.compareId = compareId;
  }

  public abstract boolean equals(E o1, E o2) throws CompareException;

  public void assertEquals(E o1, E o2) throws CompareException, AssertionError {
    try {
      equals(o1, o2);
    } catch (CompareException e) {
      e.add("Expected %s and %s objects to be equal", getClassName(o1), getClassName(o2));
      throw e;
    }
  }

  public void assertNotEquals(E o1, E o2) throws CompareException, AssertionError {
    try {
      equals(o1, o2);
      throw new CompareException("Expected %s and %s objects to be different", getClassName(o1), getClassName(o2));
    } catch (CompareException e) {
      // Suppress exception
    }
  }

  protected void addChild(BaseComparator<?> child) {
    children.add(child);
  }

  protected boolean checkNull(String name, Object o1, Object o2) throws CompareException {
    if (o1 == null) {
      if (o2 != null) {
        throw new CompareException("Null check for '%s' failed: o1=null, o2=<%s>", name, o2);
      }
      return true;
    } else if (o2 == null) {
      throw new CompareException("Null check for '%s' failed: o2=null, o1=<%s>", name, o1);
    }
    return false;
  }

  protected boolean equals(String name, int n1, int n2) throws CompareException {
    if (n1 != n2) {
      throw new CompareException("Values for '%s' do not match: Expected <%s> but was <%s>", name, n1, n2);
    }
    return true;
  }

  protected boolean equals(String name, long n1, long n2) throws CompareException {
    if (n1 != n2) {
      throw new CompareException("Values for '%s' do not match: Expected <%s> but was <%s>", name, n1, n2);
    }
    return true;
  }

  protected boolean equals(String name, double n1, double n2) throws CompareException {
    boolean isNan1 = Double.isNaN(n1);
    boolean isNan2 = Double.isNaN(n2);

    if (isNan1) {
      if (isNan2) {
        return true;
      }
    } else if (!isNan2) {
      if (n1 == n2) {
        return true;
      }
    }

    throw new CompareException("Values for '%s' do not match: Expected <%s> but was <%s>", name, n1, n2);
  }

  protected boolean equals(String name, boolean b1, boolean b2) throws CompareException {
    if (b1 != b2) {
      throw new CompareException("Values for '%s' do not match: Expected <%s> but was <%s>", name, b1, b2);
    }
    return true;
  }

  protected boolean equals(String name, Date o1, Date o2) throws CompareException {
    if (!checkNull(name, o1, o2) && !o1.equals(o2)) {
      throw new CompareException("Dates for '%1$s' do not match: Expected <%2$tF %2$tT> but was <%3$tF %3$tT>", name, o1, o2);
    }
    return true;
  }

  protected boolean equals(String name, Object o1, Object o2) throws CompareException {
    if (!checkNull(name, o1, o2) && !o1.equals(o2)) {
      throw new CompareException("%s objects for '%s' do not match: Expected <%s> but was <%s>",
                                 getClassName(o1), name, o1, o2);
    }
    return true;
  }

  public boolean equals(String name, int[] array1, int[] array2) throws CompareException {
    if (checkNull(name, array1, array2)) {
      return (array1 == array2);
    }

    int size1 = array1.length;
    int size2 = array2.length;
    boolean result = equals("size", size1, size2);

    int count = (size1 < size2) ? size1 : size2;
    for (int i=0;  i < count;  i++) {
      int item1 = array1[i];
      int item2 = array2[i];
      try {
        result &= equals("int[]", item1, item2);
      } catch (CompareException e) {
        e.add("Error comparing int[], item %1$s/%2$s with item %1$s/%2$s", i+1, size1, size2);
        throw e;
      }
    }
    return result;
  }

  public boolean equals(String name, HasId o1, HasId o2) throws CompareException {
    if (compareId && !checkNull(name, o1, o2)) {
      return equals(name, o1.getId(), o2.getId());
    }
    return true;
  }

  protected String getClassName(Object o) {
    return (o != null) ? o.getClass().getSimpleName() : "?";
  }
}
