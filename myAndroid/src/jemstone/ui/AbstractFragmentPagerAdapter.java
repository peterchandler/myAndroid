package jemstone.ui;

import java.util.ArrayList;
import java.util.List;

import jemstone.model.HasName;
import jemstone.util.log.Logger;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public abstract class AbstractFragmentPagerAdapter<T extends HasName, F extends AbstractFragment<?,?,?>> 
              extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
  
  protected final Logger log = Logger.getLogger(getClass());
  
  /** The activity that owns this adapter */
  private final AbstractActivity<?,?> activity;
  
  /** A list of entities to be displayed on each fragment (or page of the view pager) */
  private final List<T> entities;
  
  /** The list of fragments managed by the view pager */
  private final List<F> fragments;
  
  /** The view pager that this adapter is managing */
  private final BaseViewPager viewPager;
  
  /** The position of the currently selected fragment */
  private int currentPosition = -1;

  public AbstractFragmentPagerAdapter(AbstractActivity<?,?> activity, List<T> entities, BaseViewPager viewPager) {
    super(activity.getSupportFragmentManager());
    this.activity = activity;
    this.entities = entities;
    this.viewPager = viewPager;
    
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
    
    // Force title update
    if (position == currentPosition) {
      setActivityTitle(position);
    }
    
    return fragment;
  }

  public int getCurrentPosition() {
    return currentPosition;
  }

  public void setCurrentPosition(int position) {
    log.debug("setCurrentPosition: [position=%s]", position);

    this.currentPosition = position;
    if (position != -1) {
      viewPager.setCurrentItem(position);
    }
  }

  public void setCurrentItem(T entity) {
    int position = 0;
    if (entity != null) {
      position = indexOf(entity);
    }
    setCurrentPosition(position);
  }

  public F getFragment(int position) {
    return fragments.get(position);
  }
  
  protected abstract F createFragment(int position);
  
  @Override
  public CharSequence getPageTitle(int position) {
    return null;
  }

  @Override
  public void onPageSelected(int position) {
    final F fragment = getFragment(position);
    log.debug("onPageSelected: [page=%s, fragment=%s]", position, 
              (fragment == null) ? null : fragment.getClass().getName());

    setActivityTitle(position);
  }

  private void setActivityTitle(int position) {
    final CharSequence title = getPageTitle(position);
    if (title != null) {
      activity.setTitle(title);
    }
    activity.setSubTitle(get(position).getName());
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
  }

  @Override
  public void onPageScrollStateChanged(int state) {
  }
}