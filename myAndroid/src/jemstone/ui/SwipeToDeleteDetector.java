package jemstone.ui;

import jemstone.myandroid.R;
import android.view.View;
import android.widget.ListView;

/**
 * When the user swipes right-to-left over a list item, reveal a Delete button (if one is
 * found in the list item). If the user clicks this then fire the onItemDelete method on
 * {@link #listener}.
 */
public class SwipeToDeleteDetector extends SwipeItemDetector {
  private OnItemDeleteListener listener;
  private View deleteButton;

  public void setListener(OnItemDeleteListener listener) {
    this.listener = listener;
  }

  public void hideDeleteButton() {
    if (deleteButton != null) {
      deleteButton.setVisibility(View.GONE);
      deleteButton.setOnClickListener(null);
      deleteButton = null;
    }
  }

  public void revealDeleteButton(ListView list, View item, int position) {
    // Reveal delete button if found
    if (item == null) {
      deleteButton = null;
    } else {
      deleteButton = item.findViewById(R.id.delete);
      if (deleteButton != null) {
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(new OnClickDeleteButton(list, position));
      }
    }
  }

  @Override
  protected void onActionDown(View view, float x, float y) {
    super.onActionDown(view, x, y);
    hideDeleteButton();
  }

  @Override
  protected void onSwipeItemLeft(ListView list, View item, int position, float x, float y) {
    revealDeleteButton(list, item, position);
  }

  private class OnClickDeleteButton implements View.OnClickListener {
    private final ListView list;
    private final int position;

    public OnClickDeleteButton(ListView list, int position) {
      this.list = list;
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      hideDeleteButton();
      if (listener != null) {
        listener.onItemDelete(list, position);
      }
    }
  }

  public interface OnItemDeleteListener {
    public void onItemDelete(ListView list, int position);
  }
}
