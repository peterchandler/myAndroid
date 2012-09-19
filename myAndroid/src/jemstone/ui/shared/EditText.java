package jemstone.ui.shared;

import jemstone.util.log.Logger;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Extended {@link android.widget.EditText} control that can notify a specified
 * {@link OnValueChangedListener} when the control loses focus if the value has
 * changed since the control received focus.
 */
public class EditText extends android.widget.EditText {
  public static final Logger log = Logger.getLogger(EditText.class);

  private final Watcher watcher = new Watcher();
  private OnValueChangedListener onValueChangedListener;

  public EditText(Context context) {
    super(context);
    setOnFocusChangeListener(watcher);
  }

  public EditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    setOnFocusChangeListener(watcher);
  }

  public EditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setOnFocusChangeListener(watcher);
  }

  public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
    this.onValueChangedListener = onValueChangedListener;
  }

  /**
   * If the contents of the edit control have changed, then notify the
   * {@link OnValueChangedListener} if present.
   */
  public void getValue() {
    String newValue = getTextTrimmed();
    log.trace("getValue: text='%s'", newValue);

    watcher.notifyIfValueChanged(newValue);
  }

  private String getTextTrimmed() {
    String text = getText().toString();
    return (text != null) ? text.trim() : "";
  }

  @SuppressWarnings("unused")
  private void showKeyboard() {
    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    inputManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
  }

  public interface OnValueChangedListener {
    public void onValueChanged(View view, String text);
  }

  private class Watcher implements OnFocusChangeListener {
    private String oldValue = null;

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
      if (hasFocus) {
        // Seems that in some circumstances we can get focus more than once without
        // having lost it!!
        if (oldValue == null) {
          oldValue = getTextTrimmed();
        }

        // Display soft keyboard
        //        showKeyboard();

        // Log it
        log.trace("onFocusChange: Got focus, text='%s'", oldValue);
      } else {
        String newValue = getTextTrimmed();
        log.trace("onFocusChange: Lost focus, text='%s'", newValue);

        // Notify if required and reset oldValue
        notifyIfValueChanged(newValue);
      }
    }

    public void notifyIfValueChanged(String newValue) {
      if (onValueChangedListener != null && oldValue != null && !newValue.equals(oldValue)) {
        onValueChangedListener.onValueChanged(EditText.this, newValue);
      }
      oldValue = null;
    }
  }
}
