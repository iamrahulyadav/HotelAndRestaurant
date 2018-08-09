package com.shoaibnwar.hotelandrestaurant.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shoaibnwar.hotelandrestaurant.Calendar.DatePickerController;
import com.shoaibnwar.hotelandrestaurant.Calendar.DayPickerView;
import com.shoaibnwar.hotelandrestaurant.Calendar.SimpleMonthAdapter;
import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.HttpRequester;
import com.shoaibnwar.hotelandrestaurant.Utils.Logger;
import com.shoaibnwar.hotelandrestaurant.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CustomCalendar extends AppCompatActivity implements DatePickerController, AsyncTaskCompleteListener {

    private DayPickerView dayPickerView;
    private TextView tv_checkin, tv_checkout;
    private ImageView iv_cross;
    private TextView tv_next;
    private TextView tv_clear_calendar;
    RelativeLayout rrRL;
    ImageView loading_img_rr;
    Animation rotation;
    JSONArray jsonArray;

    long diff = 0;
    String Aid;
    String VType;
    String roomID;
    private SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays;

    public static ArrayList<HashMap<String, Integer>> bookedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.custome_loading_dialog);

        setContentView(R.layout.activity_custom_calendar);


        Intent intentData = getIntent();
        Aid = intentData.getStringExtra("vid");
        VType = intentData.getStringExtra("VType");

        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        month = month+1;
        String cMonth = String.valueOf(month);

        bookedDates = new ArrayList<>();
        checkAvailability(Aid, cMonth);

        init();
        gettingIntenValues();
        clearCalendarCall();
        croseClickListner();
        tvNextClickHandler();
    }

    private void gettingIntenValues()
    {
        Intent intent = getIntent();
        roomID = intent.getStringExtra("room_id");
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void init(){

        dayPickerView = (DayPickerView) findViewById(R.id.pickerView);
        dayPickerView.setController(this);

        tv_checkin = (TextView) findViewById(R.id.tv_checkin);
        tv_checkout = (TextView) findViewById(R.id.tv_checkout);
        iv_cross = (ImageView) findViewById(R.id.iv_cross);
        tv_next = (TextView) findViewById(R.id.tv_next);

        tv_clear_calendar = (TextView) findViewById(R.id.tv_clear_calendar);
        selectedDays = new SimpleMonthAdapter.SelectedDays<>();

        rrRL  = (RelativeLayout) findViewById(R.id.rrRL);
        loading_img_rr = (ImageView) findViewById(R.id.loading_img_rr);
        rotation = AnimationUtils.loadAnimation(CustomCalendar.this, R.anim.rotate);
        rotation.setFillAfter(true);

    }

    @Override
    public int getMaxYear()
    {
        int year = Calendar.getInstance().get(Calendar.YEAR);

        return year+1;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day)
    {
        Log.e("Day Selected", day + " / " + month + " / " + year);
    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays)
    {
        Log.e("Date range selected", selectedDays.getFirst().toString() + " --> " + selectedDays.getLast().toString());

        SimpleMonthAdapter.CalendarDay selectedDateFirst =  selectedDays.getFirst();
        SimpleMonthAdapter.CalendarDay selectedDateSecond =  selectedDays.getLast();

        //for first selected date
        Date dateFirst = selectedDateFirst.getDate();
        Calendar calendarFirst = toCalendar(dateFirst);
        int dayOfMonthFirst = calendarFirst.get(Calendar.DAY_OF_MONTH);
        int monthFirst = calendarFirst.get(Calendar.MONTH);
        int yearFirst = calendarFirst.get(Calendar.YEAR);
        String monthStringFirst  = getMonth(monthFirst);
        String finalDateStringFirst = String.valueOf(dayOfMonthFirst)+"-"+monthStringFirst+"-"+String.valueOf(yearFirst);

        //for second selected date
        Date dateSecond = selectedDateSecond.getDate();
        Calendar calendarSecond = toCalendar(dateSecond);
        int dayOfMonthSecond = calendarSecond.get(Calendar.DAY_OF_MONTH);
        int monthSecond = calendarSecond.get(Calendar.MONTH);
        int yearSecond = calendarSecond.get(Calendar.YEAR);
        String monthStringSecond  = getMonth(monthSecond);
        String finalDateStringSecond = String.valueOf(dayOfMonthSecond)+"-"+monthStringSecond+"-"+String.valueOf(yearSecond);


        if ((monthSecond>monthFirst) && (yearSecond>=yearFirst)) {
            Log.e("TAG", "the calendar vaues :  1 ");

            tv_checkin.setText(finalDateStringFirst);
            tv_checkout.setText(finalDateStringSecond);

            diff = daysBetween(calendarSecond, calendarFirst);


        }else if ((monthFirst>monthSecond) && yearSecond>yearFirst){
            tv_checkin.setText(finalDateStringFirst);
            tv_checkout.setText(finalDateStringSecond);
            Log.e("TAG", "the calendar vaues :  2 ");

            diff = daysBetween(calendarSecond, calendarFirst);


        }
        else if ((dayOfMonthSecond>dayOfMonthFirst) && (monthFirst == monthSecond) && (yearFirst == yearSecond)){
            tv_checkin.setText(finalDateStringFirst);
            tv_checkout.setText(finalDateStringSecond);

            Log.e("TAG", "the calendar vaues :  3 ");

            diff = daysBetween(calendarSecond, calendarFirst);

        }
        else {
            tv_checkin.setText(finalDateStringSecond);
            tv_checkout.setText(finalDateStringFirst);

            Log.e("TAG", "the calendar vaues :  4 ");

            diff = daysBetween(calendarFirst, calendarSecond);

        }
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    private void croseClickListner(){
        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void tvNextClickHandler(){

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String startDate = tv_checkin.getText().toString();
                try {
                    String dateFormated = getDateFormated(startDate);
                    Log.e("TAG", "the formatede date is: " + dateFormated);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String endDate = tv_checkout.getText().toString();

                int theAmountFor1Day = 12;

                long thAmountForXDays = theAmountFor1Day*diff;
                Log.e("TAg", "the amunt for selected days are: " + thAmountForXDays);

                if (startDate.contains("Check")){
                    Toast.makeText(CustomCalendar.this, "Please Select check-in check-out dates", Toast.LENGTH_SHORT).show();
                }else {

                    final String OLD_FORMAT = "dd-MMM-yyyy";
                    final String NEW_FORMAT = "MM/dd/yyyy";
                    startDate = datFormating(OLD_FORMAT, NEW_FORMAT, startDate);
                    endDate = datFormating(OLD_FORMAT, NEW_FORMAT, endDate);
                    confirmBooking("1255", roomID, "hroom", "500", "4", String.valueOf(diff), startDate, endDate, "Shoaib", "+92323008754");
                }

            }
        });
    }

    private void confirmBooking(final String Cid, final String Vid,
                                final String VType, final String Req_Price,
                                final String Guests, final String Req_Nights,
                                final String Chkin, final String Chkout,
                                final String CName, final String CMobileNo){

                AlertDialog.Builder alert = new AlertDialog.Builder(CustomCalendar.this);
                alert.setTitle("Alert!");
                alert.setMessage("Do you want to confirm your booking?");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                        rrRL.setVisibility(View.VISIBLE);
                        loading_img_rr.startAnimation(rotation);

                        //HttpRequest
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("url", Urls.BookRRandRV);
                        map.put("Cid",  Cid);
                        map.put("Vid",  Vid);
                        map.put("Vtype",  VType);
                        map.put("Req_Price",  Req_Price);
                        map.put("Guests",  Guests);
                        map.put("Req_Nights",  Req_Nights);
                        map.put("Chkin",  Chkin);
                        map.put("Chkout",  Chkout);
                        map.put("CName",  CName);
                        map.put("CMobileNo",  CMobileNo);

                        new HttpRequester(CustomCalendar.this, map, 1, CustomCalendar.this);

                    }
                });
                alert.show();
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        long end = endDate.getTimeInMillis();
        long start = startDate.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
    }

    public static String getDateFormated(String strDate) throws ParseException {

        DateFormat formatter = new SimpleDateFormat("mm-dd-yyyy");
        Date da = (Date)formatter.parse(strDate);
        System.out.println("==Date is ==" + da);
        String strDateTime = formatter.format(da);

        return strDateTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        Log.e("TAG","The result is here 11: " + response);

        if(serviceCode == 1)
        {
            rrRL.setVisibility(View.GONE);
            rotation.cancel();

            try {
                Logger.log(response);
//                BaseService.dismissProgressDialog();
                JSONObject obj = new JSONObject(response);
                jsonArray = new JSONArray();
                String error = obj.getString("Error");
                if (error.equals("You have already booked it."))
                {
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                }
                else {
                    jsonArray = obj.getJSONArray("Table");
                    String IsSusscess = jsonArray.getJSONObject(0).get("IsSuccess").toString();
                    Log.e("TAg", "my booking status is: " + IsSusscess);
                    showingThankyouDialog();

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    private void checkAvailability(String V_ID, String month){

     /*   HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("url", Urls.checkBookedDates);
        map2.put("Vid", V_ID);
        map2.put("Month", month);
        new HttpRequester(CustomCalendar.this, map2, 1, CustomCalendar.this);
*/
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

    public static String getNextDate(String  curDate) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final Date date;
        Calendar calendar = Calendar.getInstance();
        try {
            date = format.parse(curDate);
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return format.format(calendar.getTime());
    }

    private void clearCalendarCall(){
        tv_clear_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedDays.setFirst(null);
                selectedDays.setLast(null);

            }
        });
    }

    private void showingThankyouDialog()
    {
        final Dialog customeDialog = new Dialog(CustomCalendar.this);
        customeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customeDialog.setContentView(R.layout.order_confirmation_dialog);
        TextView dialog_tv_title = (TextView) customeDialog.findViewById(R.id.dialog_tv_title);
        TextView dialog_tv_detail = (TextView) customeDialog.findViewById(R.id.dialog_tv_detail);
        dialog_tv_title.setText("Booked Successfully");
        dialog_tv_detail.setText("Thank you, Your Room Has been Booked Successfully Our Agent will Contact you soon.");
        RelativeLayout dialog_rl_ok = (RelativeLayout) customeDialog.findViewById(R.id.dialog_rl_ok);

        dialog_rl_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customeDialog.dismiss();

                Intent intent = new Intent(CustomCalendar.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        customeDialog.setCancelable(false);
        customeDialog.show();
    }
    private String datFormating(String orldFormate, String newFormate, String dateToConvert){
        String resultDate = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(orldFormate);
            Date d = sdf.parse(dateToConvert);
            sdf.applyPattern(newFormate);
            resultDate = sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }
}
