package jemstone.ui;

import jemstone.myandroid.R;
import jemstone.util.log.Logger;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class AbstractDialogFragment<T extends ActivityParameters> extends DialogFragment implements OnClickListener {

  private final String TAG = getClass().getSimpleName();

  protected final Logger log = Logger.getLogger(this);

  private T parameters;
  private HeaderView header;
  private String title;
  private String subtitle;

  protected OnDismissListener onDismissListener;

  public AbstractDialogFragment() {
    super();
  }

  public AbstractDialogFragment(T parameters) {
    super();
    this.parameters = parameters;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

//    // Keep track of the parent Activity
//    this.activity = (BaseActivity) activity;
//
//    // Set parameters from the parent Activity if not already set
//    if (parameters == null) {
//      setParameters(this.activity.getParameters().clone());
//    }

    log.debug("onAttach: %s", getParameters());
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setParameters(savedInstanceState);
    setHasOptionsMenu(true);
    setStyle(STYLE_NO_TITLE, 0);

    log.debug("onCreate: %s", getParameters());
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    log.debug("onActivityCreated: %s", getParameters());
    
    // Get the header, and set title and subtitle if requested
    header = (HeaderView) getView().findViewById(R.id.header_view);
    
    // Set title, and list properties
    if (title != null) {
      header.setTitle(title);
    }
    if (subtitle != null) {
      header.setSubTitle(subtitle);
    }
    
    // Initialize OK/Cancel buttons
    initButton(getView(), R.id.dialog_accept);
    initButton(getView(), R.id.dialog_cancel);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putSerializable(ActivityParameters.NAME, getParameters());

    log.debug("onSaveInstanceState: %s", parameters);
  }

  @Override
  public void onClick(View view) {
    if (onDismissListener != null) {
      boolean accept = (view.getId() == R.id.dialog_accept);
      log.debug("onClick: Firing onDismiss(%s)", accept);

      onDismissListener.onDimissed(accept);
    }
    dismiss();
  }

  private View initButton(View view, int id) {
    View button = view.findViewById(id);
    if (button != null) {
      button.setOnClickListener(this);
    }
    return button;
  }

  public T getParameters() {
    return parameters;
  }

  public void setParameters(T parameters) {
    this.parameters = parameters;
  }

  public void setParameters(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      @SuppressWarnings("unchecked")
      T parameters = (T) savedInstanceState.getSerializable(ActivityParameters.NAME);
      if (parameters != null) {
        setParameters(parameters);
      }
    }
  }

  public void setTitle(String title) {
    this.title = title;
    
    if (header != null) {
      header.setTitle(title);
    }
  }

  public void setTitle(int resId) {
    setTitle(getString(resId));
  }

  public void setTitle(int resId, Object ... args) {
    setTitle(getString(resId, args));
  }

  public void setSubTitle(String subtitle) {
    this.subtitle = subtitle;
    
    if (header != null) {
      header.setSubTitle(subtitle);
    }
  }

  public void setOnDismissListener(OnDismissListener onDismissListener) {
    this.onDismissListener = onDismissListener;
  }
  
  public void show(FragmentManager manager) {
    show(manager, TAG);
  }

  public interface OnDismissListener {
    public void onDimissed(boolean accept);
  }
}