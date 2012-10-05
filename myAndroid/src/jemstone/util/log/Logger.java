package jemstone.util.log;

import org.slf4j.LoggerFactory;

public class Logger {
  private final org.slf4j.Logger logger;

  public static Logger getLogger(Class<?> clazz) {
    return new Logger(clazz);
  }

  public static Logger getLogger(Object src) {
    return new Logger(src.getClass());
  }

  protected Logger(Class<?> clazz) {
    logger = LoggerFactory.getLogger(clazz);
  }

  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  public boolean isErrorEnabled() {
    return logger.isErrorEnabled();
  }

  public boolean isInfoEnabled() {
    return logger.isInfoEnabled();
  }

  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  public boolean isWarnEnabled() {
    return logger.isWarnEnabled();
  }

  public void error(Object object) {
    if (logger.isErrorEnabled() && object != null) {
      logger.error(object.toString());
    }
  }

  public void error(Throwable e, String format, Object ... args) {
    if (logger.isErrorEnabled()) {
      logger.error(String.format(format, args), e);
    }
  }

  public void warn(Object object) {
    if (logger.isWarnEnabled() && object != null) {
      logger.warn(object.toString());
    }
  }

  public void warn(String format, Object ... args) {
    if (logger.isWarnEnabled()) {
      logger.warn(String.format(format, args));
    }
  }

  public void info(Object object) {
    if (logger.isInfoEnabled() && object != null) {
      logger.info(object.toString());
    }
  }

  public void info(String format, Object ... args) {
    if (logger.isInfoEnabled()) {
      logger.info(String.format(format, args));
    }
  }

  public void debug(Object object) {
    if (logger.isDebugEnabled() && object != null) {
      logger.debug(object.toString());
    }
  }

  public void debug(String format, Object ... args) {
    if (logger.isDebugEnabled()) {
      logger.debug(String.format(format, args));
    }
  }

  public void trace(Object object) {
    if (logger.isTraceEnabled() && object != null) {
      logger.trace(object.toString());
    }
  }

  public void trace(String format, Object ... args) {
    if (logger.isTraceEnabled()) {
      logger.trace(String.format(format, args));
    }
  }
}
