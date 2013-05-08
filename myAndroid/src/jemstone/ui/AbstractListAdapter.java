package jemstone.ui;

import java.util.List;

import jemstone.model.HasAmount;
import jemstone.model.HasDescription;
import jemstone.model.HasName;
import jemstone.myandroid.R;
import jemstone.util.Formatter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public abstract class AbstractListAdapter<T extends HasName> extends BaseAdapter {
  private final LayoutInflater inflater;
  private final List<T> items;
  private final int layoutId;
  private final ItemSelectionHandler<T> itemSelectionHandler;
  private final OnClickListener onNextClickListener;
  private final Formatter formatter = new Formatter();

  public AbstractListAdapter(Context context, List<T> items, int layoutId) {
    this(context, items, layoutId, null);
  }
  
  public AbstractListAdapter(Context context, List<T> items, int layoutId, View.OnClickListener onNextClickListener) {
    super();
    this.items = items;
    this.layoutId = layoutId;
    this.onNextClickListener = onNextClickListener;

    inflater = LayoutInflater.from(context);
    itemSelectionHandler = new ItemSelectionHandler<T>();
  }

  public LayoutInflater getInflater() {
    return inflater;
  }

  public int getLayoutId() {
    return layoutId;
  }

  public ItemSelectionHandler<T> getItemSelectionHandler() {
    return itemSelectionHandler;
  }

  public List<T> getSelectedItems() {
    return itemSelectionHandler.getSelectedItems();
  }

  public void selectItem(int position, boolean selected) {
    T item = getItem(position);
    itemSelectionHandler.selectItem(item, selected);
  }

  public void selectItems(List<T> items, boolean selected) {
    for (T item : items) {
      itemSelectionHandler.selectItem(item, selected);
    }
  }

  public boolean isSelectionMode() {
    return itemSelectionHandler.isSelectionMode();
  }

  public void setSelectionMode(boolean selectionMode) {
    itemSelectionHandler.setSelectionMode(selectionMode);
  }

  @Override
  public int getCount() {
    return items.size();
  }

  @Override
  public T getItem(int position) {
    return items.get(position);
  }

  @Override
  public long getItemId(int position) {
    return items.get(position).getId();
  }
  
  public int getItemPosition(T item) {
    return items.indexOf(item);
  }
  
  public void scrollTo(ListView list, T item) {
    if (item != null) {
      int position = getItemPosition(item);
      if (position >= 0) {
        list.smoothScrollToPosition(position);
      }
    }
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    if (view == null) {
      view = inflater.inflate(layoutId, parent, false);
    }
  
    final T item = getItem(position);
  
    TextView name = (TextView)view.findViewById(R.id.name);
    if (name != null) {
      name.setText(item.getName());
    }
  
    if (item instanceof HasDescription) {
      TextView description = (TextView)view.findViewById(R.id.description);
      if (description != null) {
        description.setText(((HasDescription) item).getDescription());
      }
    }
    
    if (item instanceof HasAmount) {
      TextView amount = (TextView)view.findViewById(R.id.amount);
      if (amount != null) {
        String text = formatter.formatAmount(((HasAmount) item).getAmount());
        amount.setText(text);
      }
    }
  
    // Initialize the selection checkbox
    CheckBox checkBox = (CheckBox) view.findViewById(R.id.selectCheckBox);
    getItemSelectionHandler().initCheckBox(checkBox, isSelectionMode() ? item : null);
    
    // Get Goto button
    if (onNextClickListener != null) {
      View next = view.findViewById(R.id.next);
      if (next != null) {
        next.setOnClickListener(onNextClickListener);
        next.setTag(getItem(position));
        next.setVisibility(View.VISIBLE);
      }
    }

    return view;
  }
}