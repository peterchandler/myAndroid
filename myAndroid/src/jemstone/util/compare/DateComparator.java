package jemstone.util.compare;

import java.util.Comparator;
import java.util.Date;

import jemstone.model.HasDate;


public class DateComparator implements Comparator<HasDate> {
  @Override
  public int compare(HasDate o1, HasDate o2) {
    Date date1 = o1.getDate();
    Date date2 = o2.getDate();

    if (date1 == null) {
      return (date2 == null) ? 0 : -1;
    } else if (date2 == null) {
      return 1;
    }
    return date1.compareTo(date2);
  }
}
