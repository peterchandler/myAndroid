package jemstone.test.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jemstone.model.HasDate;
import jemstone.util.DateOrderedList;
import jemstone.util.DateUtil;
import junit.framework.Assert;
import android.test.AndroidTestCase;

public class OrderedListTest extends AndroidTestCase {
  private DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
  private DateOrderedList<DateItem> list;

  private Calendar date1 = DateUtil.calendar(2011, Calendar.SEPTEMBER, 1);
  private Calendar date2 = DateUtil.calendar(2011, Calendar.SEPTEMBER, 15);
  private Calendar date3 = DateUtil.calendar(2011, Calendar.SEPTEMBER, 17);
  private Calendar date4 = DateUtil.calendar(2011, Calendar.SEPTEMBER, 9);
  private Calendar date5 = DateUtil.calendar(2011, Calendar.SEPTEMBER, 4);
  private Calendar date6 = DateUtil.calendar(2010, Calendar.SEPTEMBER, 15);
  private Calendar date7 = DateUtil.calendar(2012, Calendar.SEPTEMBER, 15);

  public OrderedListTest() {
    list = new DateOrderedList<DateItem>();
    list.add(new DateItem(date1));
    list.add(new DateItem(date2));
    list.add(new DateItem(date3));
    list.add(new DateItem(date4));
    list.add(new DateItem(date5));
    list.add(new DateItem(date6));
    list.add(new DateItem(date7));
  }

  public void testDateList() {
    Date actual = list.get(0).getDate();
    assertEquals(date6, actual);

    actual = list.get(list.size()-1).getDate();
    assertEquals(date7, actual);

    actual = list.get(4).getDate();
    assertEquals(date2, actual);
  }

  public void testFind() {
    DateItem actual = list.find(date3.getTime());
    DateItem expected = list.get(list.size()-2);

    assertEquals(expected, actual);
  }

  public void testFindOnOrBefore() {
    Calendar date = DateUtil.calendar(2011, Calendar.SEPTEMBER, 14);
    DateItem actual = list.findOnOrBefore(date.getTime());

    assertEquals(date4, actual.getDate());
  }

  public void testFindBetween() {
    Calendar from = DateUtil.calendar(2011, Calendar.SEPTEMBER, 2);
    Calendar to = DateUtil.calendar(2011, Calendar.SEPTEMBER, 16);

    List<DateItem> sublist = list.findBetween(from.getTime(), to.getTime());

//    System.out.println(list);
//    System.out.println(sublist);

    assertEquals(3, sublist.size());

    Date first = sublist.get(0).getDate();
    assertEquals(date5, first);

    Date last = sublist.get(2).getDate();
    assertEquals(date2, last);
  }

  private void assertEquals(Date expected, Date actual) {
    Assert.assertEquals((Object)dateFormat.format(expected), (Object)dateFormat.format(actual));
  }

  private void assertEquals(Calendar expected, Date actual) {
    assertEquals(expected.getTime(), actual);
  }

  private static class DateItem implements HasDate {
    private Date date;

    public DateItem(Calendar calendar) {
      this.date = calendar.getTime();
    }

    @Override
    public Date getDate() {
      return date;
    }
  }
}
