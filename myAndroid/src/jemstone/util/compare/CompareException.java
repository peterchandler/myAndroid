package jemstone.util.compare;

import java.util.ArrayList;
import java.util.List;

public class CompareException extends Exception {
  private static final long serialVersionUID = 1L;

  private List<String> messages = new ArrayList<String>();

  public CompareException(Throwable t) {
    messages.add(t.toString());
  }

  public CompareException(String message) {
    messages.add(message);
  }

  public CompareException(String format, Object ... args) {
    add(format, args);
  }

  public void add(String format, Object ... args) {
    messages.add(String.format(format, args));
  }

  @Override
  public String getMessage() {
    StringBuilder str = new StringBuilder();
    for (int i = messages.size()-1;  i >= 0;  i--) {
      str.append('\n');
      str.append(i+1);
      str.append(": ");
      str.append(messages.get(i));
    }
    return str.toString();
  }
}
