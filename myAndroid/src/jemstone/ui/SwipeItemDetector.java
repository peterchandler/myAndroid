package jemstone.ui;

import android.view.View;
import android.widget.ListView;

public class SwipeItemDetector extends SwipeDetector {
  private OnSwipeItemListener listener;

  public void setListener(OnSwipeItemListener listener) {
    this.listener = listener;
  }

  @Override
  protected void onSwipeLeft(View view, float x, float y) {
    ListView list = (ListView)view;
    int position = list.pointToPosition((int)x, (int)y);
    View item = list.getChildAt(position - list.getFirstVisiblePosition());

    if (log.isTraceEnabled()) {
      String listClass = view.getClass().getSimpleName();
      String itemClass = (item != null) ? item.getClass().getSimpleName() : "null";

      log.trace("onSwipeLeft: [list=%s, item=%s, position=%d, x=%.0f, y=%.0f]", listClass, itemClass, position, x, y);
    }

    onSwipeItemLeft(list, item, position, x, y);
  }

  @Override
  protected void onSwipeRight(View view, float x, float y) {
    ListView list = (ListView)view;
    int position = list.pointToPosition((int)x, (int)y);
    View item = list.getChildAt(position - list.getFirstVisiblePosition());

    if (log.isTraceEnabled()) {
      String listClass = view.getClass().getSimpleName();
      String itemClass = (item != null) ? item.getClass().getSimpleName() : "null";

      log.trace("onSwipeRight: [list=%s, item=%s, position=%d, x=%.0f, y=%.0f]", listClass, itemClass, position, x, y);
    }

    onSwipeItemRight(list, item, position, x, y);
  }

  protected void onSwipeItemLeft(ListView list, View item, int position, float x, float y) {
    if (listener != null) {
      listener.onSwipeLeft(list, item, position, x, y);
    }
  }

  protected void onSwipeItemRight(ListView list, View item, int position, float x, float y) {
    if (listener != null) {
      listener.onSwipeRight(list, item, position, x, y);
    }
  }

  public interface OnSwipeItemListener {
    public void onSwipeLeft(ListView parent, View item, int position, float x, float y);
    public void onSwipeRight(ListView parent, View item, int position, float x, float y);
  }
}
