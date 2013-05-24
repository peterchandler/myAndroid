package jemstone.ui;

import jemstone.myandroid.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class ActivityManager {
  public enum Theme { Dark, Light, LightDarkActionBar };

  private final Context context;
  
  private Theme theme;

  public ActivityManager(Context context) {
    this.context = context;
  }

  public Theme getTheme() {
    if (theme == null) {
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
      String themeName = preferences.getString("theme", Theme.Dark.name());
      theme = Theme.valueOf(themeName);
    }
    return theme;
  }

  public void setTheme(Theme theme) {
    this.theme = theme;
    
    // Save the choice
    Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    editor.putString("theme", theme.name());              
    editor.commit();
  }
  
  public void setActivityTheme(Activity activity) {
    switch (getTheme()) {
      case Dark:
        activity.setTheme(R.style.MyTheme);
        break;
      case Light:
        activity.setTheme(R.style.MyTheme_Light);
        break;
      case LightDarkActionBar:
        activity.setTheme(R.style.MyTheme_Light_DarkActionBar);
        break;
    }
  }
}
