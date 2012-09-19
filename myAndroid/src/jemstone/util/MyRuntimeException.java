package jemstone.util;

public class MyRuntimeException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public MyRuntimeException() {
  }

  public MyRuntimeException(String detailMessage) {
    super(detailMessage);
  }

  public MyRuntimeException(String format, Object ... args) {
    super(String.format(format, args));
  }

  public MyRuntimeException(Throwable throwable) {
    super(throwable);
  }

  public MyRuntimeException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

  public MyRuntimeException(Throwable throwable, String format, Object ... args) {
    super(String.format(format, args), throwable);
  }
}
