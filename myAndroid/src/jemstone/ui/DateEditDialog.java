package jemstone.ui;

import java.util.Calendar;
import java.util.Date;

import jemstone.myandroid.R;
import jemstone.util.DateUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

public class DateEditDialog extends AbstractDialogFragment<ActivityParameters>
                         implements DatePicker.OnDateChangedListener 
{
  public static final String DATE = "DateEditDialog.Date";

  private Calendar calendar;

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

    // Use the current date as the default date in the picker
    if (calendar == null) {
      calendar = DateUtil.getCurrentDate();
    }
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    // Set the date in the date picker
    DatePicker picker = (DatePicker) getView().findViewById(R.id.date_picker);
    picker.init(year, month, day, this);

    // Update the title
    setTitle(R.string.titleSelectDate, calendar);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(DATE, getDate());
  }

  public Date getDate() {
    return calendar.getTime();
  }

  public void setDate(Date date) {
    this.calendar = DateUtil.calendar(date);
  }

  public void setDate(Bundle bundle) {
    if (bundle != null) {
      Date date = (Date)bundle.getSerializable(DATE);
      if (date != null) {
        setDate(date);
      }
    }
  }

  @Override
  public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    calendar.set(year, monthOfYear, dayOfMonth);
    setTitle(R.string.titleSelectDate, calendar);
  }
}
