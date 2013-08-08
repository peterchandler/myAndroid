package jemstone.util;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.util.Date;


public class RepeatFormat {
  private final StringBuffer str = new StringBuffer();
  private final DateFormat dateFormat = Formatter.getDateInstance();
  private final DateFormat monthYearFormat = Formatter.getDateMonthYearInstance();
  private final DateFormatSymbols symbols = new DateFormatSymbols();
  
  public static String DEFAULT_PREFIX = "Occurs";

  public RepeatFormat() {
  }

  public String format(Repeat repeat, Date startDate) {
    return format(repeat, startDate, null, null);
  }

  public String format(Repeat repeat, Date startDate, String prefix) {
    return format(repeat, startDate, null, prefix);
  }

  public String format(Repeat repeat, Date startDate, Date finishDate) {
    return format(repeat, startDate, finishDate, null);
  }

  public String format(Repeat repeat, Date startDate, Date finishDate, String prefix) {
    str.setLength(0);
    str.append(prefix != null ? prefix : DEFAULT_PREFIX);
    str.append(' ');

    if (repeat.isSingle()) {
      str.append("once");
      if (startDate != null) {
        str.append(" on ");
        dateFormat.format(startDate, str, new FieldPosition(0));
      }
    }

    else if (repeat.isDaily()) {
      append(repeat);
    }

    // If weekly then include days of week
    else if (repeat.isWeekly()) {
      str.append("every ");

      if (repeat.getEvery() > 1) {
        str.append(Formatter.getNumberWithSuffix(repeat.getEvery()));
      }

      int[] daysOfWeek = repeat.getDaysOfWeek();
      if (daysOfWeek != null) {
        appendDaysOfWeek(daysOfWeek);
      } else {
        str.append(getUnits(repeat));
      }
    }

    // Monthly repeats can be
    else if (repeat.isMonthly()) {
      // If daysOfWeek are specified they take priority
      int[] daysOfWeek = repeat.getDaysOfWeek();
      if (daysOfWeek != null) {
        str.append("on the ");

        int dayOfWeekInMonth = repeat.getDayOfWeekInMonth();
        if (dayOfWeekInMonth == -1) {
          str.append("last ");
        } else {
          str.append(Formatter.getNumberWithSuffix(dayOfWeekInMonth));
        }
        appendDaysOfWeek(daysOfWeek);
        str.append(" of ");
      }

      // No daysOfWeek so use dayOfMonth
      else {
        appendDayOfMonth(repeat);
      }

      append(repeat);
    }

    else if (repeat.isYearly()) {
      appendDayOfMonth(repeat);

      int month = repeat.getMonthOfYear();
      if (month != -1) {
        str.append(getMonth(month));
        str.append(' ');
      }

      append(repeat);
    }

    if (finishDate != null) {
      str.append(" until ");
      monthYearFormat.format(finishDate, str, new FieldPosition(0));
    }

    return str.toString();
  }

  private void appendDayOfMonth(Repeat repeat) {
    int dayOfMonth = repeat.getDayOfMonth();
    if (dayOfMonth == -1) {
      str.append("on the last day of ");
    } else if (dayOfMonth != 0) {
      str.append("on the ");
      str.append(Formatter.getNumberWithSuffix(dayOfMonth));
      str.append("of ");
    }
  }

  private void append(Repeat repeat) {
    str.append("every ");

    if (repeat.getEvery() == 1) {
      str.append(getUnits(repeat));
    } else {
      str.append(Formatter.getNumberWithSuffix(repeat.getEvery()));
      str.append(getUnits(repeat));
    }
  }

  private void appendDaysOfWeek(int[] daysOfWeek) {
    boolean comma = false;
    for (int dayOfWeek : daysOfWeek) {
      if (comma) {
        str.append(", ");
      }
      str.append(getDayOfWeek(dayOfWeek));
      comma = true;
    }
  }

  private String getUnits(Repeat repeat) {
    switch (repeat.getFrequency()) {
      case DAILY:   return "day";
      case WEEKLY:  return "week";
      case MONTHLY: return "month";
      case YEARLY:  return "year";
      case SINGLE:
      default:
        return null;
    }
  }

  public String getDayOfWeek(int dayOfWeek) {
    return symbols.getWeekdays()[dayOfWeek];
  }

  public String getShortMonth(int month) {
    return symbols.getShortMonths()[month];
  }

  public String getMonth(int month) {
    return symbols.getMonths()[month];
  }
}
