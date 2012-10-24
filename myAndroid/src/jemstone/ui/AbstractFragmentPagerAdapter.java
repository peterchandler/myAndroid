package jemstone.ui;

import java.util.ArrayList;
import java.util.List;

import jemstone.model.HasName;
import jemstone.myandroid.R;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public abstract class AbstractFragmentPagerAdapter<T extends HasName, F extends AbstractFragment<?,?,?>> 
              extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
  /** The activity that owns this adapter */
  private final AbstractActivity<?,?> activity;
  
  /** A list of entities to be displayed on each fragment (or page of the view pager) */
  private final List<T> entities;
  
  /** The list of fragments managed by the view pager */
  private final List<F> fragments;

  public AbstractFragmentPagerAdapter(AbstractActivity<?,?> activity, List<T> entities) {
    super(activity.getSupportFragmentManager());
    this.activity = activity;
    
    this.entities = entities;
    
    this.fragments = new ArrayList<F>(entities.size());
    for (int i=0;  i < entities.size();  i++) {
      this.fragments.add(null);
    }
  }
  
  public T get(int index) {
    return entities.get(index);
  }

  public int indexOf(T entity) {
    return entities.indexOf(entity);
  }

  @Override
  public int getCount() {
    return (entities != null) ? entities.size() : 0;
  }
  
  @Override
  public F getItem(int position) {
    F fragment = createFragment(position);
    fragments.set(position, fragment);
    return fragment;
  }

  public F getFragment(int position) {
    return fragments.get(position);
  }
  
  protected abstract F createFragment(int position);

  @Override
  public CharSequence getPageTitle(int position) {
    final T entity = get(position);
    return (entity != null) ? entity.getName() : null;
  }

  protected CharSequence formatTitle(int position, int typeResId) {
    T entity = get(position);
    if (entity.getName() != null) {
      return activity.getString(R.string.titleEdit, entity.getName());
    } 
    
    String name = activity.getString(typeResId);
    return activity.getString(R.string.titleCreate, name);
  }

  @Override
  public void onPageSelected(int position) {
    final HeaderView headerView = activity.getHeaderView();
    if (headerView != null) {
      // Set command handler
      final F fragment = getFragment(position);
      if (fragment != null) {
        headerView.setCommandButtonHandler(fragment.getCommandButtonHandler());
      }
      
      // Set title and update buttons
      headerView.setTitle(getPageTitle(position));
      headerView.setButtonState();
    }
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onPageScrollStateChanged(int state) {
    // TODO Auto-generated method stub
  }
}