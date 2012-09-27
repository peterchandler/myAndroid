package jemstone.ui;

import jemstone.util.log.Logger;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

public class BaseDialogFragment extends DialogFragment {

  private final String TAG = getClass().getSimpleName();

  protected final Logger log = Logger.getLogger(this);

  protected ActivityParameters parameters;
  protected HeaderView header;
  protected String title;
  protected String subtitle;

  protected OnDismissListener onDismissListener;

  public BaseDialogFragment() {
    super();
  }

  public BaseDialogFragment(ActivityParameters parameters) {
    super();
    this.parameters = parameters;
  }

  public HeaderView getHeader() {
    return header;
  }

  public ActivityParameters getParameters() {
    return parameters;
  }

  public void setParameters(ActivityParameters parameters) {
    this.parameters = parameters;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setSubTitle(String subtitle) {
    this.subtitle = subtitle;
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