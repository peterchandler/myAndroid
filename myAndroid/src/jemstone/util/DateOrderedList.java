package jemstone.util;

import java.util.Date;

import jemstone.model.HasDate;
import jemstone.util.compare.DateComparator;

public class DateOrderedList<E extends HasDate> extends OrderedList<E> {
  public DateOrderedList() {
    super(new DateComparator());
  }

  /**
   * Find the item with the date equals to the specified date
   *
   * @param date
   * @return The first item in the list with the specified date, or <code>null</code> if
   *         none found
   */
  public E find(Date date) {
    for (E item : this) {
      int result = item.getDate().compareTo(date);
      if (result == 0) {
        return item;
      } else if (result > 0) {
        return null;
      }
    }
    return null;
  }

  /**
   * Find the item with the latest date on or before the specified date
   *
   * @param date
   * @return The last item in the list with a date on or before the specified date, or
   *         <code>null</code> if none found
   */
  public E findOnOrBefore(Date date) {
    E result = null;
    for (E item : this) {
      final Date itemDate = item.getDate();
      if (itemDate != null && itemDate.compareTo(date) > 0) {
        break;
      }
      result = item;
    }
    return result;
  }

  public DateOrderedList<E> findBetween(Date from, Date to) {
    DateOrderedList<E> list = new DateOrderedList<E>();
    for (E item : this) {
      if (item.getDate().after(to)) {
        break;
      }
      if (item.getDate().after(from)) {
        list.add(item);
      }
    }

    return list;
  }
}
