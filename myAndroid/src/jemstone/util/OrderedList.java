package jemstone.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class OrderedList<E> implements List<E>, Printable {
  private List<E> items = new ArrayList<E>();

  private boolean sorted = true;

  private Comparator<? super E> comparator;

  /**
   * Create a new ordered list with a {@link Comparator} that will be used to sort the items
   * when required.
   * @param comparator
   */
  public OrderedList(Comparator<? super E> comparator) {
    this.comparator = comparator;
  }

  protected void sort() {
    if (!sorted) {
      Collections.sort(items, comparator);
      sorted = true;
    }
  }
  
  public void setDirty() {
    sorted = false;
  }

  /**
   * @param arg0
   * @return
   * @see java.util.List#add(java.lang.Object)
   */
  @Override
  public boolean add(E arg0) {
    sorted = false;
    return items.add(arg0);
  }

  /**
   * @param location
   * @param object
   * @see java.util.List#add(int, java.lang.Object)
   */
  @Override
  public void add(int location, E object) {
    sorted = false;
    items.add(location, object);
  }

  /**
   * @param arg0
   * @return
   * @see java.util.List#addAll(java.util.Collection)
   */
  @Override
  public boolean addAll(Collection<? extends E> arg0) {
    sorted = false;
    return items.addAll(arg0);
  }

  /**
   * @param arg0
   * @param arg1
   * @return
   * @see java.util.List#addAll(int, java.util.Collection)
   */
  @Override
  public boolean addAll(int arg0, Collection<? extends E> arg1) {
    sorted = false;
    return items.addAll(arg0, arg1);
  }

  /**
   *
   * @see java.util.List#clear()
   */
  @Override
  public void clear() {
    sorted = true;
    items.clear();
  }

  /**
   * @param object
   * @return
   * @see java.util.List#contains(java.lang.Object)
   */
  @Override
  public boolean contains(Object object) {
    return items.contains(object);
  }

  /**
   * @param arg0
   * @return
   * @see java.util.List#containsAll(java.util.Collection)
   */
  @Override
  public boolean containsAll(Collection<?> arg0) {
    return items.containsAll(arg0);
  }

  /**
   * @param object
   * @return
   * @see java.util.List#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    sort();
    return items.equals(object);
  }

  /**
   * @param location
   * @return
   * @see java.util.List#get(int)
   */
  @Override
  public E get(int location) {
    sort();
    return items.get(location);
  }

  /**
   * @return
   * @see java.util.List#hashCode()
   */
  @Override
  public int hashCode() {
    sort();
    return items.hashCode();
  }

  /**
   * @param object
   * @return
   * @see java.util.List#indexOf(java.lang.Object)
   */
  @Override
  public int indexOf(Object object) {
    sort();
    return items.indexOf(object);
  }

  /**
   * @return
   * @see java.util.List#isEmpty()
   */
  @Override
  public boolean isEmpty() {
    return items.isEmpty();
  }

  /**
   * @return
   * @see java.util.List#iterator()
   */
  @Override
  public Iterator<E> iterator() {
    sort();
    return items.iterator();
  }

  /**
   * @param object
   * @return
   * @see java.util.List#lastIndexOf(java.lang.Object)
   */
  @Override
  public int lastIndexOf(Object object) {
    sort();
    return items.lastIndexOf(object);
  }

  /**
   * @return
   * @see java.util.List#listIterator()
   */
  @Override
  public ListIterator<E> listIterator() {
    sort();
    return items.listIterator();
  }

  /**
   * @param location
   * @return
   * @see java.util.List#listIterator(int)
   */
  @Override
  public ListIterator<E> listIterator(int location) {
    sort();
    return items.listIterator(location);
  }

  /**
   * @param location
   * @return
   * @see java.util.List#remove(int)
   */
  @Override
  public E remove(int location) {
    sort();
    return items.remove(location);
  }

  /**
   * @param object
   * @return
   * @see java.util.List#remove(java.lang.Object)
   */
  @Override
  public boolean remove(Object object) {
    sort();
    return items.remove(object);
  }

  /**
   * @param arg0
   * @return
   * @see java.util.List#removeAll(java.util.Collection)
   */
  @Override
  public boolean removeAll(Collection<?> arg0) {
    sort();
    return items.removeAll(arg0);
  }

  /**
   * @param arg0
   * @return
   * @see java.util.List#retainAll(java.util.Collection)
   */
  @Override
  public boolean retainAll(Collection<?> arg0) {
    sort();
    return items.retainAll(arg0);
  }

  /**
   * @param location
   * @param object
   * @return
   * @see java.util.List#set(int, java.lang.Object)
   */
  @Override
  public E set(int location, E object) {
    sorted = false;
    return items.set(location, object);
  }

  /**
   * @return
   * @see java.util.List#size()
   */
  @Override
  public int size() {
    return items.size();
  }

  /**
   * @param start
   * @param end
   * @return
   * @see java.util.List#subList(int, int)
   */
  @Override
  public List<E> subList(int start, int end) {
    sort();
    return items.subList(start, end);
  }

  /**
   * @return
   * @see java.util.List#toArray()
   */
  @Override
  public Object[] toArray() {
    sort();
    return items.toArray();
  }

  /**
   * @param <T>
   * @param array
   * @return
   * @see java.util.List#toArray(T[])
   */
  @Override
  public <T> T[] toArray(T[] array) {
    sort();
    return items.toArray(array);
  }

  @Override
  public final String toString() {
    return Printer.toString(this);
  }

  @Override
  public void print(PrintWriter out, int depth) {
    Printer.print(out, (Object)this, depth);
    out.format(" size=%s, sorted=%s", size(), sorted);
    for (E item : items) {
      out.println();
      if (item instanceof Printable) {
        ((Printable)item).print(out, depth+1);
      } else {
        Printer.print(out, item, depth+1);
      }
    }
  }
}
