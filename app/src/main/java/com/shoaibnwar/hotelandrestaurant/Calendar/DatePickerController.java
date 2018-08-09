package com.shoaibnwar.hotelandrestaurant.Calendar;

/**
 * Created by gold on 7/7/2018.
 */

public interface DatePickerController {
    public abstract int getMaxYear();

    public abstract void onDayOfMonthSelected(int year, int month, int day);

    public abstract void onDateRangeSelected(final SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays);

}