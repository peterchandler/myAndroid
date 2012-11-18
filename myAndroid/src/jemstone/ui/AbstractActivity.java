package jemstone.ui;

import jemstone.model.HasName;
import jemstone.util.log.Logger;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MenuInflater;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class AbstractActivity<AM extends ActivityManager, AP extends ActivityParameters> extends FragmentActivity {

  protected final Logger log = Logger.getLogger(this);
  
  private AM activityManager;
  private AP parameters;
  private boolean isHomeActivity = false;
  
  private MenuItemHandler<AM,AP> menuItemHandler;

  public AbstractActivity() {
    super();
  }

  public AM getActivityManager() {
    return activityManager;
  }

  public void setActivityManager(AM activityManager) {
    this.activityManager = activityManager;
  }

  public MenuItemHandler<AM,AP> getMenuItemHandler() {
    return menuItemHandler;
  }

  public void setMenuItemHandler(MenuItemHandler<AM,AP> handler) {
    this.menuItemHandler = handler;
  }

  public boolean isHomeActivity() {
    return isHomeActivity;
  }

  public void setHomeActivity(boolean isHomeActivity) {
    this.isHomeActivity = isHomeActivity;
  }

  public AP getParameters() {
    return parameters;
  }

  public void setParameters(AP parameters) {
    this.parameters = parameters;
  }

  private void setParameters(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      @SuppressWarnings("unchecked")
      AP parameters = (AP) savedInstanceState.getSerializable(ActivityParameters.NAME);
      if (parameters != null) {
        setParameters(parameters);
      }
    }
  }

  @Override
  public FragmentManager getSupportFragmentManager() {
    return super.getSupportFragmentManager();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // Extract parameters
    if (savedInstanceState != null) {
      setParameters(savedInstanceState);
    } else {
      setParameters(getIntent().getExtras());
    }
    
    //
    getActionBar().setDisplayHomeAsUpEnabled(!isHomeActivity);
  
    // Don't want keyboard on startup
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
  
    log.debug("onCreate: %s", getParameters());
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    try {
      super.onSaveInstanceState(outState);
  
      if (outState != null) {
        outState.putSerializable(ActivityParameters.NAME, getParameters());
      }
      log.debug("onSaveInstanceState: %s", getParameters());
    } catch (Exception e) {
      log.error(e, "onSaveInstanceState: %s", getParameters());
    }
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
  
    setParameters(savedInstanceState);
  
    log.debug("onRestoreInstanceState: %s", getParameters());
  }

  @Override
  public void onResume() {
    super.onResume();
    log.debug("onResume: %s", getParameters());
  }

  @Override
  public MenuInflater getMenuInflater() {
    return super.getMenuInflater();
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    log.debug("onPostCreate: %s", getParameters());
  }

  @Override
  protected void onTitleChanged(CharSequence title, int color) {
    super.onTitleChanged(title, color);
    log.debug("onTitleChanged: [title=%s]", title, getParameters());
  }
  

  public void setTitle(int stringId, HasName entity) {
    setTitle(getString(stringId, entity.getName()));
  }

  public void setTitle(int stringId, Object ... args) {
    setTitle(getString(stringId, args));
  }

  public void setSubTitle(CharSequence subtitle) {
    log.debug("setSubTitle: [subtitle=%s]", subtitle);
    getActionBar().setSubtitle(subtitle);
  }

  public void setSubTitle(String format, Object ... args) {
    setSubTitle(String.format(format, args));
  }

  public int getWindowRotation() {
    return getWindowManager().getDefaultDisplay().getRotation();
  }

  @SuppressWarnings("deprecation")
  public int getWindowWidth() {
    return getWindowManager().getDefaultDisplay().getWidth();
  }

  public boolean isLandscape() {
    final int rotation = getWindowRotation();
    return (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270);
  }

  public void hideKeyboard() {
    View currentFocus = getCurrentFocus();
    if (currentFocus != null) {
      InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
  }

  public void showKeyboard() {
    View currentFocus = getCurrentFocus();
    if (currentFocus != null) {
      InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      inputManager.showSoftInput(currentFocus, InputMethodManager.SHOW_FORCED);
    }
  }

  public void navigateToGooglePlay(String packageName) {
    try {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
    } catch (android.content.ActivityNotFoundException anfe) {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
    }
  }
}