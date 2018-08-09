package com.shoaibnwar.hotelandrestaurant.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shoaibnwar.hotelandrestaurant.Database.FoodOrderDb;
import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Utils.Logger;
import com.shoaibnwar.hotelandrestaurant.Utils.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SingleItemDetailActivity extends AppCompatActivity implements AsyncTaskCompleteListener {

    ImageView iv_back_arrow;
    ImageView iv_person2;

    TextView tv_m_name, tv_m_phome, tv_m_company;
    LinearLayout ll_address;

    JSONArray jsonArray;
    RelativeLayout rrRL;
    ImageView loading_img_rr;
    Animation rotation;

    String imageUrl;
    String contacttName=null, contactNumber=null;
    String contacttAddress=null, contactLat=null, contactLng=null;
    String contactId=null, contactStatus=null, contactCompany=null;

    TextView tv_address_line_1, tv_address_line_2;
    TextView tv_assign_now;
    int checkedIdis = 0;

    String saleManName = null;
    String saleManContact = null;
    String saleManAddress = null;
    String saleManLat = null;
    String saleManLng = null;
    String saleManId = null;
    Spinner sp_for_sale_persons;
    String tableId = null;

    SharedPreferences sharedPreferences;

    LinearLayout ll_start_date, ll_start_time;
    public TextView tv_date, tv_time;

    Calendar myCalendar = Calendar.getInstance();
    String mSelectedDate = "";
    Animation animShake;

    LinearLayout ll_when_assined, ll_when_not_assigne;
    TextView tv_saleman_m_name, tv_saleman_m_phome, tv_saleman_m_id;
    TextView tv_saleman_m_satrt_date, tv_saleman_m_satrt_time;
    Button bt_confirm_order, bt_start_assignment;
    RelativeLayout rl_spinner_layout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_detail);
        System.gc();

        init();
        gettingIntentsValues();
        onBackArrowPress();
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
        //assignNowButtonClickHandler();
        selectDate();
        showTimePickerDialog();
        spinnerITemClickHandler();
        openingSpiinerOnViewClick();
        startAssignmentClickHandler();
        btConfirmClickHandler();


    }//end of onCreate method

    private void init(){

        sharedPreferences = getSharedPreferences(SharedPrefs.PREF_NAME, Context.MODE_PRIVATE);
        iv_back_arrow = (ImageView) findViewById(R.id.iv_back_arrow);
        rrRL  = (RelativeLayout) findViewById(R.id.rrRL);
        loading_img_rr = (ImageView) findViewById(R.id.loading_img_rr);
        iv_person2 = (ImageView) findViewById(R.id.iv_person2);

        tv_m_phome = (TextView) findViewById(R.id.tv_m_phome);
        tv_m_name = (TextView) findViewById(R.id.tv_m_name);
        tv_m_company = (TextView) findViewById(R.id.tv_m_company);

        tv_address_line_1 = (TextView) findViewById(R.id.tv_address_line_1);
        tv_address_line_2 = (TextView) findViewById(R.id.tv_address_line_2);

        bt_start_assignment = (Button) findViewById(R.id.bt_start_assignment);
        bt_confirm_order = (Button) findViewById(R.id.bt_confirm_order);

        tv_assign_now = (TextView) findViewById(R.id.tv_assign_now);
        sp_for_sale_persons = (Spinner) findViewById(R.id.sp_for_sale_persons);

        ll_start_date = (LinearLayout) findViewById(R.id.ll_start_date);
        ll_start_time = (LinearLayout) findViewById(R.id.ll_start_time);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_time = (TextView) findViewById(R.id.tv_time);

        tv_saleman_m_satrt_date = (TextView) findViewById(R.id.tv_saleman_m_satrt_date);
        tv_saleman_m_satrt_time = (TextView) findViewById(R.id.tv_saleman_m_satrt_time);
        rl_spinner_layout= (RelativeLayout) findViewById(R.id.rl_spinner_layout);


        rotation = AnimationUtils.loadAnimation(SingleItemDetailActivity.this, R.anim.rotate);
        rotation.setFillAfter(true);

        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        ll_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SingleItemDetailActivity.this, MapsAssignemntSearch.class);
                startActivityForResult(i, 201);
            }
        });


        ArrayAdapter adapter = ArrayAdapter.createFromResource(SingleItemDetailActivity.this,
                R.array.sale_person, R.layout.spinner_item_sale_man);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_for_sale_persons.setAdapter(adapter);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        ll_when_assined = (LinearLayout) findViewById(R.id.ll_when_assined);
        ll_when_not_assigne = (LinearLayout) findViewById(R.id.ll_when_not_assigne);
        tv_saleman_m_name = (TextView) findViewById(R.id.tv_saleman_m_name);
        tv_saleman_m_phome = (TextView) findViewById(R.id.tv_saleman_m_phome);
        // tv_saleman_m_company = (TextView) findViewById(R.id.tv_saleman_m_company);
        tv_saleman_m_id = (TextView) findViewById(R.id.tv_saleman_m_id);


    }//end of onCreate


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void gettingIntentsValues(){
        Intent intentData = getIntent();

        contacttName = "Rehman Khan";
        contactNumber = "032352125213";
        contacttAddress = "Arfa Technology Lahore";
        contactCompany = "iTechnology";
        contactLat = "32.236526";
        contactLng = "74.236598";
        contactId = "2";
        contactStatus = intentData.getStringExtra("status");
        tableId = intentData.getStringExtra("tableId");
  /*      contacttName = intentData.getStringExtra("contactName");
        contactNumber = intentData.getStringExtra("contactNumber");
        contacttAddress = intentData.getStringExtra("contactAddress");
        contactCompany = intentData.getStringExtra("contactCompany");
        contactLat = intentData.getStringExtra("contactLat");
        contactLng = intentData.getStringExtra("contactLng");
        contactId = intentData.getStringExtra("contactId");
        contactStatus = intentData.getStringExtra("status");*/


        //imageUrl = intentData.getStringExtra("imageUrl");

       /* if (!imageUrl.equals("-1")) {
            Log.e("TAg", "the image url is: " + imageUrl);
            Picasso.with(SingleItemDetailActivity.this)
                    .load(imageUrl)
                    .placeholder(R.drawable.amu_bubble_shadow)
                    //.fit()
                    .into(iv_person2);
        }
        else {
            Picasso.with(SingleItemDetailActivity.this)
                    .load(R.drawable.person_icon)
                    .placeholder(R.drawable.amu_bubble_shadow)
                    //.fit()
                    .into(iv_person2);

        }*/



        if (contacttName!=null){
            tv_m_name.setText(contacttName);
        }

        if (contactNumber!=null){
            tv_m_phome.setText(contactNumber);
        }
        if (contacttAddress!=null){
            tv_address_line_1.setText(contacttAddress);
            tv_address_line_2.setVisibility(View.GONE);
        }
        if (contactCompany!=null){
            tv_m_company.setText(contactCompany);
        }

        if (!contactStatus.equals("0")){

            tv_assign_now.setText("Assigned");
            tv_assign_now.setTextColor(getResources().getColor(R.color.blue));
            ll_when_assined.setVisibility(View.VISIBLE);
            ll_when_not_assigne.setVisibility(View.GONE);

            /*AssignmentesDB db = new AssignmentesDB(SingleItemDetailActivity.this);
            ArrayList<AssignmentDbHelper> helper = db.getSingleRecord(contactStatus);

            Log.e("TAG", "the array list is: " + db.getCount());

            saleManName = helper.get(0).getSalemanName();
            saleManContact = helper.get(0).getSalemanContact();
            saleManAddress = helper.get(0).getSalemanAddress();
            String startDate = helper.get(0).getStartDate();
            String startTime = helper.get(0).getStartTime();
            String tableId = helper.get(0).getId();
*/

            saleManName = "Muneeb Ahmad";
            saleManContact = "Raheel Shareef";
            saleManAddress = "Model Town Lahore";
            String startDate = "27-Jul-2018";
            String startTime = "01:14 PM";
            String tableId ="2";


            tv_saleman_m_name.setText(saleManName);
            tv_saleman_m_phome.setText(saleManContact);
            // tv_saleman_m_company.setText(saleManAddress);
            tv_saleman_m_id.setText(tableId);
            tv_saleman_m_satrt_date.setText(startDate);
            tv_saleman_m_satrt_time.setText(startTime);

            bt_confirm_order.setVisibility(View.GONE);
            bt_start_assignment.setVisibility(View.VISIBLE);

        }else {
            ll_when_assined.setVisibility(View.GONE);
            ll_when_not_assigne.setVisibility(View.VISIBLE);

            bt_confirm_order.setVisibility(View.VISIBLE);
            bt_start_assignment.setVisibility(View.GONE);

            Date currentDate = Calendar.getInstance().getTime();
            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            tv_date.setText(sdf.format(currentDate));
            mSelectedDate = sdf.format(currentDate);

            Date currentTim = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String currentTiem  = dateFormat.format(currentTim);
            tv_time.setText(currentTiem);

        }




}

    private void onBackArrowPress(){
        iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        rotation.cancel();

        if(serviceCode == 12)
        {
            //isServiceInProgressFlag = false;

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
                    String IsCusscess = jsonArray.getJSONObject(0).get("IsSuccess").toString();

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }




    private void selectDate() {

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dFragment = new DatePickerFragment();
                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Date Picker");


            }
        });

    }

    private void showTimePickerDialog(){

        ll_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dFragment = new TimePickerFragment();
                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Time Picker");

            }
        });

    }

    private void spinnerITemClickHandler(){
        sp_for_sale_persons.setClickable(false);
        sp_for_sale_persons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==1){

                    saleManName ="Nadeem Ali";
                    saleManContact = "+923234524510";
                    saleManAddress = "Ravi Road Lahore, Pakistan";
                    saleManLat = "32.2546522";
                    saleManLng = "74.25463254";
                    saleManId = "1";

                }
                if (position==2){

                    saleManName ="Malik Ahmad";
                    saleManContact = "+923450125462";
                    saleManAddress = "Muzang Chungi Lahore, Pakistan";
                    saleManLat = "32.5426325";
                    saleManLng = "74.78154566";
                    saleManId = "2";
                }
                if (position==3){

                    saleManName ="Rana Naveed";
                    saleManContact = "+9232145256036";
                    saleManAddress = "Township road Lahore, Pakistan";
                    saleManLat = "32.5621464";
                    saleManLng = "74.78941654";
                    saleManId = "3";
                }
                if (position==4){

                    saleManName ="Anas Saeed";
                    saleManContact = "+923124524520";
                    saleManAddress = "Mughal pura Lahore, Pakistan";
                    saleManLat = "32.74469745";
                    saleManLng = "74.49676545646";
                    saleManId = "4";

                }
                if (position==5){

                    saleManName ="Umar Khan";
                    saleManContact = "+923335420125";
                    saleManAddress = "Sanda road Lahore, Pakistan";
                    saleManLat = "32.21654646546";
                    saleManLng = "74.3216543478";
                    saleManId = "5";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            /*
                Create a DatePickerDialog using Theme.

                    DatePickerDialog(Context context, int theme, DatePickerDialog.OnDateSetListener listener,
                        int year, int monthOfYear, int dayOfMonth)
             */

            // DatePickerDialog THEME_DEVICE_DEFAULT_LIGHT
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    R.style.datepicker,this,year,month,day);


            // Return the DatePickerDialog
            return  dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            TextView tv = (TextView) getActivity().findViewById(R.id.tv_date);
            Calendar cal = Calendar.getInstance(); cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);

            Date chosenDate = cal.getTime();
            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            String dateis = sdf.format(chosenDate);
            tv.setText(dateis);

        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            // Get a Calendar instance
            final Calendar calendar = Calendar.getInstance();
            // Get the current hour and minute
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);


            // TimePickerDialog Theme : THEME_DEVICE_DEFAULT_LIGHT
            TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                    R.style.datepicker,this,hour,minute,false);


            // Return the TimePickerDialog
            return tpd;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
            // Do something with the returned time
            TextView tv = (TextView) getActivity().findViewById(R.id.tv_time);

            String am_pm = "";

            Calendar datetime = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            datetime.set(Calendar.MINUTE, minute);

            if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                am_pm = "AM";
            else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                am_pm = "PM";


            String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
            String hours = strHrsToShow;
            int inthurs = Integer.parseInt(hours);
            if (inthurs<10){
                String stringhours = String.valueOf(inthurs);
                hours = "0"+hours;
            }
            int minuts = datetime.get(Calendar.MINUTE);
            String stringMinuts = String.valueOf(minuts);
            if (minute<10){
                stringMinuts = "0"+stringMinuts;

            }
            tv.setText( hours+":"+stringMinuts+" "+am_pm );

            //tv.setText("HHH:MMM\n" + hourOfDay + ":" + minute);
        }
    }

    private void openingSpiinerOnViewClick(){

        rl_spinner_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp_for_sale_persons.performClick();
            }
        });
    }

    private void startAssignmentClickHandler(){

        bt_start_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = tv_address_line_1.getText().toString();
                String bookingDate = tv_saleman_m_satrt_date.getText().toString();
                String bookingTime = tv_saleman_m_satrt_time.getText().toString();

                Intent startAssignemnts = new Intent(SingleItemDetailActivity.this, StartTripMaps.class);
                startAssignemnts.putExtra("address", address);
                startAssignemnts.putExtra("bookingDate", bookingDate);
                startAssignemnts.putExtra("bookingTime", bookingTime);
                startAssignemnts.putExtra("assignerId", contactNumber);
                startAssignemnts.putExtra("salemanid", saleManContact);
                startActivity(startAssignemnts);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 201) {
            if(resultCode == Activity.RESULT_OK){
                String Address=data.getStringExtra("result");
                tv_address_line_1.setText(Address);
                if (Address.contains("Pakistan")){
                    tv_address_line_2.setVisibility(View.GONE);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void btConfirmClickHandler()
    {

        bt_confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FoodOrderDb db = new FoodOrderDb(SingleItemDetailActivity.this);
                boolean isUpdated = db.updateTable(Integer.valueOf(tableId), "1");
                if (isUpdated){
                    Intent i = new Intent(SingleItemDetailActivity.this, HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(SingleItemDetailActivity.this, "Order Assining Faild", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
