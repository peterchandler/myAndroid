package jemstone.ui;

import jemstone.util.log.Logger;
import android.view.MotionEvent;
import android.view.View;

/**
 * Detects left-to-right and right-to-left swipes on a {@link View}
 */
public class SwipeDetector implements View.OnTouchListener {
  protected static final Logger log = Logger.getLogger(SwipeDetector.class);

//  private static final ScreenUtils utils = new ScreenUtils();
  private static final int SWIPE_MIN_DISTANCE = 75;
  private static final int SWIPE_MAX_OFF_PATH = 100;

  private float xDelta, yDelta, lastX, lastY;

  private boolean isSwiped;

  private OnSwipeListener listener;

  public SwipeDetector() {
  }

  public void setListener(OnSwipeListener listener) {
    this.listener = listener;
  }

  @Override
  public boolean onTouch(View view, MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        xDelta = yDelta = 0f;
        lastX = event.getX();
        lastY = event.getY();
        isSwiped = false;
        onActionDown(view, lastX, lastY);
        break;

      case MotionEvent.ACTION_MOVE:
        final float curX = event.getX();
        final float curY = event.getY();
        xDelta += Math.abs(curX - lastX);
        yDelta += Math.abs(curY - lastY);

        if (xDelta < SWIPE_MIN_DISTANCE || yDelta > SWIPE_MAX_OFF_PATH) {
          lastX = curX;
          lastY = curY;
          return false;
        }

        if (!isSwiped) {
          if (curX < lastX) {
            onSwipeLeft(view, curX, curY);
          } else if (curX > lastX) {
            onSwipeRight(view, curX, curY);
          }

          // Don't trigger twice
          isSwiped = true;
        }

        lastX = curX;
        lastY = curY;

        MotionEvent cancelEvent = MotionEvent.obtain(event);
        cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
        view.onTouchEvent(cancelEvent);
        return true;
    }

    return false;
  }

  protected void onActionDown(View view, float x, float y) {
    log.trace("onActionDown: [view=%s, x=%.0f, y=%.0f]", view.getClass().getSimpleName(), x, y);
  }

  protected void onSwipeLeft(View view, float x, float y) {
    log.trace("onSwipeLeft: [view=%s, x=%.0f, y=%.0f]", view.getClass().getSimpleName(), x, y);
    if (listener != null) {
      listener.onSwipeLeft(view, x, y);
    }
  }

  protected void onSwipeRight(View view, float x, float y) {
    log.trace("onSwipeRight: [view=%s, x=%.0f, y=%.0f]", view.getClass().getSimpleName(), x, y);
    if (listener != null) {
      listener.onSwipeRight(view, x, y);
    }
  }

  public interface OnSwipeListener {
    public void onSwipeLeft(View view, float x, float y);
    public void onSwipeRight(View view, float x, float y);
  }
}
