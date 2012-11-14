package jemstone.ui;

import java.util.ArrayList;
import java.util.List;

import jemstone.model.HasName;
import jemstone.myandroid.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public abstract class AbstractExpandableListAdapter<T extends HasName> extends BaseExpandableListAdapter {
  protected final Context context;
  private final LayoutInflater inflater;
  private final List<String> groups;
  private final List<List<? extends T>> children;
  private final int groupLayoutId;
  
  private final ItemSelectionHandler<T> itemSelectionHandler;

  public AbstractExpandableListAdapter(Context context, int groupLayoutId) {
    this.context = context;
    this.groupLayoutId = groupLayoutId;
    inflater = LayoutInflater.from(context);
    groups = new ArrayList<String>();
    children = new ArrayList<List<? extends T>>();
    itemSelectionHandler = new ItemSelectionHandler<T>();
  }
  
  public void reset() {
    groups.clear();
    children.clear();
    setSelectionMode(false);
  }

  public ItemSelectionHandler<T> getItemSelectionHandler() {
    return itemSelectionHandler;
  }

  public boolean isSelectionMode() {
    return itemSelectionHandler.isSelectionMode();
  }

  public void setSelectionMode(boolean selectionMode) {
    itemSelectionHandler.setSelectionMode(selectionMode);
  }

  public List<T> getSelectedItems() {
    return itemSelectionHandler.getSelectedItems();
  }

  public void selectItem(int groupPosition, int childPosition, boolean selected) {
    T item = getChild(groupPosition, childPosition);
    itemSelectionHandler.selectItem(item, selected);
  }

  /**
   * Add an empty group
   * @param group
   * @param children
   */
  public void addGroup(String group) {
    this.groups.add(group);
    this.children.add(new ArrayList<T>());
  }

  /**
   * Add a group and list of children ONLY IF <code>children</code> is not empty.
   * @param group
   * @param children
   */
  public void addGroup(String group, List<? extends T> children) {
    if (children != null && !children.isEmpty()) {
      this.groups.add(group);
      this.children.add(children);
    }
  }

  @Override
  public T getChild(int groupPosition, int childPosition) {
    try {
      return children.get(groupPosition).get(childPosition);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    try {
      return getChild(groupPosition, childPosition).getId();
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    try {
      return children.get(groupPosition).size();
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  public String getGroup(int groupPosition) {
    try {
      return groups.get(groupPosition);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public int getGroupCount() {
    try {
      return groups.size();
    } catch (Exception e) {
      return 0;
    }
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    View view = convertView;
    if (view == null) {
      view = inflater.inflate(groupLayoutId, parent, false);
    }

    TextView nameView = (TextView)view.findViewById(R.id.name);
    if (nameView != null) {
      nameView.setText(getGroup(groupPosition));
    }

    TextView countView = (TextView)view.findViewById(R.id.count);
    if (countView != null) {
      countView.setText("(" + getChildrenCount(groupPosition) + ")");
    }
    return view;
  }
}
