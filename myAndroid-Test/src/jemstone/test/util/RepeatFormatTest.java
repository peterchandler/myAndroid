package jemstone.test.util;

import java.util.Calendar;

import jemstone.util.DateUtil;
import jemstone.util.Repeat;
import jemstone.util.RepeatFormat;
import android.test.AndroidTestCase;

public class RepeatFormatTest extends AndroidTestCase {
  public void testNullDate() {
    Repeat repeat = new Repeat();
    RepeatFormat format = new RepeatFormat();
    String text = format.format(repeat, null);

    System.out.println(text);
  }

  public void testSixMonthly() {
    Calendar date = DateUtil.calendar(2011, Calendar.SEPTEMBER, 1);
    Repeat repeat = new Repeat().setMonthly(6);
    RepeatFormat format = new RepeatFormat();
    String text = format.format(repeat, date.getTime());

    System.out.println(text);
  }
}
