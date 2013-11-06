package jemstone.ui;

import jemstone.util.log.Logger;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

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
    addTextChangedListener(watcher);
  }

  public EditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    setOnFocusChangeListener(watcher);
    addTextChangedListener(watcher);
  }

  public EditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setOnFocusChangeListener(watcher);
    addTextChangedListener(watcher);
  }

  public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
    this.onValueChangedListener = onValueChangedListener;
  }

  public interface OnValueChangedListener {
    public void onValueChanged(View view, String text);
  }

  private class Watcher implements OnFocusChangeListener, TextWatcher {
    private String newValue = null;

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
      if (hasFocus) {
        newValue = null;
        log.trace("onFocusChange: Got focus, oldValue='%s'", getText());
      } else {
        log.trace("onFocusChange: Lost focus, newValue='%s'", newValue);
        notifyIfValueChanged();
      }
    }

    public void notifyIfValueChanged() {
      log.trace("notifyIfValueChanged: newValue='%s'", newValue);
      
      if (onValueChangedListener != null && newValue != null) {
        onValueChangedListener.onValueChanged(EditText.this, newValue);
        newValue = null;
      }
    }

    @Override
    public void afterTextChanged(Editable s) {
      newValue = getTextTrimmed(s);
      log.trace("afterTextChanged: newValue='%s'", newValue);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    private String getTextTrimmed(Editable s) {
      String text = s.toString();
      return (text != null) ? text.trim() : "";
    }
  }
}
