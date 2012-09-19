package jemstone.util;

import java.io.Serializable;

/**
 * Implement a LIFO stack
 * @param <T>
 */
public class Stack<T extends Serializable> implements Serializable {
  private static final long serialVersionUID = 1L;

  private final T[] items;

  private int last = -1;

  @SuppressWarnings("unchecked")
  public Stack(int maxSize) {
    this.items = (T[]) new Serializable[maxSize];
  }

  public void push(T item) {
    // If stack is full then drop off first item
    final int length = items.length;
    if (last == length-1) {
      for (int i=1;  i < length;  i++) {
        items[i-1] = items[i];
      }
    } else {
      last++;
    }

    // Add new item as the Last
    items[last] = item;
  }

  public T pop() {
    if (last != -1) {
      return items[last--];
    }
    return null;
  }

  public T peek() {
    return items[last];
  }

  public void clear() {
    last = -1;
  }

  public boolean isEmpty() {
    return last == -1;
  }

  public int size() {
    return last+1;
  }
}
