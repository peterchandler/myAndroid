package jemstone.ui.shared;

import java.util.ArrayList;
import java.util.List;

import jemstone.model.HasName;
import jemstone.util.log.Logger;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

/**
 * Handles list item selection. Note that {@link #initCheckBox(CheckBox, HasName)}
 * sets this as a click handler for the selection checkbox.
 *
 * @param <T>
 */
public class ItemSelectionHandler<T extends HasName> implements OnClickListener {
  private static final Logger log = Logger.getLogger(ItemSelectionHandler.class);

  private boolean selectionMode;
  private List<T> selectedItems;
  private OnItemSelectedListener<T> onItemSelectedListener;

  public ItemSelectionHandler() {
  }

  public boolean isSelectionMode() {
    return selectionMode;
  }

  public void setSelectionMode(boolean selectionMode) {
    if (selectionMode != this.selectionMode && selectedItems != null) {
      selectedItems.clear();
    }
    this.selectionMode = selectionMode;
  }

  public List<T> getSelectedItems() {
    return selectedItems;
  }

  public OnItemSelectedListener<T> getOnItemSelectedListener() {
    return onItemSelectedListener;
  }

  public void setOnItemSelectedListener(OnItemSelectedListener<T> onItemSelected) {
    this.onItemSelectedListener = onItemSelected;
  }

  public boolean isSelected(T item) {
    return (selectedItems == null) ? false : selectedItems.contains(item);
  }

  public void selectItem(T item, boolean selected) {
    if (item != null) {
      if (selectedItems == null) {
        selectedItems = new ArrayList<T>();
      }

      if (selected) {
        selectedItems.add(item);
        log.debug("Selected item: [%s (id=%d)]", item.getName(), item.getId());
      } else {
        selectedItems.remove(item);
        log.debug("Deselected item: [%s (id=%d)]", item.getName(), item.getId());
      }

      // Notify listener if required
      if (onItemSelectedListener != null) {
        onItemSelectedListener.onItemSelected(item, selected);
      }
    }
  }

  public void initCheckBox(CheckBox checkBox, T item) {
    if (checkBox != null) {
      if (item != null) {
        checkBox.setVisibility(View.VISIBLE);
        checkBox.setOnClickListener(this);
        checkBox.setChecked(isSelected(item));
        checkBox.setTag(item);
      } else {
        checkBox.setVisibility(View.GONE);
        checkBox.setTag(null);
      }
    }
  }

  @Override
  public void onClick(View view) {
    CheckBox checkBox = (CheckBox) view;

    @SuppressWarnings("unchecked")
    T item = (T) checkBox.getTag();
    selectItem(item, checkBox.isChecked());
  }

  public interface OnItemSelectedListener<T> {
    public void onItemSelected(T item, boolean selected);
  }
}
