/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jemstone.ui.shared;

import jemstone.util.log.Logger;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.WheelViewAdapter;
import android.content.Context;
import android.view.View;

/**
 * Numeric Wheel adapter.
 */
public abstract class BaseWheelAdapter extends AbstractWheelTextAdapter
implements HasValues, OnWheelChangedListener {
  protected final Logger log = Logger.getLogger(getClass());

  /** The default min value */
  public static final int DEFAULT_MAX_VALUE = 9;

  /** The default max value */
  private static final int DEFAULT_MIN_VALUE = 0;

  /** The default text size */
  public static final int TEXT_SIZE = 18;

  /** The default number of visible items */
  public static final int VISIBLE_ITEMS = 5;

  // Values
  private int minValue;
  private int maxValue;

  // format
  private String format;

  private String[] items;

  // The wheel that is bound to this adapter
  protected WheelView wheel;

  /**
   * Constructor
   * @param context the current context
   */
  public BaseWheelAdapter(Context context) {
    this(context, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
  }

  /**
   * Constructor
   * @param context the current context
   * @param minValue the wheel min value
   * @param maxValue the wheel max value
   */
  public BaseWheelAdapter(Context context, int minValue, int maxValue) {
    this(context, minValue, maxValue, null);
  }

  /**
   * Constructor
   * @param context the current context
   * @param minValue the wheel min value
   * @param maxValue the wheel max value
   * @param format the format string
   */
  public BaseWheelAdapter(Context context, int minValue, int maxValue, String format) {
    super(context);

    this.minValue = minValue;
    this.maxValue = maxValue;
    this.format = format;

    setTextSize(TEXT_SIZE);
  }

  public BaseWheelAdapter(Context context, int itemsArrayId) {
    this(context, 0, 0, null);
    this.items = context.getResources().getStringArray(itemsArrayId);
    this.maxValue = items.length - 1;
  }

  public WheelView getWheel() {
    return wheel;
  }

  public void setWheel(WheelView wheel) {
    this.wheel = wheel;

    WheelViewAdapter oldAdapter = wheel.getViewAdapter();
    if (oldAdapter instanceof OnWheelChangedListener) {
      wheel.removeChangingListener((OnWheelChangedListener) oldAdapter);
    }

    wheel.setVisibleItems(VISIBLE_ITEMS);
    wheel.setCyclic(this.getItemsCount() >= VISIBLE_ITEMS);

    wheel.setViewAdapter(this);
    wheel.addChangingListener(this);
  }

  public void setWheel(View view, int wheelId) {
    setWheel((WheelView) view.findViewById(wheelId));
  }

  public int getMinValue() {
    return minValue;
  }

  public void setMinValue(int minValue) {
    this.minValue = minValue;
  }

  public int getMaxValue() {
    return maxValue;
  }

  public void setMaxValue(int maxValue) {
    log.debug("setMaxValue: oldValue=%d, newValue=%d", this.maxValue, maxValue);
    this.maxValue = maxValue;
  }

  @Override
  public CharSequence getItemText(int index) {
    if (index >= 0 && index < getItemsCount()) {
      if (items != null) {
        return items[index];
      }
      int value = minValue + index;
      return format != null ? String.format(format, value) : Integer.toString(value);
    }
    return null;
  }

  @Override
  public int getItemsCount() {
    return maxValue - minValue + 1;
  }

  public void notifyDataSetChanged() {
    final int itemsCount = getItemsCount();
    final int currentItem = wheel.getCurrentItem();

    log.debug("notifyDataSetChanged: currentItem=%d, itemsCount=%d", currentItem, itemsCount);

    if (currentItem >= itemsCount) {
      wheel.setCurrentItem(itemsCount-1);
    }
    wheel.invalidateWheel(true);
    wheel.setCyclic(itemsCount >= VISIBLE_ITEMS);
  }

  @Override
  public void onChanged(WheelView wheel, int oldValue, int newValue) {
    log.debug("onChanged: oldValue=%d, newValue=%d", oldValue, newValue);
    getValues();
  }
}
