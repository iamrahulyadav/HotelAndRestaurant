package com.shoaibnwar.hotelandrestaurant.Calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.shoaibnwar.hotelandrestaurant.Activities.CustomCalendar;
import com.shoaibnwar.hotelandrestaurant.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gold on 7/7/2018.
 */

public class SimpleMonthAdapter extends RecyclerView.Adapter<SimpleMonthAdapter.ViewHolder> implements SimpleMonthView.OnDayClickListener {
    protected static final int MONTHS_IN_YEAR = 12;
    private final TypedArray typedArray;
    private final Context mContext;
    private final DatePickerController mController;
    private final Calendar calendar;
    private final SelectedDays<CalendarDay> selectedDays;
    private final Integer firstMonth;
    private final Integer lastMonth;

    //ArrayList<HashMap<String, Integer>> datess;


    public SimpleMonthAdapter(Context context, DatePickerController datePickerController, TypedArray typedArray) {
        this.typedArray = typedArray;
        calendar = Calendar.getInstance();
        firstMonth = typedArray.getInt(R.styleable.DayPickerView_firstMonth, calendar.get(Calendar.MONTH));
        lastMonth = typedArray.getInt(R.styleable.DayPickerView_lastMonth, (calendar.get(Calendar.MONTH) - 1) % MONTHS_IN_YEAR);
        selectedDays = new SelectedDays<>();
        mContext = context;
        mController = datePickerController;
        init();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final SimpleMonthView simpleMonthView = new SimpleMonthView(mContext, typedArray);
        return new ViewHolder(simpleMonthView, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        final SimpleMonthView v = viewHolder.simpleMonthView;
        final HashMap<String, Integer> drawingParams = new HashMap<String, Integer>();
        int month;
        int year;

        month = (firstMonth + (position % MONTHS_IN_YEAR)) % MONTHS_IN_YEAR;
        year = position / MONTHS_IN_YEAR + calendar.get(Calendar.YEAR) + ((firstMonth + (position % MONTHS_IN_YEAR)) / MONTHS_IN_YEAR);

        int selectedFirstDay = -1;
        int selectedLastDay = -1;
        int selectedFirstMonth = -1;
        int selectedLastMonth = -1;
        int selectedFirstYear = -1;
        int selectedLastYear = -1;

        if (selectedDays.getFirst() != null)
        {
            selectedFirstDay = selectedDays.getFirst().day;
            selectedFirstMonth = selectedDays.getFirst().month;
            selectedFirstYear = selectedDays.getFirst().year;
        }

        if (selectedDays.getLast() != null)
        {
            selectedLastDay = selectedDays.getLast().day;
            selectedLastMonth = selectedDays.getLast().month;
            selectedLastYear = selectedDays.getLast().year;
        }

        v.reuse();

        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_YEAR, selectedFirstYear);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_YEAR, selectedLastYear);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_MONTH, selectedFirstMonth);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_MONTH, selectedLastMonth);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_BEGIN_DAY, selectedFirstDay);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_SELECTED_LAST_DAY, selectedLastDay);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_YEAR, year);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_MONTH, month);
        drawingParams.put(SimpleMonthView.VIEW_PARAMS_WEEK_START, calendar.getFirstDayOfWeek());
        v.setMonthParams(drawingParams);
        v.invalidate();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount()
    {
        int itemCount = (((mController.getMaxYear() - calendar.get(Calendar.YEAR)) + 1) * MONTHS_IN_YEAR);

        if (firstMonth != -1)
            itemCount -= firstMonth;

        if (lastMonth != -1)
            itemCount -= (MONTHS_IN_YEAR - lastMonth) - 1;

        return itemCount;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        final SimpleMonthView simpleMonthView;

        public ViewHolder(View itemView, SimpleMonthView.OnDayClickListener onDayClickListener)
        {
            super(itemView);
            simpleMonthView = (SimpleMonthView) itemView;
            simpleMonthView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            simpleMonthView.setClickable(true);
            simpleMonthView.setOnDayClickListener(onDayClickListener);
        }
    }

    protected void init() {
        if (typedArray.getBoolean(R.styleable.DayPickerView_currentDaySelected, false)) {
            onDayTapped(new CalendarDay(System.currentTimeMillis()));
        }

        /*datess = new ArrayList<>();

        for(int i = 10; i<19; i++){
            HashMap<String, Integer> da = new HashMap<>();
            da.put("day", i);
            da.put("month", 6);
            da.put("year", 2018);
            datess.add(da);

        }*/
    }

    public void onDayClick(SimpleMonthView simpleMonthView, CalendarDay calendarDay) {
        if (calendarDay != null) {
            onDayTapped(calendarDay);

        }
    }

    protected void onDayTapped(CalendarDay calendarDay) {
        mController.onDayOfMonthSelected(calendarDay.year, calendarDay.month, calendarDay.day);
        setSelectedDay(calendarDay);
    }

    public void setSelectedDay(CalendarDay calendarDay) {
        if (selectedDays.getFirst() != null && selectedDays.getLast() == null)
        {
            selectedDays.setLast(calendarDay);


            Log.e("TAg", "the last values are: " + selectedDays.getFirst());
            Log.e("TAg", "the last values are: " + selectedDays.getLast());

            Date dateFirst = selectedDays.getFirst().getDate();
            Date dateLast = selectedDays.getLast().getDate();
            int compare = dateFirst.compareTo(dateLast);
            Log.e("TAg", "the resturn result is: " + compare);
            List<Date> between;
            if (compare == 1){
                between = getDatesBetweenUsingJava7(selectedDays.getLast().getDate(), selectedDays.getFirst().getDate());
            }else {
                between = getDatesBetweenUsingJava7(selectedDays.getFirst().getDate(), selectedDays.getLast().getDate());
            }

            Log.e("TGAg", "the dates between range " + between);
            List<Calendar> cList = new ArrayList<>();
            for (int i = 0; i<between.size(); i++){

                Calendar calendar = toCalendar(between.get(i));
                cList.add(calendar);
            }
            Log.e("TGAg", "the dates between range " + cList);
            boolean isMatchFound = false;


            for (int i=0; i<cList.size(); i++){

                final int curentDay = cList.get(i).get(Calendar.DAY_OF_MONTH);
                final int curentMonth = cList.get(i).get(Calendar.MONTH);
                final int curentYear = cList.get(i).get(Calendar.YEAR);
                Log.e("TGAg", "Dates dates dates " + cList.get(i).get(Calendar.DAY_OF_MONTH));
                Log.e("TGAg", "Dates dates dates " + cList.get(i).get(Calendar.MONTH));
                Log.e("TGAg", "Dates dates dates " + cList.get(i).get(Calendar.YEAR));

                for (int j = 0; j< CustomCalendar.bookedDates.size(); j++){
                    int bookedDay = CustomCalendar.bookedDates.get(j).get("day");
                    int bookedMonth = CustomCalendar.bookedDates.get(j).get("month");
                    int bookedYear = CustomCalendar.bookedDates.get(j).get("year");
                    if ((curentDay == bookedDay) && ((curentMonth+1) == bookedMonth) && curentYear == bookedYear){

                        Log.e("TAG", "Match Found here " );
                        isMatchFound = true;
                        break;
                    }
                }
            }

            if (!isMatchFound) {

                if (selectedDays.getFirst().month < calendarDay.month) {
                    for (int i = 0; i < selectedDays.getFirst().month - calendarDay.month - 1; ++i) {
                        mController.onDayOfMonthSelected(selectedDays.getFirst().year, selectedDays.getFirst().month + i, selectedDays.getFirst().day);


                    }
                }

                mController.onDateRangeSelected(selectedDays);
            }
            else {

                selectedDays.setFirst(calendarDay);
                selectedDays.setLast(null);
            }
        }
        else if (selectedDays.getLast() != null)
        {

            selectedDays.setFirst(calendarDay);
            selectedDays.setLast(null);

        }
        else
            selectedDays.setFirst(calendarDay);

        notifyDataSetChanged();
    }

    public static class CalendarDay implements Serializable
    {
        private static final long serialVersionUID = -5456695978688356202L;
        private Calendar calendar;

        int day;
        int month;
        int year;

        public CalendarDay() {
            setTime(System.currentTimeMillis());
        }

        public CalendarDay(int year, int month, int day) {
            setDay(year, month, day);
        }

        public CalendarDay(long timeInMillis) {
            setTime(timeInMillis);
        }

        public CalendarDay(Calendar calendar) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        private void setTime(long timeInMillis) {
            if (calendar == null) {
                calendar = Calendar.getInstance();
            }
            calendar.setTimeInMillis(timeInMillis);
            month = this.calendar.get(Calendar.MONTH);
            year = this.calendar.get(Calendar.YEAR);
            day = this.calendar.get(Calendar.DAY_OF_MONTH);
        }

        public void set(CalendarDay calendarDay) {
            year = calendarDay.year;
            month = calendarDay.month;
            day = calendarDay.day;
        }

        public void setDay(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public Date getDate()
        {
            if (calendar == null) {
                calendar = Calendar.getInstance();
            }
            calendar.set(year, month, day);
            return calendar.getTime();
        }

        @Override
        public String toString()
        {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{ year: ");
            stringBuilder.append(year);
            stringBuilder.append(", month: ");
            stringBuilder.append(month);
            stringBuilder.append(", day: ");
            stringBuilder.append(day);
            stringBuilder.append(" }");

            return stringBuilder.toString();
        }
    }

    public SelectedDays<CalendarDay> getSelectedDays()
    {
        return selectedDays;
    }

    public static class SelectedDays<K> implements Serializable
    {
        private static final long serialVersionUID = 3942549765282708376L;
        private K first;
        private K last;

        public K getFirst()
        {
            return first;
        }

        public void setFirst(K first)
        {
            this.first = first;
        }

        public K getLast()
        {
            return last;
        }

        public void setLast(K last)
        {
            this.last = last;
        }
    }

    public static List<Date> getDatesBetweenUsingJava7(
            Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return datesInRange;
    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}

