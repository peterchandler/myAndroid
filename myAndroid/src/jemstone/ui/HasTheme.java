package jemstone.ui;

import android.content.res.Resources.Theme;

public interface HasTheme {
  public Theme getTheme();
  public int getThemeColor(int attrId, int defaultColor);
}
