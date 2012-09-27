package jemstone.test.util;

import jemstone.util.Formatter;
import android.test.AndroidTestCase;

public class ParserTest extends AndroidTestCase {
  public void testParsePercent() {
    Formatter formatter = new Formatter();
    
    assertEquals(0.05, formatter.parsePercent("5%"));
    assertEquals(0.05, formatter.parsePercent("5.0%"));
    assertEquals(-0.05, formatter.parsePercent("-5%"));
    assertEquals(-0.005, formatter.parsePercent("-.5"));
    assertEquals(0.53, formatter.parsePercent("53%"));
    assertEquals(0.05, formatter.parsePercent("5"));
    assertEquals(0.0575, formatter.parsePercent("5.75%"));
    assertEquals(1.01, formatter.parsePercent("101%"));
  }

  public void testParseAmount() {
    Formatter formatter = new Formatter();

    assertEquals(0.00, formatter.parseCurrency(""));
    assertEquals(5.00, formatter.parseCurrency("5"));
    assertEquals(5.00, formatter.parseCurrency("5.0"));
    assertEquals(5.00, formatter.parseCurrency("$5.00"));
    assertEquals(5.95, formatter.parseCurrency("$5.95"));
    assertEquals(-5.0, formatter.parseCurrency("-$5.00"));
    assertEquals(-5.0, formatter.parseCurrency("$-5.00"));
    assertEquals(-5.0, formatter.parseCurrency("-5.00"));
  }
}
