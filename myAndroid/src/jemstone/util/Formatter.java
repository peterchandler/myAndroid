package jemstone.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {
  private static String[] numbersWithSuffix;

  private final NumberFormat amountFormat = Formatter.getAmountFormat();
  private final NumberFormat currencyFormat = Formatter.getCurrencyFormat();
  private final NumberFormat percentFormat  = Formatter.getPercentFormat();
  private final DateFormat dateFormat = Formatter.getDateInstance();

  public static DateFormat getDateInstance() {
    return new SimpleDateFormat("dd-MMM-yy");
//    return DateFormat.getDateInstance(DateFormat.SHORT);
  }

  public static DateFormat getDayMonthInstance() {
    return new SimpleDateFormat("dd-MM");
  }

  public static DateFormat getDayMonthYearInstance() {
    return new SimpleDateFormat("E, MMMM d yyyy");
  }

  public static DateFormat getDateMonthYearInstance() {
    return new SimpleDateFormat("MMM-yyyy");
  }

  public static DateFormat getDateYearInstance() {
    return new SimpleDateFormat("yyyy");
  }

  public static DateFormat getDateYearMonthInstance() {
    return new SimpleDateFormat("yyyy MMMM");
  }

  public static DateFormat getDateMonthInstance() {
    return new SimpleDateFormat("MMMM");
  }

  public static NumberFormat getAmountFormat() {
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    NumberFormat format = NumberFormat.getNumberInstance();
    format.setMinimumIntegerDigits(currencyFormat.getMinimumIntegerDigits());
    format.setMinimumFractionDigits(currencyFormat.getMinimumFractionDigits());
    format.setMaximumFractionDigits(currencyFormat.getMaximumFractionDigits());
    format.setGroupingUsed(currencyFormat.isGroupingUsed());
    return format;
  }

  public static NumberFormat getCurrencyFormat() {
    NumberFormat format = NumberFormat.getCurrencyInstance();
    return format;
  }

  public static NumberFormat getPercentFormat() {
    NumberFormat format = NumberFormat.getPercentInstance();
    format.setMaximumFractionDigits(2);
    return format;
  }

  public static RepeatFormat getRepeatFormat() {
    return new RepeatFormat();
  }

  public static String getNumberWithSuffix(int index) {
    if (numbersWithSuffix == null) {
      initNumericSuffixes();
    }

    return numbersWithSuffix[index];
  }

  private static void initNumericSuffixes() {
    numbersWithSuffix = new String[100];
    StringBuilder str = new StringBuilder(30);

    for (int value=0;  value < numbersWithSuffix.length;  value++) {
      str.setLength(0);
      str.append(value);

      int rem = value % 10;
      if (rem == 1 && value != 11) {
        str.append("st ");
      } else if (rem == 2 && value != 12) {
        str.append("nd ");
      } else if (rem == 3 && value != 13) {
        str.append("rd ");
      } else {
        str.append("th ");
      }

      numbersWithSuffix[value] = str.toString();
    }
  }

  public String formatDate(Date date) {
    return (date == null) ? "" : dateFormat.format(date);
  }

  public String formatLongDate(Date date) {
    return (date == null) ? "" : String.format("%1$ta, %1$te-%1$tb-%1$tY", date);
  }

  public String formatAmount(double value) {
    return Double.isNaN(value) ? "" : amountFormat.format(value);
  }

  public String formatCurrency(double value) {
    return Double.isNaN(value) ? "" : currencyFormat.format(value);
  }

  public String formatPercent(double value) {
    return Double.isNaN(value) ? "" : percentFormat.format(value);
  }

  public String formatDouble(String format, double value) {
    return Double.isNaN(value) ? "" : String.format(format, value);
  }

  public double parsePercent(String text) {
    return parseDouble(text, true);
  }

  public double parseCurrency(String text) {
    return parseDouble(text, false);
  }

  public double parseDouble(String text) {
    return parseDouble(text, false);
  }
  
  private double parseDouble(String text, boolean isPercentage) {
    try {
      StringBuilder buf = new StringBuilder(text.length());
      
      // Strip all non-numeric characters, and record presence of % sign
      for (char ch : text.toCharArray()) {
        if (Character.isDigit(ch) || ch == '-' || ch == '.') {
          buf.append(ch);
        } else if (ch == '%') {
          isPercentage = true;
        }
      }

      // An empty string is 0
      double value = 0.0;
      if (buf.length() > 0) {
        value = Double.parseDouble(buf.toString());
      }
      if (isPercentage) {
        value /= 100.0;
      }
      
      return value;
    } catch (NumberFormatException e) {
      throw new MyRuntimeException(e, "Error parsing value '%s'", text);
    }
  }
}
