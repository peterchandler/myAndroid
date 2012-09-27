package jemstone.ui;

import jemstone.util.log.Logger;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class MyGestureListener extends SimpleOnGestureListener {
  private static final Logger log = Logger.getLogger(MyGestureListener.class);

  private static final int SWIPE_MIN_DISTANCE = 100;
  private static final int SWIPE_MAX_OFF_PATH = 250;
  private static final int SWIPE_THRESHOLD_VELOCITY = 50;

  private View view;

  public MyGestureListener(View view) {
    super();
    this.view = view;
  }

  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    final float y1 = e1.getY();
    final float y2 = e2.getY();

    if (Math.abs(y1 - y2) > SWIPE_MAX_OFF_PATH) {
      log.debug("onFling Ignored: [y1=%.0f, y2=%.0f]", y1, y2);
      return false;
    }

    if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) {
      log.debug("onFling Ignored: [velocityX=%.0f]", velocityX);
    } else {
      final float x1 = e1.getX();
      final float x2 = e2.getX();

      // Right-to-left swipe
      if (x1-x2 > SWIPE_MIN_DISTANCE) {
        log.debug("Left Swipe: [x1=%.0f, x2=%.0f]", x1, x2);
        cancel(e2);
        return true;
      }

      // Left-to-right swipe
      if (x2-x1 > SWIPE_MIN_DISTANCE) {
        log.debug("Right Swipe: [x1=%.0f, x2=%.0f]", x1, x2);
        cancel(e2);
        return true;
      }
    }

    return false;
  }

  private void cancel(MotionEvent e2) {
    MotionEvent cancelEvent = MotionEvent.obtain(e2);
    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
    view.onTouchEvent(cancelEvent);
  }

  @Override
  public boolean onDown(MotionEvent e) {
    log.debug("onDown Ignored: [x=%.0f, y=%.0f]", e.getX(), e.getY());
    return false;
  }
}
