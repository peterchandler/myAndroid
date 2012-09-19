package jemstone.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

import jemstone.model.HasAmount;
import jemstone.model.HasDate;
import jemstone.model.HasId;
import jemstone.model.HasName;


public class Printer {
  public static final String DATE_FORMAT = "%1$2te-%1$2tm-%1$tY";
  public static final String AMOUNT_FORMAT = "%1$12.2f";
  public static final String PERCENT_FORMAT = "%1$6.2f%%";

  public static PrintWriter getWriter(Writer writer) {
    return new PrintWriter(writer, true);
  }

  public static void indent(PrintWriter out, int depth) {
    for (int i=0;  i < depth;  i++) {
      out.print("  ");
    }
  }

  public static void print(PrintWriter out, Object object, int depth) {
    indent(out, depth);
    print(out, object);
  }

  public static void print(PrintWriter out, String text, int depth) {
    indent(out, depth);
    out.print(text);
  }

  public static void print(PrintWriter out, Iterable<?> objects, int depth) {
    boolean newline = false;
    for (Object object : objects) {
      if (newline) {
        out.println();
      }
      if (object instanceof Printable) {
        ((Printable)object).print(out, depth);
      } else {
        print(out, object, depth);
      }
      newline = true;
    }
  }

  public static void print(PrintWriter out, Object object) {
    if (object == null) {
      out.print("null");
      return;
    }

    out.format("%-20.20s", object.getClass().getSimpleName());

    if (object instanceof HasId) {
      out.format(" id=%-4d", ((HasId)object).getId());
    }
    if (object instanceof HasName) {
      out.format(" name=%-20.20s", ((HasName)object).getName());
    }
    if (object instanceof HasDate) {
      out.print(" ");
      print(out, ((HasDate)object).getDate());
    }
    if (object instanceof HasAmount) {
      out.print(" ");
      print(out, ((HasAmount)object).getAmount());
    }
  }

  public static void print(PrintWriter out, Date date) {
    if (date == null) {
      out.print("null");
    }  else {
      out.format(DATE_FORMAT, date);
    }
  }

  public static void print(PrintWriter out, Calendar calendar) {
    out.format(DATE_FORMAT, calendar);
  }

  public static void print(PrintWriter out, double amount) {
    out.format(AMOUNT_FORMAT, amount);
  }

  public static String toString(Printable object) {
    StringWriter str = new StringWriter();
    PrintWriter out = getWriter(str);

    object.print(out, 0);

    out.close();
    return str.toString();
  }
}
