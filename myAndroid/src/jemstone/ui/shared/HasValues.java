package jemstone.ui.shared;

import jemstone.util.Repeat;

public interface HasValues {
  /**
   * Get the current item from the wheel and update the {@link Repeat}
   */
  public abstract void getValues();

  /**
   * Set the current item in the wheel from the {@link Repeat}
   */
  public abstract void setValues();
}