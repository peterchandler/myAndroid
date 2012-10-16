package jemstone.ui;

import java.util.Calendar;
import java.util.Date;

import jemstone.myandroid.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DateEditDialog extends BaseDialogFragment<ActivityParameters> {
  public static final String DATE = "DateEditDialog.Date";

  private Date date;

  private DateEditView dateEdit;

  public DateEditDialog() {
    super();
  }

  public DateEditDialog(ActivityParameters parameters) {
    super(parameters);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setDate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.date_edit_dialog, container);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    dateEdit = (DateEditView) getView().findViewById(R.id.date_edit);
    dateEdit.setDate(date);
    dateEdit.addChangedListener(new OnDateChangedListener());

    // Update the title
    setTitle(R.string.titleSelectDate, dateEdit.getCalendar());
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(DATE, date);
  }

  public Date getDate() {
    return dateEdit.getCalendar().getTime();
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setDate(Bundle bundle) {
    if (bundle != null) {
      Date date = (Date)bundle.getSerializable(DATE);
      if (date != null) {
        this.date = date;
      }
    }
  }

  private class OnDateChangedListener implements DateEditView.OnDateChangedListener {
    @Override
    public void onChanged(Calendar newDate) {
      setTitle(R.string.titleSelectDate, newDate);
    }
  }
}
