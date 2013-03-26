package jemstone.util.compare;

import jemstone.util.Repeat;

public class RepeatComparator extends BaseComparator<Repeat> {
  public RepeatComparator(boolean compareId) {
    super(compareId);
  }

  @Override
  public boolean equals(Repeat o1, Repeat o2) throws CompareException {
    if (checkNull("Repeat", o1, o2)) {
      return (o1 == o2);
    }

    try {
      boolean result = true;
      result &= equals("frequency", o1.getFrequency(), o2.getFrequency());
      result &= equals("every", o1.getEvery(), o2.getEvery());
      result &= equals("dayOfMonth", o1.getDayOfMonth(), o2.getDayOfMonth());
      result &= equals("dayOfWeekInMonth", o1.getDayOfWeekInMonth(), o2.getDayOfWeekInMonth());
      result &= equals("daysOfWeek", o1.getDaysOfWeek(), o2.getDaysOfWeek());
      result &= equals("monthOfYear", o1.getMonthOfYear(), o2.getMonthOfYear());
      return result;
    } catch (CompareException e) {
      e.add("Error comparing Repeat objects");
      throw e;
    }
  }
}
