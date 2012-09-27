package jemstone.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Very simple wrapper to encapsulate dependency upon Android Support package
 */
public class BaseViewPager extends ViewPager {
  public BaseViewPager(Context context) {
    super(context);
  }

  public BaseViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
}
