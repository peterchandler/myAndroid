package jemstone.ui;

import jemstone.model.HasName;
import jemstone.myandroid.R;
import jemstone.ui.HeaderView.CommandButtonHandler;
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
  
  private HeaderView headerView;

  public AbstractActivity() {
    super();
  }

  public AM getActivityManager() {
    return activityManager;
  }

  public void setActivityManager(AM activityManager) {
    this.activityManager = activityManager;
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

  public HeaderView getHeaderView() {
    if (headerView == null) {
      headerView = (HeaderView) findViewById(R.id.header_view);
    }
    return headerView;
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
  
  /**
   * Set the title for the activity 
   * @param title
   */
  @Override
  public void setTitle(CharSequence title) {
    log.debug("setTitle: [title=%s]", title);

    HeaderView headerView = getHeaderView();
    if (headerView != null) {
      headerView.setTitle(title);
    }
  }

  @Override
  public void setTitle(int stringId) {
    setTitle(getString(stringId));
  }

  public void setTitle(int stringId, HasName entity) {
    setTitle(getString(stringId, entity.getName()));
  }

  public void setTitle(int stringId, Object ... args) {
    setTitle(getString(stringId, args));
  }

  public void setSubTitle(CharSequence subtitle) {
    log.debug("setSubTitle: [subtitle=%s]", subtitle);

    HeaderView headerView = getHeaderView();
    if (headerView != null) {
      headerView.setSubTitle(subtitle);
    }
  }

  public void setCommandButtonHandler(CommandButtonHandler handler) {
    HeaderView headerView = getHeaderView();
    if (headerView != null) {
      headerView.setCommandButtonHandler(handler);
      headerView.setButtonState();
    }
  }

  public int getWindowRotation() {
    WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    return windowManager.getDefaultDisplay().getRotation();
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