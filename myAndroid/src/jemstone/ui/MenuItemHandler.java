package jemstone.ui;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public interface MenuItemHandler<AM extends ActivityManager, AP extends ActivityParameters> {
  public boolean canUndo();
  public boolean canRedo();
  public boolean canAdd();
  public boolean canDelete();
  public boolean canAccept();
  public boolean canCancel();
  public boolean canConfig();
  
  public void onUndo();
  public void onRedo();
  public void onAdd();
  public void onDelete();
  public void onAccept();
  public void onCancel();
  public void onReload();

  public void onRefresh();
  public void onCreateMenu(Menu menu, MenuInflater inflater, AP parameters);
  public boolean onMenuItemSelected(MenuItem item, AP parameters, AM activityManager);
}