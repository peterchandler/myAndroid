package jemstone.test.util;

import java.util.Calendar;

import jemstone.util.DateUtil;
import android.test.AndroidTestCase;

public class DateUtilTest extends AndroidTestCase {
  public void testDiffDays() {
    Calendar date1 = DateUtil.calendar(2011, Calendar.SEPTEMBER, 1);
    Calendar date2 = DateUtil.calendar(2011, Calendar.SEPTEMBER, 15);

    int days = DateUtil.diffDays(date1, date2);
    assertEquals(14, days);
  }

  public void testDiffDaysWithDST() {
    Calendar date1 = DateUtil.calendar(2011, Calendar.SEPTEMBER, 20);
    Calendar date2 = DateUtil.calendar(2011, Calendar.SEPTEMBER, 28);

    int days = DateUtil.diffDays(date1, date2);
    assertEquals(8, days);
  }

  public void testDiffDaysWithDST2() {
    Calendar date1 = DateUtil.calendar(2012, Calendar.MARCH, 25);
    Calendar date2 = DateUtil.calendar(2012, Calendar.APRIL, 25);

    int days = DateUtil.diffDays(date1, date2);
    assertEquals(31, days);
  }

  public void testLastDayOfMonth() {
    Calendar date = DateUtil.calendar(2012, Calendar.MARCH, 31);

    date.add(Calendar.MONTH, 1);
    int month = date.get(Calendar.MONTH);
    int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);

    assertEquals(30, dayOfMonth);
    assertEquals(Calendar.APRIL, month);
  }

  public void testFirstDayOfWeek() {
    Calendar date1 = DateUtil.calendar(2012, Calendar.JANUARY, 1);

    int dayOfWeek = date1.getFirstDayOfWeek();

    assertEquals(Calendar.SUNDAY, dayOfWeek);
  }

  public void testMinimalDaysInFirstWeek() {
    Calendar date1 = DateUtil.calendar(2012, Calendar.JANUARY, 1);

    int result = date1.getMinimalDaysInFirstWeek();

    assertEquals(1, result);
  }

  public void testFirstWeekOfYear() {
    Calendar date1 = DateUtil.calendar(2011, Calendar.JANUARY, 1);
    Calendar date2 = DateUtil.calendar(2011, Calendar.JANUARY, 3);

    int week1 = date1.get(Calendar.WEEK_OF_YEAR);
    int week2 = date2.get(Calendar.WEEK_OF_YEAR);

    assertEquals(1, week1);
    assertEquals(2, week2);
  }
}
