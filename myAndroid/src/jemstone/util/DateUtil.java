package jemstone.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
  private static final int MILLIS_PER_DAY = 24 * 60 * 60000;

  public static Calendar getCurrentTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;
  }

  public static Calendar getCurrentDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;
  }

  public static Calendar calendar(int year, int month, int day) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day, 0,0,0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;
  }

  public static Calendar calendar(Date date) {
    if (date != null) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar;
    }
    return null;
  }

  public static Calendar calendar(Calendar other) {
    if (other != null) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(other.getTimeInMillis());
      return calendar;
    }
    return null;
  }

  public static boolean isBetween(Calendar calendar, Calendar from, Calendar to) {
    long fromMillis = (from != null) ? from.getTimeInMillis() : 0;
    long toMillis = (to != null) ? to.getTimeInMillis() : Long.MAX_VALUE;
    return isBetween(calendar.getTimeInMillis(), fromMillis, toMillis);
  }

  public static boolean isBetween(Date date, Date from, Date to) {
    long fromMillis = (from != null) ? from.getTime() : 0;
    long toMillis = (to != null) ? to.getTime() : Long.MAX_VALUE;
    return isBetween(date.getTime(), fromMillis, toMillis);
  }

  public static boolean isBetween(long millis, long fromMillis, long toMillis) {
    if (millis < fromMillis || millis > toMillis) {
      return false;
    }
    return true;
  }

  /**
   * Calculate difference in days between two {@link Calendar} objects, taking daylight
   * savings and timezone offsets into account.
   *
   * @param from
   * @param to
   * @return
   */
  public static int diffDays(Calendar from, Calendar to) {
    long fromMillis = from.getTimeInMillis();
    fromMillis += getOffset(from);

    long toMillis = to.getTimeInMillis();
    toMillis += getOffset(to);

    return (int)((toMillis - fromMillis) / MILLIS_PER_DAY);
  }

  private static long getOffset(Calendar calendar) {
    return calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
  }
}
