package com.shoaibnwar.hotelandrestaurant.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoaibnwar.hotelandrestaurant.Adapters.ViewPagerAdapter;
import com.shoaibnwar.hotelandrestaurant.Adapters.ViewPagerAdapterForFragments;
import com.shoaibnwar.hotelandrestaurant.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gold on 7/13/2018.
 */

public class FoodOrdersFragment extends Fragment {

    public static ViewPager viewPager;
    RelativeLayout tab_rl_diary, tab_rl_pets, tab_rl_equine;
    ImageView tab_iv_dairy, tab_iv_pets, tab_iv_equine;
    TextView tab_tv_dairy, tab_tv_pets, tab_tv_equine;

    ArrayList<HashMap<String, String>> contactsList;
    
    
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.foodorder_fragment, container, false);


        init(v);
        onDairyTabClickListener();
        onPetTabClickListener();
        onEquineTabClickListener();
        viewPagerTabChangeListner();

        addingHardCodeData();

        return v;

    }

    @SuppressLint("NewApi")
    private void init(View view){
        contactsList = new ArrayList<>();

        tab_rl_diary = (RelativeLayout) view.findViewById(R.id.tab_rl_diary);
        tab_rl_pets = (RelativeLayout) view.findViewById(R.id.tab_rl_pets);
        tab_rl_equine = (RelativeLayout) view.findViewById(R.id.tab_rl_equine);

        tab_iv_dairy = (ImageView) view.findViewById(R.id.tab_iv_dairy);
        tab_iv_pets = (ImageView) view.findViewById(R.id.tab_iv_pets);
        tab_iv_equine = (ImageView) view.findViewById(R.id.tab_iv_equine);

        tab_tv_dairy = (TextView) view.findViewById(R.id.tab_tv_dairy);
        tab_tv_pets = (TextView) view.findViewById(R.id.tab_tv_pets);
        tab_tv_equine = (TextView) view.findViewById(R.id.tab_tv_equine);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);

        tab_rl_diary.setBackground(ContextCompat.getDrawable(getActivity(), R.color.white));
        tab_tv_dairy.setTextColor(ContextCompat.getColor(getActivity(), R.color.redish));
        tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);
        viewPager.setCurrentItem(0);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterForFragments adapter = new ViewPagerAdapterForFragments(getActivity().getSupportFragmentManager());

        adapter.addFragment(new FragmentsAll(), "All");
        adapter.addFragment(new FragmentsUnAssigned(), "Un-Assigned");
        adapter.addFragment(new FragmentsAssigned(), "Assigned");
        viewPager.setAdapter(adapter);

    }
    private void onDairyTabClickListener(){

        tab_rl_diary.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                tab_rl_diary.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_button_style_after_click));
                tab_tv_dairy.setTextColor(ContextCompat.getColor(getActivity(), R.color.redish));
                tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);

                tab_rl_pets.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tabs_button_style));
                tab_tv_pets.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                tab_iv_pets.setImageResource(R.drawable.lunch_icon);

                tab_rl_equine.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tabs_button_style));
                tab_tv_equine.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                tab_iv_equine.setImageResource(R.drawable.dinner_icon);

                viewPager.setCurrentItem(0);

            }
        });
    }

    private void onPetTabClickListener(){

        tab_rl_pets.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                tab_rl_diary.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tabs_button_style));
                tab_tv_dairy.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);

                tab_rl_pets.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_button_style_after_click));
                tab_tv_pets.setTextColor(ContextCompat.getColor(getActivity(), R.color.redish));
                tab_iv_pets.setImageResource(R.drawable.lunch_icon);

                tab_rl_equine.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tabs_button_style));
                tab_tv_equine.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                tab_iv_equine.setImageResource(R.drawable.dinner_icon);

                viewPager.setCurrentItem(1);

            }
        });
    }

    private void onEquineTabClickListener(){

        tab_rl_equine.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {

                tab_rl_diary.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tabs_button_style));
                tab_tv_dairy.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);

                tab_rl_pets.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tabs_button_style));
                tab_tv_pets.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                tab_iv_pets.setImageResource(R.drawable.lunch_icon);

                tab_rl_equine.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_button_style_after_click));
                tab_tv_equine.setTextColor(ContextCompat.getColor(getActivity(), R.color.redish));
                tab_iv_equine.setImageResource(R.drawable.dinner_icon);

                viewPager.setCurrentItem(2);

            }
        });
    }


    private void viewPagerTabChangeListner(){

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressLint("NewApi")
            @Override
            public void onPageSelected(int position) {

                Log.e("TAG", "the selected page position is: " + position);

                switch (position){

                    case 0:
                        //if (position == 0){
                        tab_rl_diary.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_button_style_after_click));
                        tab_tv_dairy.setTextColor(ContextCompat.getColor(getActivity(), R.color.redish));
                        tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);

                        tab_rl_pets.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tabs_button_style));
                        tab_tv_pets.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                        tab_iv_pets.setImageResource(R.drawable.lunch_icon);

                        tab_rl_equine.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tabs_button_style));
                        tab_tv_equine.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                        tab_iv_equine.setImageResource(R.drawable.dinner_icon);


                        break;
                    //}
                    case 1:
                        //if (position == 1){
                        tab_rl_diary.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tabs_button_style));
                        tab_tv_dairy.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                        tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);

                        tab_rl_pets.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_button_style_after_click));
                        tab_tv_pets.setTextColor(ContextCompat.getColor(getActivity(), R.color.redish));
                        tab_iv_pets.setImageResource(R.drawable.lunch_icon);

                        tab_rl_equine.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tabs_button_style));
                        tab_tv_equine.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                        tab_iv_equine.setImageResource(R.drawable.dinner_icon);

                        break;

                    //}
                    case 2:
                        //if (position == 2){
                        tab_rl_diary.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tabs_button_style));
                        tab_tv_dairy.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                        tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);

                        tab_rl_pets.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tabs_button_style));
                        tab_tv_pets.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
                        tab_iv_pets.setImageResource(R.drawable.lunch_icon);

                        tab_rl_equine.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_button_style_after_click));
                        tab_tv_equine.setTextColor(ContextCompat.getColor(getActivity(), R.color.redish));
                        tab_iv_equine.setImageResource(R.drawable.dinner_icon);


                        break;
                    //}

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void insertingDataIntoDbAssignment(){

      /*  AssignmentesDB assignmentesDB = new AssignmentesDB(Assignments.this);
        AssignmentDbHelper helper = new AssignmentDbHelper();

        helper.setAssignerID("3");
        helper.setAssignerName("Rameez Raja");
        helper.setAssignerContact("+923456236521");
        helper.setAssignerCompany("IdeaCentricity");
        helper.setAssignerAddress("Arfa Software technology park, Lahore");
        helper.setStartDate("15-july-2018");
        helper.setStartTime("02:10 pm");
        helper.setSalemanName("Hassan Raza");
        helper.setSalemanContact("+92345863223");
        helper.setSalemanAddress("Lahore, Pakistan");
        helper.setSalemanId("4");

        //inseting data into database
        long isInserted = assignmentesDB.insertDatatoDb(helper);
        if (isInserted!=-1) {
            Log.e("TAG", "DATA inserted into Assignment Table succesfully");

        }*/

    }

    private void addingHardCodeData()
    {

        String name1 = "Hamza Khan";
        String name1_phone = "+923454166554";
        String name1_address = "Anarkali lahore, Pakistan";
        String name1_company = "technologics.cp";
        String name1Lat = "31.571359";
        String name1Lng = "74.310355";
        String name1ImageUri = "-1";

        HashMap<String, String> contactDetail1 = new HashMap<>();
        contactDetail1.put("number", name1_phone);
        contactDetail1.put("name", name1);
        contactDetail1.put("imageuri", name1ImageUri);
        contactDetail1.put("lat", name1Lat);
        contactDetail1.put("lng", name1Lng);
        contactDetail1.put("address", name1_address);
        contactDetail1.put("company", name1_company);

        contactsList.add(contactDetail1);

        String name2 = "Abaas Ali";
        String name2_phone = "+923252654695";
        String name2_address = "Model Town lahore, Pakistan";
        String name2_company = "rehman interprises";
        String name2Lat = "31.254564";
        String name2Lng = "74.264154";
        String name2ImageUri = "-1";

        HashMap<String, String> contactDetail2 = new HashMap<>();
        contactDetail2.put("number", name2_phone);
        contactDetail2.put("name", name2);
        contactDetail2.put("imageuri", name2ImageUri);
        contactDetail2.put("lat", name2Lat);
        contactDetail2.put("lng", name2Lng);
        contactDetail2.put("address", name2_address);
        contactDetail2.put("company", name2_company);
        contactsList.add(contactDetail2);

        String name3 = "Rizwan Nisar";
        String name3_phone = "+923214585425";
        String name3_address = "Gulberg II lahore, Pakistan";
        String name3_company = "Shaheen trders";
        String name3Lat = "31.5465446";
        String name3Lng = "74.1313365";
        String name3ImageUri = "-1";

        HashMap<String, String> contactDetail3 = new HashMap<>();
        contactDetail3.put("number", name3_phone);
        contactDetail3.put("name", name3);
        contactDetail3.put("imageuri", name3ImageUri);
        contactDetail3.put("lat", name3Lat);
        contactDetail3.put("lng", name3Lng);
        contactDetail3.put("address", name3_address);
        contactDetail3.put("company", name3_company);
        contactsList.add(contactDetail3);

        String name4 = "Kashif latif";
        String name4_phone = "+923458251254";
        String name4_address = "Johar Town lahore, Pakistan";
        String name4_company = "Aqha Stells";
        String name4Lat = "31.85857897";
        String name4Lng = "74.8798797";
        String name4ImageUri = "-1";

        HashMap<String, String> contactDetail4 = new HashMap<>();
        contactDetail4.put("number", name4_phone);
        contactDetail4.put("name", name4);
        contactDetail4.put("imageuri", name4ImageUri);
        contactDetail4.put("lat", name4Lat);
        contactDetail4.put("lng", name4Lng);
        contactDetail4.put("address", name4_address);
        contactDetail4.put("company", name4_company);
        contactsList.add(contactDetail4);

        String name5 = "Adeel Khalil";
        String name5_phone = "+923135245265";
        String name5_address = "Behria town lahore, Pakistan";
        String name5_company = "Ali brothers";
        String name5Lat = "31.49646546";
        String name5Lng = "74.16544656";
        String name5ImageUri = "-1";

        HashMap<String, String> contactDetail5 = new HashMap<>();
        contactDetail5.put("number", name5_phone);
        contactDetail5.put("name", name5);
        contactDetail5.put("imageuri", name5ImageUri);
        contactDetail5.put("lat", name5Lat);
        contactDetail5.put("lng", name5Lng);
        contactDetail5.put("address", name5_address);
        contactDetail5.put("company", name5_company);
        contactsList.add(contactDetail5);

        String name6 = "Salman saeed";
        String name6_phone = "+923334525485";
        String name6_address = "Shadra lahore, Pakistan";
        String name6_company = "Final Algo";
        String name6Lat = "31.465456466";
        String name6Lng = "74.65465464";
        String name6ImageUri = "";

        HashMap<String, String> contactDetail6 = new HashMap<>();
        contactDetail6.put("number", name6_phone);
        contactDetail6.put("name", name6);
        contactDetail6.put("imageuri", name6ImageUri);
        contactDetail6.put("lat", name6Lat);
        contactDetail6.put("lng", name6Lng);
        contactDetail6.put("address", name6_address);
        contactDetail6.put("company", name6_company);
        contactsList.add(contactDetail6);

        String name7 = "Usman Rafique";
        String name7_phone = "+9233452545216";
        String name7_address = "Multan road lahore, Pakistan";
        String name7_company = "Me and He backers";
        String name7Lat = "31.3164344646";
        String name7Lng = "74.545465464646";
        String name7ImageUri = "-1";

        HashMap<String, String> contactDetail7 = new HashMap<>();
        contactDetail7.put("number", name7_phone);
        contactDetail7.put("name", name7);
        contactDetail7.put("imageuri", name7ImageUri);
        contactDetail7.put("lat", name7Lat);
        contactDetail7.put("lng", name7Lng);
        contactDetail7.put("address", name7_address);
        contactDetail7.put("company", name7_company);
        contactsList.add(contactDetail7);

        String name8 = "Ammar suhail";
        String name8_phone = "+923215426330";
        String name8_address = "Behria town lahore, Pakistan";
        String name8_company = "Cocacola";
        String name8Lat = "31.875797987";
        String name8Lng = "74.579798";
        String name8ImageUri = "-1";

        HashMap<String, String> contactDetail8 = new HashMap<>();
        contactDetail8.put("number", name8_phone);
        contactDetail8.put("name", name8);
        contactDetail8.put("imageuri", name8ImageUri);
        contactDetail8.put("lat", name8Lat);
        contactDetail8.put("lng", name8Lng);
        contactDetail8.put("address", name8_address);
        contactDetail8.put("company", name8_company);
        contactsList.add(contactDetail8);

        String name9 = "Akranm ismaeil";
        String name9_phone = "+923224502150";
        String name9_address = "Urdu bazar lahore, Pakistan";
        String name9_company = "Nestlay";
        String name9Lat = "31.546464";
        String name9Lng = "74.5445465";
        String name9ImageUri = "-1";

        HashMap<String, String> contactDetail9 = new HashMap<>();
        contactDetail9.put("number", name9_phone);
        contactDetail9.put("name", name9);
        contactDetail9.put("imageuri", name9ImageUri);
        contactDetail9.put("lat", name9Lat);
        contactDetail9.put("lng", name9Lng);
        contactDetail9.put("address", name9_address);
        contactDetail9.put("company", name9_company);
        contactsList.add(contactDetail9);
/*

        ContactDatabase contactDatabase = new ContactDatabase(Assignments.this);
        int count  = contactDatabase.getCount();
        Log.e("TAg", "the data inserted or not " + contactsList.size());
        if (count==0) {
            for (int i = 0; i < contactsList.size(); i++) {

                String contactName  = contactsList.get(i).get("name").toString();
                String contactNumber  = contactsList.get(i).get("number").toString();
                String contactAddress =  contactsList.get(i).get("address").toString();
                String companyName = contactsList.get(i).get("company").toString();
                String contactLat =  contactsList.get(i).get("lat").toString();
                String contactLng =  contactsList.get(i).get("lng").toString();
                String status = "0";
                ContactDbHelper helper = new ContactDbHelper();
                helper.setContactName(contactName);
                helper.setContactPhone(contactNumber);
                helper.setContactAddress(contactAddress);
                helper.setContactCompany(companyName);
                helper.setContactLat(contactLat);
                helper.setContactLng(contactLng);
                helper.setContactStatus(status);
                long inSerted =  contactDatabase.insertDatatoDb(helper);
                Log.e("TAg", "the data inserted or not " + inSerted);
            }
        }
*/


   /*     progress_bar.setVisibility(View.GONE);
        contactsAdapter = new Contacts(ContactsList.this, contactsList);
        rc_list.setAdapter(contactsAdapter);*/

    }

    private void updateTable()
    {

       /* ContactDatabase dbContact = new ContactDatabase(Assignments.this);
        boolean isUpdated = dbContact.updateTable(Integer.valueOf("5"), "4");
        Log.e("TAG", "the value is update or not " + isUpdated);
        if (isUpdated){

        }*/
    }


}
