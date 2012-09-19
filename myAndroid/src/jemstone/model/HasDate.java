package jemstone.model;

import java.util.Date;

import jemstone.util.compare.DateComparator;

/**
 * Simple interface to be implemented by any class that has a Date property. Specifically
 * used by {@link DateComparator} to enable easy date sorting.
 */
public interface HasDate {
  public Date getDate();
}
