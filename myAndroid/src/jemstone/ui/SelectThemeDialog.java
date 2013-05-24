package jemstone.ui;

import jemstone.myandroid.R;
import jemstone.ui.ActivityManager.Theme;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class SelectThemeDialog extends DialogFragment {
  public static String TAG = "SelectThemeDialog";
  
  private AbstractActivity<?,?> activity;
  
  public SelectThemeDialog(AbstractActivity<?,?> activity) {
    this.activity = activity;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setTitle(R.string.titleSelectTheme);
    builder.setSingleChoiceItems(R.array.themes, activity.getActivityManager().getTheme().ordinal(), new OnClickListener());
    
    return builder.create();
  }
  
  private class OnClickListener implements DialogInterface.OnClickListener {
    @Override
    public void onClick(DialogInterface dialog, int which) {
      ActivityManager.Theme selectedTheme = null;
      switch (which) {
        case 0: selectedTheme = Theme.Dark;  break;
        case 1: selectedTheme = Theme.Light; break;
        case 2: selectedTheme = Theme.LightDarkActionBar;  break;
      }
      
      // Save the choice
      activity.getActivityManager().setTheme(selectedTheme);
      
      // Close the dialog
      SelectThemeDialog.this.dismiss();
    }
  }
}
