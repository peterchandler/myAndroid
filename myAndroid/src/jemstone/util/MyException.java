package jemstone.util;

public class MyException extends Exception {
  private static final long serialVersionUID = 1L;

  public MyException() {
  }

  public MyException(String detailMessage) {
    super(detailMessage);
  }

  public MyException(String format, Object ... args) {
    super(String.format(format, args));
  }

  public MyException(Throwable throwable) {
    super(throwable);
  }

  public MyException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public MyException(Throwable throwable, String format, Object ... args) {
    super(String.format(format, args), throwable);
  }
}
