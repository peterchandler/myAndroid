package jemstone.ui;

import java.util.List;

import jemstone.model.HasDescription;
import jemstone.model.HasName;
import jemstone.myandroid.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AbstractActionBarDropdownAdapter<T extends HasName> extends AbstractListAdapter<T> {
  private CharSequence title;
  
  public AbstractActionBarDropdownAdapter(Context context, CharSequence title, List<T> items) {
    super(context, items, R.layout.actionbar_dropdown_item);
    this.title = title;
  }

  public CharSequence getTitle() {
    return title;
  }

  public void setTitle(CharSequence title) {
    this.title = title;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    if (view == null) {
      view = getInflater().inflate(R.layout.actionbar_dropdown_title, parent, false);
    }
  
    final T item = getItem(position);
  
    TextView title = (TextView)view.findViewById(R.id.title);
    if (title != null) {
      title.setText(this.title);
    }
  
    TextView subtitle = (TextView)view.findViewById(R.id.subtitle);
    if (subtitle != null) {
      subtitle.setText(item.getName());
    }
    
    return view;
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    if (view == null) {
      view = getInflater().inflate(getLayoutId(), parent, false);
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
    
    return view;
  }
}
