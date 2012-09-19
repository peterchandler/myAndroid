package jemstone.ui.shared;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jemstone.mylib.R;
import jemstone.util.DateUtil;
import jemstone.util.Formatter;
import jemstone.util.RepeatFormat;
import jemstone.util.log.Logger;
import kankan.wheel.widget.WheelView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class DateEditView extends LinearLayout {
  private static final Logger log = Logger.getLogger(DateEditView.class);

  private Calendar calendar = Calendar.getInstance();

  private DayAdapter dayAdapter;
  private MonthAdapter monthAdapter;
  private YearAdapter yearAdapter;

  private List<OnDateChangedListener> changedListeners = new ArrayList<OnDateChangedListener>();

  public interface OnDateChangedListener {
    public void onChanged(Calendar newDate);
  }

  public DateEditView(Context context) {
    super(context);
  }

  public DateEditView(Context context, AttributeSet attrs) {
    super(context, attrs);

    log.debug("Inflating date_edit_layout");
    View view = LayoutInflater.from(context).inflate(R.layout.date_edit_layout, this);

    // Create adapters
    dayAdapter = new DayAdapter(view);
    monthAdapter = new MonthAdapter(view);
    yearAdapter = new YearAdapter(view);

    updateValues();
  }

  public Calendar getCalendar() {
    return calendar;
  }

  public void setCalendar(Calendar calendar) {
    this.calendar = calendar;
    updateValues();
  }

  public void setDate(Date date) {
    if (date != null) {
      setCalendar(DateUtil.calendar(date));
    }
  }

  public void addChangedListener(OnDateChangedListener listener) {
    changedListeners.add(listener);
  }

  private void notifyChangedListeners() {
    for (OnDateChangedListener listener : changedListeners) {
      listener.onChanged(calendar);
    }
  }

  private void updateValues() {
    if (dayAdapter != null) {
      dayAdapter.setValues();
      monthAdapter.setValues();
      yearAdapter.setValues();
    }
  }

  public abstract class CalendarAdapter extends BaseWheelAdapter {
    public CalendarAdapter(View view, int wheelId, int min, int max) {
      super(view.getContext(), min, max, null);
      setWheel(view, wheelId);
    }

    public CalendarAdapter(View view, int wheelId, int itemsArrayId) {
      super(view.getContext(), itemsArrayId);
      setWheel(view, wheelId);
    }
  }

  private class DayAdapter extends CalendarAdapter {
    public DayAdapter(View view) {
      super(view, R.id.day, 1, 31);
    }

    @Override
    public void getValues() {
      int index = wheel.getCurrentItem();
      calendar.set(Calendar.DAY_OF_MONTH, index+1);
    }

    @Override
    public void setValues() {
      int day = calendar.get(Calendar.DAY_OF_MONTH);
      wheel.setCurrentItem(day-1);
    }

    public void setMaxDays() {
      int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
      setMaxValue(maxDays);
      notifyDataSetChanged();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
      super.onChanged(wheel, oldValue, newValue);
      notifyChangedListeners();
    }
  }

  private class MonthAdapter extends CalendarAdapter {
    private final RepeatFormat repeatFormat = Formatter.getRepeatFormat();

    public MonthAdapter(View view) {
      super(view, R.id.month, Calendar.JANUARY, Calendar.DECEMBER);
    }

    @Override
    public CharSequence getItemText(int index) {
      return repeatFormat.getMonth(index);
    }

    @Override
    public void getValues() {
      int month = wheel.getCurrentItem();

      // Be careful to protect the day-of-month
      int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      calendar.set(Calendar.MONTH, month);

      int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
      if (dayOfMonth > daysInMonth) {
        dayOfMonth = daysInMonth;
      }
      calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    @Override
    public void setValues() {
      int month = calendar.get(Calendar.MONTH);
      wheel.setCurrentItem(month);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
      super.onChanged(wheel, oldValue, newValue);
      dayAdapter.setMaxDays();
      notifyChangedListeners();
    }
  }

  private class YearAdapter extends CalendarAdapter {
    public YearAdapter(View view) {
      super(view, R.id.year, 2000, 2020);
    }

    @Override
    public void getValues() {
      int index = wheel.getCurrentItem();

      // Be careful to protect the day-of-month
      int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      calendar.set(Calendar.YEAR, index + getMinValue());

      int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
      if (dayOfMonth > daysInMonth) {
        dayOfMonth = daysInMonth;
      }
      calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    @Override
    public void setValues() {
      int year = calendar.get(Calendar.YEAR);
      wheel.setCurrentItem(year - getMinValue());
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
      super.onChanged(wheel, oldValue, newValue);
      dayAdapter.setMaxDays();
      notifyChangedListeners();
    }
  }
}
