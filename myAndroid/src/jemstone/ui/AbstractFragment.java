package jemstone.ui;

import jemstone.ui.HeaderView.CommandButtonHandler;
import jemstone.util.command.CommandManager;
import jemstone.util.log.Logger;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractFragment<A extends AbstractActivity<AM,AP>,
                                      AM extends ActivityManager, 
                                      AP extends ActivityParameters> extends Fragment {
  protected final Logger log = Logger.getLogger(this);
  
  private A activity;
  private AP parameters;
  
  private CommandManager commandManager;
  private CommandButtonHandler commandButtonHandler;

  public AbstractFragment() {
    super();
    this.commandManager = new CommandManager(getClass().getSimpleName());
  }

  public A getBaseActivity() {
    return activity;
  }

  public AM getActivityManager() {
    return activity.getActivityManager();
  }

  public CommandButtonHandler getCommandButtonHandler() {
    return commandButtonHandler;
  }

  public void setCommandButtonHandler(CommandButtonHandler commandButtonHandler) {
    this.commandButtonHandler = commandButtonHandler;
  }

  public CommandManager getCommandManager() {
    return commandManager;
  }

  public HeaderView getHeaderView() {
    return activity.getHeaderView();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
  
    // Set activity and a local copy of the parameters
    this.activity = (A) activity;
    setParametersClone(this.activity.getParameters());
  
    log.debug("onAttach: %s", getParameters());
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  
    setParameters(savedInstanceState);
  
    log.debug("onCreate: %s", getParameters());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    log.debug("onCreateView: %s", getParameters());
  
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  
    log.debug("onActivityCreated: %s", getParameters());
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  
    outState.putSerializable(ActivityParameters.NAME, getParameters());
  
    log.debug("onSaveInstanceState: %s", getParameters());
  }

  @Override
  public void onPause() {
    super.onPause();
    log.debug("onPause: %s", getParameters());
  }

  @Override
  public void onResume() {
    super.onResume();
    log.debug("onResume: %s", getParameters());
  }

  public void finishActivity() {
    // Clear focus
    final View currentFocus = activity.getCurrentFocus();
    if (currentFocus != null) {
      currentFocus.clearFocus();
    }
    
    // Hide keyboard, and finish the activity
    activity.hideKeyboard();
    activity.finish();
  }

  public AP getParameters() {
    return parameters;
  }

  public void setParameters(AP parameters) {
    this.parameters = parameters;
  }

  public void setParameters(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      @SuppressWarnings("unchecked")
      AP parameters = (AP) savedInstanceState.getSerializable(ActivityParameters.NAME);
      if (parameters != null) {
        setParameters(parameters);
      }
    }
  }

  /**
   * Implement this method to overcome a problem when cloning a parameterised type (in this
   * case <AP extends ActivityParameters>).
   * @param parameters
   */
  protected abstract void setParametersClone(AP parameters);
}