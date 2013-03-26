package jemstone.util.compare;

import java.util.Map;

public class MapComparator<K,V> extends BaseComparator<Map<K,V>> {
  private final String name;
  private final BaseComparator<? super V> itemComparator;

  public MapComparator(boolean compareId, String name, BaseComparator<? super V> itemComparator) {
    super(compareId);
    this.name = name;
    this.itemComparator = itemComparator;
    addChild(itemComparator);
  }

  @Override
  public boolean equals(Map<K,V> map1, Map<K,V> map2) throws CompareException {
    if (checkNull(name, map1, map2)) {
      return (map1 == map2);
    }

    // Size check
    boolean result = equals(name + " Size", map1.size(), map2.size());

    // Compare items for all keys in map1
    for (K key : map1.keySet()) {
      result &= equals(map1, map2, key);
    }

    // Compare items for all keys in map2
    for (K key : map2.keySet()) {
      result &= equals(map1, map2, key);
    }
    
    return result;
  }

  private boolean equals(Map<K,V> map1, Map<K,V> map2, K key) throws CompareException {
    V item1 = map1.get(key);
    V item2 = map2.get(key);
    try {
      return itemComparator.equals(item1, item2);
    } catch (CompareException e) {
      String className = (item1 != null) ? getClassName(item1) : getClassName(item2);
      e.add("Error comparing Map<%1$s> item [key=%2$s]", className, key, name);
      throw e;
    }
  }
}
