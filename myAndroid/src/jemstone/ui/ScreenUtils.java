package jemstone.ui;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtils {
  private DisplayMetrics displayMetrics;
  
  public ScreenUtils(Context context) {
    displayMetrics = context.getResources().getDisplayMetrics();
  }

  /**
   * @deprecated Use <code>Context.getResources().getDimension()</code> instead, and define
   * the dimensions in dimens.xml.
   */
  @Deprecated
  public int calcPixels(int dps) {
    return (int)(displayMetrics.density * dps);
  }
}
