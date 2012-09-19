package jemstone.util;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;



/**
 * Describes the frequency with which a transaction repeats. Supported models include:
 * <ul>
 * <li>Repeat one/daily/weekly/monthly/yearly</li>
 * <li>Repeat once of the first date specified</li>
 * <li>Repeat every n days/weeks/months/years</li>
 * <li>Repeat every n weeks on the specified day(s) of the week</li>
 * <li>Repeat every n months on the nth day of the month</li>
 * <li>Repeat every n months on the nth Mon/Tue/Wed/Thu/Fri/Sat/Sun of the month</li>
 * <li>Repeat every n years on the nth day of the specified month</li>
 * </ul>
 * <p>The {@link Calendar} class is used to compute future dates that conform to the
 * rules defined here. Call the {@link #iterator(Calendar)} method to get
 * an {@link Iterator} object which will compute the future dates for you. Note that the
 * start date specified by the {@link Calendar} passed to {@link #iterator(Calendar)} is
 * never returned in the results - all dates will be later than this date.
 */
public class Repeat implements Printable, Cloneable, Serializable {
  private static final long serialVersionUID = 1L;

  /** The frequency with which repeats will occur */
  public enum Frequency { SINGLE, DAILY, WEEKLY, MONTHLY, YEARLY };

  /** Used to indicate the last day of the month where {@link #dayOfMonth} is used */
  public static final int LAST_DAY_OF_MONTH = -1;

  /** Reusable value for one-off repeats */
  public static final Repeat SINGLE  = new Repeat();
  public static final Repeat DAILY   = new Repeat().setDaily(1);
  public static final Repeat WEEKLY  = new Repeat().setWeekly(1);
  public static final Repeat MONTHLY = new Repeat().setMonthly(1,1);
  public static final Repeat YEARLY  = new Repeat().setYearly(1,1,1);

  /**
   * Repeat daily/weekly/monthly/yearly.
   * @see Frequency
   */
  private Frequency frequency = Frequency.SINGLE;

  /**
   * Repeat every n days/weeks/months/years
   */
  private int every = 1;

  /**
   * Repeat weekly on the specified day(s) of the week e.g. every Mon/Fri.
   * <p>An array of {@link Calendar#DAY_OF_WEEK} values.
   */
  private int daysOfWeek[];

  /**
   * Repeat monthly on the nth day of the month e.g. the 20th of the month.
   * <p>Must be a valid {@link Calendar#DAY_OF_MONTH} value, or {@link #LAST_DAY_OF_MONTH}
   * for the last day of the month.
   */
  private int dayOfMonth = 0;

  /**
   * Repeat monthly on the nth Mon/Tue/Wed/Thu/Fri/Sat/Sun of the month. For example, the
   * 1st Wed of the month, the 2nd Sat of the month, the last Fri of the month.
   * <p>
   * Must be a valid {@link Calendar#DAY_OF_WEEK_IN_MONTH} value.
   */
  private int dayOfWeekInMonth = 0;

  /**
   * Repeat yearly on the nth day of the specified month e.g. the 20th of September.
   * <p>Must be a valid {@link Calendar#MONTH} value.
   */
  private int monthOfYear = -1;

  public Repeat() {
  }

  public Repeat(Frequency frequency) {
    this.frequency = frequency;
  }

  public boolean isSingle() {
    return (frequency == Frequency.SINGLE);
  }

  public boolean isDaily() {
    return (frequency == Frequency.DAILY);
  }

  public boolean isWeekly() {
    return (frequency == Frequency.WEEKLY);
  }

  public boolean isMonthly() {
    return (frequency == Frequency.MONTHLY);
  }

  public boolean isDayOfMonth() {
    return (frequency == Frequency.MONTHLY) && (dayOfMonth != 0);
  }

  public boolean isDayOfWeekInMonth() {
    return (frequency == Frequency.MONTHLY) && (daysOfWeek != null || dayOfWeekInMonth != 0);
  }

  public boolean isYearly() {
    return (frequency == Frequency.YEARLY);
  }

  public Repeat setDaily(int days) {
    this.frequency = Frequency.DAILY;
    this.every = days;
    return this;
  }

  public Repeat setWeekly(int weeks) {
    return setWeekly(weeks, null);
  }

  public Repeat setWeekly(int weeks, int ... daysOfWeek) {
    this.frequency = Frequency.WEEKLY;
    this.every = weeks;
    this.daysOfWeek = daysOfWeek;
    return this;
  }

  public Repeat setMonthly(int months) {
    this.frequency = Frequency.MONTHLY;
    this.every = months;
    return this;
  }

  public Repeat setMonthly(int months, int dayOfMonth) {
    this.frequency = Frequency.MONTHLY;
    this.every = months;
    this.dayOfMonth = dayOfMonth;
    return this;
  }

  public Repeat setMonthlyDays(int months, int ... daysOfWeek) {
    this.frequency = Frequency.MONTHLY;
    this.every = months;
    this.daysOfWeek = daysOfWeek;
    return this;
  }

  public Repeat setYearly(int years) {
    this.frequency = Frequency.YEARLY;
    this.every = years;
    return this;
  }

  public Repeat setYearly(int every, int dayOfMonth, int monthOfYear) {
    this.frequency = Frequency.YEARLY;
    this.every = every;
    this.dayOfMonth = dayOfMonth;
    this.monthOfYear = monthOfYear;
    return this;
  }

  /**
   * @return {@link Frequency} of a repeat, in conjunction with {@link #getEvery()}
   */
  public Frequency getFrequency() {
    return frequency;
  }

  /**
   * Set the {@link Frequency} of a repeat, in conjunction with {@link #setEvery(int)}.
   */
  public void setFrequency(Frequency frequency) {
    this.frequency = frequency;
  }

  /**
   * @return the every
   */
  public int getEvery() {
    return every;
  }

  /**
   * Repeat every n days/weeks/months/years
   * @param every the numbers of days/weeks/months/years to repeat after
   */
  public void setEvery(int every) {
    this.every = every;
  }

  /**
   * @return An array of {@link Calendar#DAY_OF_WEEK} values
   */
  public int[] getDaysOfWeek() {
    return daysOfWeek;
  }

  /**
   * Repeat weekly on the specified day(s) of the week e.g. every Mon/Fri.
   * @param daysOfWeek An array of {@link Calendar#DAY_OF_WEEK} values
   */
  public void setDaysOfWeek(int ... daysOfWeek) {
    this.daysOfWeek = daysOfWeek;
  }

  public void setDaysOfWeek(ArrayList<Integer> daysOfWeek) {
    int size = daysOfWeek.size();
    if (size > 0) {
      this.daysOfWeek = new int[size];
      for (int i=0;  i < size;  i++) {
        this.daysOfWeek[i] = daysOfWeek.get(i);
      }
    } else {
      this.daysOfWeek = null;
    }
  }

  /**
   * Add an item to the {@link #daysOfWeek} array. Use instead of or in conjunction with
   * {@link #setDaysOfWeek(int...)}.
   * @param dayOfWeek
   */
  public void addDayOfWeek(int dayOfWeek) {
    if (daysOfWeek == null) {
      daysOfWeek = new int[] { dayOfWeek };
    } else if (!isDayOfWeekSet(dayOfWeek)) {
      final int len = daysOfWeek.length;
      final int[] array = new int[len+1];
      System.arraycopy(daysOfWeek, 0, array, 0, len);

      daysOfWeek = array;
      daysOfWeek[len] = dayOfWeek;
    }
  }

  public boolean isDayOfWeekSet(int dayOfWeek) {
    if (daysOfWeek != null) {
      for (int i=0;  i < daysOfWeek.length;  i++) {
        if (dayOfWeek == daysOfWeek[i]) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * @return A {@link Calendar#DAY_OF_MONTH} value, or
   *          {@link #LAST_DAY_OF_MONTH} for the last day of the month
   * @see #setDayOfMonth(int)
   */
  public int getDayOfMonth() {
    return dayOfMonth;
  }

  /**
   * Repeat monthly on the nth day of the month e.g. the 20th of the month.
   *
   * @param dayOfMonth A valid {@link Calendar#DAY_OF_MONTH} value, or
   *          {@link #LAST_DAY_OF_MONTH} for the last day of the month
   */
  public Repeat setDayOfMonth(int dayOfMonth) {
    this.dayOfMonth = dayOfMonth;
    return this;
  }

  /**
   * @return A {@link Calendar#DAY_OF_WEEK_IN_MONTH} value that indicates that
   * @see #setDayOfWeekInMonth(int)
   */
  public int getDayOfWeekInMonth() {
    return dayOfWeekInMonth;
  }

  /**
   * Repeat monthly on the nth Mon/Tue/Wed/Thu/Fri/Sat/Sun of the month. For example,
   * the 1st Wed of the month, the 2nd Sat of the month.
   *
   * @param dayOfWeekInMonth A valid {@link Calendar#DAY_OF_WEEK_IN_MONTH} value
   */
  public Repeat setDayOfWeekInMonth(int dayOfWeekInMonth) {
    this.dayOfWeekInMonth = dayOfWeekInMonth;
    return this;
  }

  /**
   * Repeat yearly on the nth day of the specified month e.g. the 20th of September.
   */
  public int getMonthOfYear() {
    return monthOfYear;
  }

  /**
   * Repeat yearly on the nth day of the specified month e.g. the 20th of September.
   * @param monthOfYear Must be a valid {@link Calendar#MONTH} value
   */
  public void setMonthOfYear(int monthOfYear) {
    this.monthOfYear = monthOfYear;
  }

  @Override
  public String toString() {
    return Printer.toString(this);
  }

  @Override
  public void print(PrintWriter out, int depth) {
    Printer.indent(out, depth);
    out.format("Repeat [frequency=%s, every=%s, daysOfWeek=%s, dayOfMonth=%s, dayOfWeekInMonth=%s, monthOfYear=%s]",
                 frequency, every, Arrays.toString(daysOfWeek), dayOfMonth,
                 dayOfWeekInMonth, monthOfYear);
  }

  /**
   * Clean up the structure, ensuring consistency of all elements with the
   * specified {@link #frequency}
   * @return
   */
  public Repeat clean() {
    switch (frequency) {
      case SINGLE:
        every = 1;
        daysOfWeek = null;
        dayOfMonth = 0;
        dayOfWeekInMonth = 0;
        monthOfYear = -1;
        break;

      case DAILY:
        daysOfWeek = null;
        dayOfMonth = 0;
        dayOfWeekInMonth = 0;
        monthOfYear = -1;
        break;

      case WEEKLY:
        dayOfMonth = 0;
        dayOfWeekInMonth = 0;
        monthOfYear = -1;
        break;

      case MONTHLY:
        monthOfYear = -1;
        break;

      case YEARLY:
        daysOfWeek = null;
        dayOfWeekInMonth = 0;
        break;
    }
    return this;
  }

  @Override
  public Repeat clone() {
    try {
      return (Repeat)super.clone();
    } catch (CloneNotSupportedException e) {
      throw new MyRuntimeException(e, "Cannot clone Repeat: %s", this);
    }
  }
}
