package jemstone.util;

public class DaoException extends Exception {

  private static final long serialVersionUID = 1L;

  public DaoException() {
    super();
  }

  public DaoException(String detailMessage) {
    super(detailMessage);
  }

  public DaoException(String format, Object ... args) {
    super(String.format(format, args));
  }

  public DaoException(Throwable throwable) {
    super(throwable);
  }

  public DaoException(Throwable throwable, String detailMessage) {
    super(detailMessage, throwable);
  }

  public DaoException(Throwable throwable, String format, Object ... args) {
    super(String.format(format, args), throwable);
  }
}
