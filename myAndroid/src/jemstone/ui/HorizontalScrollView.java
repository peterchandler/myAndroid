package jemstone.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizontalScrollView extends android.widget.HorizontalScrollView {
  private float xDistance, yDistance, lastX, lastY;

  public HorizontalScrollView(Context context) {
    super(context);
  }

  public HorizontalScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public HorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        xDistance = yDistance = 0f;
        lastX = ev.getX();
        lastY = ev.getY();
        break;

      case MotionEvent.ACTION_MOVE:
        final float curX = ev.getX();
        final float curY = ev.getY();
        xDistance += Math.abs(curX - lastX);
        yDistance += Math.abs(curY - lastY);
        lastX = curX;
        lastY = curY;
        if (xDistance < yDistance)
          return false;
    }

    return super.onInterceptTouchEvent(ev);
  }
}
