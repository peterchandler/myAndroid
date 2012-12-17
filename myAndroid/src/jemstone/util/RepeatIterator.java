package jemstone.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;


/**
 * Iterate over future dates that conform to the rules defined in the {@link Repeat}.
 * The {@link Calendar} class is used to compute these dates.
 * <p>
 * Note that the start date specified by the {@link Calendar} is NEVER returned in the
 * results - all dates will be later than this date.
 */
public class RepeatIterator implements Iterator<Calendar> {
  private final Repeat repeat;

  private final Calendar next;

  private final Calendar current;

  private final Calendar finish;

  private int daysOfWeekIndex = -1;

  private boolean firstOccursAfterStart = false;

  private boolean firstRepeat = true;

  public RepeatIterator(Repeat repeat, Calendar start) {
    this(repeat, start, null);
  }

  public RepeatIterator(Repeat repeat, Calendar start, Calendar finish) {
    this.repeat = repeat.clean();
    this.next = DateUtil.calendar(start);
    this.finish = finish;
    this.current = DateUtil.calendar(start);

    setMonth();
    setDayOfMonth();
    setDayOfWeek();

    if (next.compareTo(start) < 0) {
      if (repeat.getDayOfMonth() != 0 || repeat.getDayOfWeekInMonth() != 0) {
        next.add(repeat.isMonthly() ? Calendar.MONTH : Calendar.YEAR, 1);
        setDayOfMonth();
        setDayOfWeek();
      } else if (repeat.getDaysOfWeek() != null) {
        next.add(Calendar.WEEK_OF_YEAR, 1);
      }
    }

    firstOccursAfterStart = (next.compareTo(start) >= 0);
  }

  public RepeatIterator(Repeat repeat, Date start) {
    this(repeat, DateUtil.calendar(start), null);
  }

  public RepeatIterator(Repeat repeat, Date start, Date finish) {
    this(repeat, DateUtil.calendar(start), DateUtil.calendar(finish));
  }

  public Calendar getCurrent() {
    return current;
  }

  /**
   * Calculate the first occurrence of the specified repeat that occurs AFTER the current time.
   * @param repeat
   * @param startDate
   * @return
   */
  public static Calendar getNextOccurrence(Repeat repeat, Date startDate) {
    Calendar start = (startDate != null) ? DateUtil.calendar(startDate) : DateUtil.getCurrentDate();
    Calendar now = DateUtil.getCurrentDate();
    Calendar next = null;

    RepeatIterator iterator = new RepeatIterator(repeat, start);
    while (iterator.hasNext()) {
      next = iterator.next();
      if (next.after(now)) {
        break;
      }
    }

    return next;
  }

  @Override
  public boolean hasNext() {
    if (firstOccursAfterStart) {
      firstOccursAfterStart = false;
      if (firstRepeat) {
        firstRepeat = false;
      }
    } else {
      if (repeat.isSingle()) {
        if (firstRepeat) {
          firstRepeat = false;
          return true;
        }
        return false;
      }

      if (repeat.isDaily()) {
        nextDay();
      } else if (repeat.isWeekly()) {
        nextWeek();
      } else if (repeat.isMonthly()) {
        nextMonth();
      } else if (repeat.isYearly()) {
        nextYear();
      }
    }
    return (finish == null || finish.compareTo(next) >= 0);
  }

  @Override
  public void remove() {
  }

  @Override
  public Calendar next() {
    current.setTimeInMillis(next.getTimeInMillis());
    return current;
  }

  private void nextDay() {
    next.add(Calendar.DAY_OF_YEAR, repeat.getEvery());
  }

  private void nextWeek() {
    int[] daysOfWeek = repeat.getDaysOfWeek();

    if (daysOfWeek == null || daysOfWeek.length == 0) {
      next.add(Calendar.WEEK_OF_YEAR, repeat.getEvery());
    } else {
      if (++daysOfWeekIndex >= daysOfWeek.length) {
        daysOfWeekIndex = 0;
        next.set(Calendar.DAY_OF_WEEK, daysOfWeek[0]);
        next.add(Calendar.WEEK_OF_YEAR, repeat.getEvery());
      }

      while (next.get(Calendar.DAY_OF_WEEK) != daysOfWeek[daysOfWeekIndex]) {
        next.add(Calendar.DAY_OF_YEAR, 1);
      }
    }
  }

  private void nextMonth() {
    next.add(Calendar.MONTH, repeat.getEvery());
    setDayOfMonth();
    setDayOfWeek();
  }

  private void nextYear() {
    next.add(Calendar.YEAR, repeat.getEvery());
    setMonth();
  }

  /**
   * Update {@link #next()} with the month specified in {@link Repeat#getMonthOfYear()},
   * and try to ensure that the day-of-month does not change.
   */
  private void setMonth() {
    int monthOfYear = repeat.getMonthOfYear();
    if (monthOfYear != -1) {
      next.set(Calendar.MONTH, monthOfYear);
      setDayOfMonth();
    }
  }

  private void setDayOfMonth() {
    final int daysInMonth = next.getActualMaximum(Calendar.DAY_OF_MONTH);
    int dayOfMonth = repeat.getDayOfMonth();
    if (dayOfMonth == -1 || dayOfMonth > daysInMonth) {
      dayOfMonth = daysInMonth;
    }

    if (dayOfMonth != 0) {
      next.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }
  }

  private void setDayOfWeek() {
    int[] daysOfWeek = repeat.getDaysOfWeek();
    if (daysOfWeek != null) {
      daysOfWeekIndex = 0;
      next.set(Calendar.DAY_OF_WEEK, daysOfWeek[0]);

      int dayOfWeekInMonth = repeat.getDayOfWeekInMonth();
      if (dayOfWeekInMonth != 0) {
        next.set(Calendar.DAY_OF_WEEK_IN_MONTH, dayOfWeekInMonth);
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("RepeatIterator [repeat=");
    builder.append(repeat);
    builder.append(", current=");
    builder.append(String.format("%1$te-%1$tm-%1$tY", current));
    builder.append(", next=");
    builder.append(String.format("%1$te-%1$tm-%1$tY", next));
    builder.append(", finish=");
    builder.append(String.format("%1$te-%1$tm-%1$tY", finish));
    builder.append(", daysOfWeekIndex=");
    builder.append(daysOfWeekIndex);
    builder.append(", firstRepeatAfterStart=");
    builder.append(firstOccursAfterStart);
    builder.append(", firstRepeat=");
    builder.append(firstRepeat);
    builder.append("]");
    return builder.toString();
  }
}
