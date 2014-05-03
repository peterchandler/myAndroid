package jemstone.test.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jemstone.util.DateUtil;
import jemstone.util.Repeat;
import jemstone.util.Repeat.Frequency;
import jemstone.util.RepeatIterator;
import jemstone.util.compare.CompareException;
import jemstone.util.compare.RepeatComparator;
import junit.framework.Assert;
import android.test.AndroidTestCase;

public class RepeatTest extends AndroidTestCase {
  private DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

  private void assertEquals(Date expected, Date actual) {
    Assert.assertEquals((Object)dateFormat.format(expected), (Object)dateFormat.format(actual));
  }

  private void assertEquals(Calendar expected, Calendar actual) {
    assertEquals(expected.getTime(), actual.getTime());
  }

  private void assertNotEquals(Calendar expected, Calendar actual) {
//    assertEquals(expected.getTime(), actual.getTime());
  }

  public void testDaily1Day() {
    // Test 5 days
    Calendar start = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected = DateUtil.calendar(2011, Calendar.JUNE, 16);
    Calendar actual = null;

    Repeat repeat = new Repeat().setDaily(1);
    RepeatIterator iterator = new RepeatIterator(repeat, start);
    for (int i=0;  i < 5 && iterator.hasNext();  i++) {
      actual = iterator.next();
    }

    assertEquals(expected, actual);
  }

  public void testDaily2Days() {
    // Test 20 days, 2 day increments
    Calendar start = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected = DateUtil.calendar(2011, Calendar.JUNE, 30);
    Calendar actual = null;

    Repeat repeat = new Repeat().setDaily(2);
    RepeatIterator iterator = new RepeatIterator(repeat, start);
    for (int i=0;  i < 10 && iterator.hasNext();  i++) {
      actual = iterator.next();
      //    DateUtil.log(actual);
    }

    assertEquals(expected, actual);
  }

  public void testWeekly1Week() {
    // Test 5 weeks
    Calendar start = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected = DateUtil.calendar(2011, Calendar.JULY, 10);
    Calendar actual = null;

    Repeat repeat = new Repeat().setWeekly(1);
    RepeatIterator iterator = new RepeatIterator(repeat, start);
    for (int i=0;  i < 5 && iterator.hasNext();  i++) {
      actual = iterator.next();
      //      DateUtil.log(actual);
    }

    assertEquals(expected, actual);
  }

  public void testWeeklyDateRange() {
    // Test repeat on the 20th of every month
    Calendar start = DateUtil.calendar(2011, Calendar.JANUARY, 1);
    Calendar finish = DateUtil.calendar(2011, Calendar.JANUARY, 31);
    Calendar expected = DateUtil.calendar(2011, Calendar.JANUARY, 29);
    Calendar actual = null;

    Repeat repeat = new Repeat().setWeekly(1);
    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);

    int count = 0;
    while (iterator.hasNext()) {
      count++;
      actual = iterator.next();
      //      DateUtil.log(actual);
    }

    assertEquals(expected, actual);
    assertEquals(5, count);
  }

  public void testWeeklyEveryTuesday() {
    // Test 1st Tuesday
    Calendar start = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected = DateUtil.calendar(2011, Calendar.JUNE, 14);
    Calendar actual = null;

    Repeat repeat = new Repeat();
    repeat.setWeekly(1, Calendar.TUESDAY);
    RepeatIterator iterator = new RepeatIterator(repeat, start);
    if (iterator.hasNext()) {
      actual = iterator.next();
      //    DateUtil.log(calendar);
    }

    assertEquals(expected, actual);
  }

  public void testWeeklyEverySunday() {
    // Test 1st Sunday
    Calendar start = DateUtil.calendar(2011, Calendar.JUNE, 13);
    Calendar expected = DateUtil.calendar(2011, Calendar.JUNE, 26);
    Calendar actual = null;

    Repeat repeat = new Repeat();
    repeat.setWeekly(1, Calendar.SUNDAY);

    RepeatIterator iterator = new RepeatIterator(repeat, start);
    for (int i=0;  i < 2 && iterator.hasNext();  i++) {
      actual = iterator.next();
      //      DateUtil.log(calendar);
    }

    assertEquals(expected, actual);
  }

  public void testWeeklyEveryWedFri() {
    // Test every Wed/Fri for 5 weeks
    Calendar start = DateUtil.calendar(2011, Calendar.JUNE, 15);
    Calendar expected = DateUtil.calendar(2011, Calendar.JULY, 15);
    Calendar actual = null;

    Repeat repeat = new Repeat();
    repeat.setWeekly(1, Calendar.WEDNESDAY, Calendar.FRIDAY);

    RepeatIterator iterator = new RepeatIterator(repeat, start);
    for (int i=0;  i < 10 && iterator.hasNext();  i++) {
      actual = iterator.next();
      //      DateUtil.log(actual);
    }

    assertEquals(expected, actual);
  }

  public void testFortnightly() {
    // Test 10 weeks
    Calendar start = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected = DateUtil.calendar(2011, Calendar.AUGUST, 7);
    Calendar actual = null;

    Repeat repeat = new Repeat().setWeekly(2);
    RepeatIterator iterator = new RepeatIterator(repeat, start);
    for (int i=0;  i < 5 && iterator.hasNext();  i++) {
      actual = iterator.next();
      //    DateUtil.log(actual);
    }

    assertEquals(expected, actual);
  }

  /**
   * Test that the first repeat date is the first Monday AFTER the specified start date
   */
  public void testFortnightlyStartDate() {
    Calendar start = DateUtil.calendar(2011, Calendar.JANUARY, 1);
    Calendar expected = DateUtil.calendar(2011, Calendar.JANUARY, 3);
    Calendar actual = null;

    Repeat repeat = new Repeat().setWeekly(2, Calendar.MONDAY);
    RepeatIterator iterator = new RepeatIterator(repeat, start);
    if (iterator.hasNext()) {
      actual = iterator.next();
      //    DateUtil.log(actual);
    }

    assertEquals(expected, actual);
  }

  public void testFortnightlyWednesday() {
    // Test every 2nd Wed for 8 fortnights
    Calendar calendar = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected = DateUtil.calendar(2011, Calendar.SEPTEMBER, 21);

    Repeat repeat = new Repeat();
    repeat.setWeekly(2, Calendar.WEDNESDAY);

    RepeatIterator iterator = new RepeatIterator(repeat, calendar);
    for (int i=0;  i < 8 && iterator.hasNext();  i++) {
      calendar = iterator.next();
      //      DateUtil.log(calendar);
    }

    assertEquals(expected, calendar);
  }

  public void testFortnightlyThuSat() {
    // Test every 2nd Thu/Sat for 8 fortnights
    Calendar calendar = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected = DateUtil.calendar(2011, Calendar.SEPTEMBER, 24);

    Repeat repeat = new Repeat();
    repeat.setWeekly(2, Calendar.THURSDAY, Calendar.SATURDAY);

    RepeatIterator iterator = new RepeatIterator(repeat, calendar);
    for (int i=0;  i < 16 && iterator.hasNext();  i++) {
      calendar = iterator.next();
      //      DateUtil.log(calendar);
    }

    assertEquals(expected, calendar);
  }

  public void testMonthlyEveryThirdWed() {
    // Test monthly
    Calendar calendar = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected = DateUtil.calendar(2011, Calendar.AUGUST, 17);

    Repeat repeat = new Repeat();
    repeat.setMonthlyDays(1, Calendar.WEDNESDAY);
    repeat.setDayOfWeekInMonth(3);

    RepeatIterator iterator = new RepeatIterator(repeat, calendar);
    for (int i=0;  i < 3 && iterator.hasNext();  i++) {
      calendar = iterator.next();
    }

    assertEquals(expected, calendar);
  }

  public void testMonthlyEveryThirdWedAndFri() {
    // Test monthly
    Calendar calendar = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected1 = DateUtil.calendar(2011, Calendar.AUGUST, 19);
    Calendar expected2 = DateUtil.calendar(2011, Calendar.NOVEMBER, 16);

    Repeat repeat = new Repeat();
    repeat.setMonthlyDays(1, Calendar.WEDNESDAY, Calendar.FRIDAY);
    repeat.setDayOfWeekInMonth(3);

    RepeatIterator iterator = new RepeatIterator(repeat, calendar);
    for (int i=0;  i < 6 && iterator.hasNext();  i++) {
      calendar = iterator.next();
    }

    // If multiple days were supported this would be true...
    assertNotEquals(expected1, calendar);

    // ... but they aren't so this is true
    assertEquals(expected2, calendar);
  }

  public void testMonthlyFrom31st() {
    // Test monthly, starting 31st
    Calendar calendar = DateUtil.calendar(2011, Calendar.MAY, 31);
    Calendar expected = DateUtil.calendar(2011, Calendar.SEPTEMBER, 30);

    Repeat repeat = new Repeat().setMonthly(1);
    RepeatIterator iterator = new RepeatIterator(repeat, calendar);
    for (int i=0;  i < 5 && iterator.hasNext();  i++) {
      calendar = iterator.next();
      //      DateUtil.log(calendar);
    }

    assertEquals(expected, calendar);
  }

  public void testBiMonthly31st() {
    // Test bi-monthly, starting 31st
    Calendar calendar = DateUtil.calendar(2011, Calendar.MAY, 31);
    Calendar expected = DateUtil.calendar(2011, Calendar.SEPTEMBER, 30);

    Repeat repeat = new Repeat().setMonthly(2);
    RepeatIterator iterator = new RepeatIterator(repeat, calendar);
    for (int i=0;  i < 3 && iterator.hasNext();  i++) {
      calendar = iterator.next();
      //      DateUtil.log(calendar);
    }

    assertEquals(expected, calendar);
  }

  public void testMonthly10th() {
    // Test repeat on the 10th of every month
    Calendar start = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected = DateUtil.calendar(2011, Calendar.AUGUST, 10);
    Calendar actual = null;

    Repeat repeat = new Repeat().setMonthly(1, 10);
    RepeatIterator iterator = new RepeatIterator(repeat, start);
    for (int i=0;  i < 2 && iterator.hasNext();  i++) {
      actual = iterator.next();
      //      DateUtil.log(calendar);
    }

    assertEquals(expected, actual);
  }

  public void testMonthly12th() {
    // Test repeat on the 20th of every month
    Calendar calendar = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected = DateUtil.calendar(2011, Calendar.AUGUST, 12);

    Repeat repeat = new Repeat().setMonthly(1);
    repeat.setDayOfMonth(12);

    RepeatIterator iterator = new RepeatIterator(repeat, calendar);
    for (int i=0;  i < 3 && iterator.hasNext();  i++) {
      calendar = iterator.next();
      //      DateUtil.log(calendar);
    }

    assertEquals(expected, calendar);
  }

  public void testMonthly20th1() {
    // Test repeat on the 20th of every month
    Calendar calendar = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected = DateUtil.calendar(2011, Calendar.AUGUST, 20);

    Repeat repeat = new Repeat().setMonthly(1, 20);
    RepeatIterator iterator = new RepeatIterator(repeat, calendar);
    for (int i=0;  i < 3 && iterator.hasNext();  i++) {
      calendar = iterator.next();
      //      DateUtil.log(calendar);
    }

    assertEquals(expected, calendar);
  }

  public void testMonthly20th2() {
    // Test repeat on the 20th of every month
    Calendar calendar = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected = DateUtil.calendar(2011, Calendar.AUGUST, 20);

    Repeat repeat = new Repeat().setMonthly(1, 20);
    RepeatIterator iterator = new RepeatIterator(repeat, calendar);
    for (int i=0;  i < 3 && iterator.hasNext();  i++) {
      calendar = iterator.next();
      //      DateUtil.log(calendar);
    }

    assertEquals(expected, calendar);
  }

  public void testMonthly20thFor4Months() {
    Calendar actual = null;

    // Test repeat on the 20th of every month
    Calendar start = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar finish = DateUtil.calendar(2011, Calendar.SEPTEMBER, 30);
    Calendar expected = DateUtil.calendar(2011, Calendar.SEPTEMBER, 20);

    Repeat repeat = new Repeat().setMonthly(1, 20);
    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    while (iterator.hasNext()) {
      actual = iterator.next();
      //        DateUtil.log(calendar);
    }

    assertEquals(expected, actual);
  }

  /**
   * Test that the first repeat date is on the specified start date, when it is a Monday
   */
  public void testMonthlyDays() {
    Calendar start = DateUtil.calendar(2011, Calendar.JANUARY, 1);
    Calendar expected = DateUtil.calendar(2011, Calendar.JANUARY, 3);
    Calendar actual = null;

    Repeat repeat = new Repeat().setMonthlyDays(2, Calendar.MONDAY);
    RepeatIterator iterator = new RepeatIterator(repeat, start);
    if (iterator.hasNext()) {
      actual = iterator.next();
      //    DateUtil.log(actual);
    }

    assertEquals(expected, actual);
  }

  public void testFirstFridayOfMonth() {
    // Test repeat on the last Fri of every month
    Calendar calendar = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar expected = DateUtil.calendar(2011, Calendar.AUGUST, 5);

    Repeat repeat = new Repeat().setMonthly(1);
    repeat.setDaysOfWeek(Calendar.FRIDAY);
    repeat.setDayOfWeekInMonth(1);

    RepeatIterator iterator = new RepeatIterator(repeat, calendar);
    for (int i=0;  i < 2 && iterator.hasNext();  i++) {
      calendar = iterator.next();
      //      DateUtil.log(calendar);
    }

    assertEquals(expected, calendar);
  }

  public void testFirstSundayOfMonth() {
    // Test repeat on the first Sunday of every month
    Calendar start = DateUtil.calendar(2011, Calendar.JUNE, 5);
    Calendar expected = DateUtil.calendar(2011, Calendar.AUGUST, 7);

    Repeat repeat = new Repeat().setMonthly(1);
    repeat.setDaysOfWeek(Calendar.SUNDAY);
    repeat.setDayOfWeekInMonth(1);

    RepeatIterator iterator = new RepeatIterator(repeat, start);
    for (int i=0;  i < 3 && iterator.hasNext();  i++) {
      start = iterator.next();
      //      DateUtil.log(calendar);
    }

    assertEquals(expected, start);
  }

  public void testFirstSundayOfMonth2() {
    // Test repeat on the first Sunday of every month
    Calendar start = DateUtil.calendar(2012, Calendar.MARCH, 11);
    Calendar expected = DateUtil.calendar(2012, Calendar.APRIL, 1);

    Repeat repeat = new Repeat().setMonthly(6);
    repeat.setDaysOfWeek(Calendar.SUNDAY);
    repeat.setDayOfWeekInMonth(1);

    Calendar actual = getNextOccurrence(repeat, start);
    assertEquals(expected, actual);
  }

  public void testLastFridayOfMonth() {
    // Test repeat on the last Fri of every month
    Calendar start = DateUtil.calendar(2011, Calendar.JANUARY, 1);
    Calendar finish = DateUtil.calendar(2011, Calendar.MARCH, 31);
    Calendar expected = DateUtil.calendar(2011, Calendar.MARCH, 25);
    Calendar actual = null;

    Repeat repeat = new Repeat();
    repeat.setMonthlyDays(1, Calendar.FRIDAY);
    repeat.setDayOfWeekInMonth(-1);

    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    while (iterator.hasNext()) {
      actual = iterator.next();
      //      DateUtil.log(calendar);
    }

    assertEquals(expected, actual);
  }

  public void testLastFridayOfMonthOverLeapYear() {
    // Test repeat on the last Fri of every month
    Calendar start = DateUtil.calendar(2012, Calendar.JANUARY, 1);
    Calendar finish = DateUtil.calendar(2012, Calendar.MARCH, 31);
    Calendar expected = DateUtil.calendar(2012, Calendar.MARCH, 30);
    Calendar actual = null;

    Repeat repeat = new Repeat().setMonthlyDays(1, Calendar.FRIDAY);
    repeat.setDayOfWeekInMonth(-1);

    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    while (iterator.hasNext()) {
      actual = iterator.next();
      //      DateUtil.log(calendar);
    }

    assertEquals(expected, actual);
  }

  public void testLastDayOfMonth() {
    // Test repeat on the last Fri of every month
    Calendar start = DateUtil.calendar(2011, Calendar.JANUARY, 1);
    Calendar finish = DateUtil.calendar(2011, Calendar.MARCH, 15);
    Calendar expected = DateUtil.calendar(2011, Calendar.FEBRUARY, 28);
    Calendar actual = null;

    Repeat repeat = new Repeat();
    repeat.setMonthly(1, Repeat.LAST_DAY_OF_MONTH);

    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    while (iterator.hasNext()) {
      actual = iterator.next();
    }

    assertEquals(expected, actual);
  }

  public void testLastDayOfMonthLeapYear() {
    // Test repeat on the last Fri of every month
    Calendar start = DateUtil.calendar(2012, Calendar.JANUARY, 1);
    Calendar finish = DateUtil.calendar(2012, Calendar.MARCH, 15);
    Calendar expected = DateUtil.calendar(2012, Calendar.FEBRUARY, 29);
    Calendar actual = null;

    Repeat repeat = new Repeat();
    repeat.setMonthly(1, Repeat.LAST_DAY_OF_MONTH);

    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    while (iterator.hasNext()) {
      actual = iterator.next();
    }

    assertEquals(expected, actual);
  }

  public void testMonthlyTransition1() {
//    Logger.logHeading(this, "testMonthlyTransition1");

    Calendar start = DateUtil.calendar(2012, Calendar.MARCH, 8);
    Calendar expected = DateUtil.calendar(2012, Calendar.MARCH, 11);
    Calendar actual = null;

    Repeat repeat = new Repeat().setWeekly(1, Calendar.SUNDAY);
    repeat.setDayOfMonth(1);

    RepeatIterator iterator = new RepeatIterator(repeat, start, null);
    if (iterator.hasNext()) {
      actual = iterator.next();
//      Logger.log(actual);
    }

//    RepeatFormat format = new RepeatFormat();
//    Logger.log(format.format(repeat, start.getTime()));

    assertEquals(Frequency.WEEKLY, repeat.getFrequency());
    assertEquals(expected, actual);
  }

  public void testMonthlyTransition2() {
      Calendar start = DateUtil.calendar(2012, Calendar.MARCH, 8);
      Calendar expected1 = DateUtil.calendar(2012, Calendar.MARCH, 10);
      Calendar expected2 = DateUtil.calendar(2012, Calendar.MARCH, 20);

      Repeat repeat = new Repeat().setMonthly(1, 10);
      Calendar actual1 = getNextOccurrence(repeat, start);

      repeat.setDaysOfWeek(Calendar.TUESDAY);
      repeat.setDayOfWeekInMonth(3);
      Calendar actual2 = getNextOccurrence(repeat, start);

//      Logger.logHeading(this, "testMonthlyTransition2");
//      Logger.log(actual1);
//      Logger.log(actual2);

      assertEquals(expected1, actual1);
      assertEquals(expected2, actual2);

  //    assertEquals(Frequency.WEEKLY, repeat.getFrequency());
    }

  public void testOnce() {
    Calendar start = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar finish = DateUtil.calendar(2011, Calendar.AUGUST, 31);
    Calendar expected = DateUtil.calendar(2011, Calendar.JUNE, 12);
    Calendar actual = null;

    // Test with null repeat
    int count = 0;
    Repeat repeat = new Repeat();
    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    while (iterator.hasNext()) {
      actual = iterator.next();
      count++;
    }

    assertEquals(expected, actual);
    assertEquals(1, count);
  }

  public void testWeeklyNoLeapYear() {
    // Test repeat over a leap year boundary
    Calendar start = DateUtil.calendar(2011, Calendar.FEBRUARY, 1);
    Calendar finish = DateUtil.calendar(2011, Calendar.MARCH, 1);
    Calendar expected = DateUtil.calendar(2011, Calendar.MARCH, 1);
    Calendar actual = null;

    Repeat repeat = new Repeat().setWeekly(1);
    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    while (iterator.hasNext()) {
      actual = iterator.next();
//    DateUtil.log(actual);
    }

    assertEquals(expected, actual);
  }

  public void testWeeklyLeapYear() {
    // Test repeat over a leap year boundary
    Calendar start = DateUtil.calendar(2012, Calendar.FEBRUARY, 1);
    Calendar finish = DateUtil.calendar(2012, Calendar.MARCH, 1);
    Calendar expected = DateUtil.calendar(2012, Calendar.FEBRUARY, 29);
    Calendar actual = null;

    Repeat repeat = new Repeat().setWeekly(1);
    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    while (iterator.hasNext()) {
      actual = iterator.next();
//    DateUtil.log(actual);
    }

    assertEquals(expected, actual);
  }

  public void testWeeklyLeapYear1() {
    // Test repeat over a leap year boundary
    Calendar start = DateUtil.calendar(2012, Calendar.FEBRUARY, 1);
    Calendar finish = DateUtil.calendar(2012, Calendar.MARCH, 31);
    Calendar expected = DateUtil.calendar(2012, Calendar.MARCH, 28);
    Calendar actual = null;

    Repeat repeat = new Repeat().setWeekly(1);
    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    while (iterator.hasNext()) {
      actual = iterator.next();
//      DateUtil.log(actual);
    }

    assertEquals(expected, actual);
  }

  public Calendar getNextOccurrence(Repeat repeat, Calendar start) {
    RepeatIterator iterator = new RepeatIterator(repeat, start);
    if (iterator.hasNext()) {
      return iterator.next();
    }

    return null;
  }

  public void testYearly() {
    // Test repeat over a leap year boundary
    Calendar start = DateUtil.calendar(2012, Calendar.FEBRUARY, 1);
    Calendar finish = DateUtil.calendar(2014, Calendar.MARCH, 31);
    Calendar expected = DateUtil.calendar(2014, Calendar.FEBRUARY, 1);
    Calendar actual = null;

    Repeat repeat = new Repeat().setYearly(1);
    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    while (iterator.hasNext()) {
      actual = iterator.next();
//      DateUtil.log(actual);
    }

    assertEquals(expected, actual);
  }

  public void testYearly2() {
    // Test repeat over a leap year boundary
    Calendar start = DateUtil.calendar(2012, Calendar.FEBRUARY, 1);
    Calendar finish = DateUtil.calendar(2017, Calendar.MARCH, 31);
    Calendar expected = DateUtil.calendar(2017, Calendar.MARCH, 20);
    Calendar actual = null;

    Repeat repeat = new Repeat().setYearly(1, 20, Calendar.MARCH);
    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    int count = 0;
    while (iterator.hasNext()) {
      actual = iterator.next();
      count++;
    }

    assertEquals(expected, actual);
    assertEquals(6, count);
  }

  public void testYearly3() {
    // Test repeat over a leap year boundary
    Calendar start = DateUtil.calendar(2012, Calendar.FEBRUARY, 1);
    Calendar finish = DateUtil.calendar(2017, Calendar.MARCH, 1);
    Calendar expected = DateUtil.calendar(2016, Calendar.MARCH, 20);
    Calendar actual = null;

    Repeat repeat = new Repeat().setYearly(1, 20, Calendar.MARCH);
    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    int count = 0;
    while (iterator.hasNext()) {
      actual = iterator.next();
      count++;
    }

    assertEquals(expected, actual);
    assertEquals(5, count);
  }

  public void testYearly4() {
    Calendar start = DateUtil.calendar(2012, Calendar.SEPTEMBER, 11);
    Calendar expected = DateUtil.calendar(2013, Calendar.SEPTEMBER, 10);

    Repeat repeat = new Repeat().setYearly(1, 10, Calendar.SEPTEMBER);
    Calendar actual = getNextOccurrence(repeat, start);

    assertEquals(expected, actual);
  }

  public void testYearlyLastDayOfMarch() {
    // Test repeat over a leap year boundary
    Calendar start = DateUtil.calendar(2012, Calendar.FEBRUARY, 1);
    Calendar finish = DateUtil.calendar(2017, Calendar.MARCH, 30);
    Calendar expected = DateUtil.calendar(2016, Calendar.MARCH, 31);
    Calendar actual = null;

    Repeat repeat = new Repeat().setYearly(2, Repeat.LAST_DAY_OF_MONTH, Calendar.MARCH);
    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    int count = 0;
    while (iterator.hasNext()) {
      actual = iterator.next();
      count++;
    }

    assertEquals(expected, actual);
    assertEquals(3, count);
  }

  public void testYearlyLastDayOfFeb() {
//    Logger.logHeading(this, "testYearlyLastDayOfFeb");

    // Test repeat over a leap year boundary
    Calendar start = DateUtil.calendar(2011, Calendar.JANUARY, 1);
    Calendar finish = DateUtil.calendar(2012, Calendar.MARCH, 30);
    Calendar expected[] = { DateUtil.calendar(2011, Calendar.FEBRUARY, 28),
                            DateUtil.calendar(2012, Calendar.FEBRUARY, 29) };
    Calendar actual = null;

    Repeat repeat = new Repeat().setYearly(1, Repeat.LAST_DAY_OF_MONTH, Calendar.FEBRUARY);
    RepeatIterator iterator = new RepeatIterator(repeat, start, finish);
    int count = 0;
    while (iterator.hasNext()) {
      actual = iterator.next();
//      Logger.log(actual);

      assertEquals(expected[count], actual);
      count++;
    }

    assertEquals(2, count);
  }

  public void testClone1() throws CompareException, AssertionError {
    Repeat repeat1 = new Repeat().setMonthly(2);
    Repeat repeat2 = repeat1.clone();

    assertNotSame(repeat1, repeat2);
    new RepeatComparator(true).assertEquals(repeat1, repeat2);
  }

  public void testClone2() throws CompareException, AssertionError {
    Repeat repeat1 = new Repeat().setMonthly(2, 20);
    Repeat repeat2 = repeat1.clone();

    assertNotSame(repeat1, repeat2);
    new RepeatComparator(true).assertEquals(repeat1, repeat2);
  }

  public void testClone3() throws CompareException, AssertionError {
    Repeat repeat1 = new Repeat().setWeekly(2);
    Repeat repeat2 = repeat1.clone();

    assertNotSame(repeat1, repeat2);
    new RepeatComparator(true).assertEquals(repeat1, repeat2);
  }

  public void testClone4() throws CompareException, AssertionError {
    Repeat repeat1 = new Repeat().setWeekly(2, Calendar.MONDAY, Calendar.WEDNESDAY);
    Repeat repeat2 = repeat1.clone();

    assertNotSame(repeat1, repeat2);
    new RepeatComparator(true).assertEquals(repeat1, repeat2);

    repeat2.setDaysOfWeek(Calendar.THURSDAY);
    new RepeatComparator(true).assertNotEquals(repeat1, repeat2);
  }
}
