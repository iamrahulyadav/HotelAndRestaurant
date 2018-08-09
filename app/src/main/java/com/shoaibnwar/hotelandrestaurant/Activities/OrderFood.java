package com.shoaibnwar.hotelandrestaurant.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.shoaibnwar.hotelandrestaurant.Fragments.BreakfastFragment;
import com.shoaibnwar.hotelandrestaurant.Fragments.DinnerFragment;
import com.shoaibnwar.hotelandrestaurant.Fragments.LunchFragment;
import com.shoaibnwar.hotelandrestaurant.R;

import java.util.ArrayList;
import java.util.List;

public class OrderFood extends AppCompatActivity {

    //  private TabLayout tabLayout;
    public static ViewPager viewPager;
    RelativeLayout tab_rl_diary, tab_rl_pets, tab_rl_equine;
    ImageView tab_iv_dairy, tab_iv_pets, tab_iv_equine;
    TextView tab_tv_dairy, tab_tv_pets, tab_tv_equine;
    RelativeLayout rl_back_arrow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_food);


        init();
        onDairyTabClickListener();
        onPetTabClickListener();
        onEquineTabClickListener();
        viewPagerTabChangeListner();
        onBackPresArrow();

    }

    @SuppressLint("NewApi")
    private void init(){



        tab_rl_diary = (RelativeLayout) findViewById(R.id.tab_rl_diary);
        tab_rl_pets = (RelativeLayout) findViewById(R.id.tab_rl_pets);
        tab_rl_equine = (RelativeLayout) findViewById(R.id.tab_rl_equine);

        tab_iv_dairy = (ImageView) findViewById(R.id.tab_iv_dairy);
        tab_iv_pets = (ImageView) findViewById(R.id.tab_iv_pets);
        tab_iv_equine = (ImageView) findViewById(R.id.tab_iv_equine);

        tab_tv_dairy = (TextView) findViewById(R.id.tab_tv_dairy);
        tab_tv_pets = (TextView) findViewById(R.id.tab_tv_pets);
        tab_tv_equine = (TextView) findViewById(R.id.tab_tv_equine);

        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);


        tab_rl_diary.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.color.white));
        tab_tv_dairy.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPink));
        tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);
        viewPager.setCurrentItem(0);




    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new BreakfastFragment(), "Breakfast");
        adapter.addFragment(new LunchFragment(), "Lucnh");
        adapter.addFragment(new DinnerFragment(), "Dinner");
        viewPager.setAdapter(adapter);


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void onDairyTabClickListener(){

        tab_rl_diary.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                tab_rl_diary.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_button_style_after_click));
                tab_tv_dairy.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);

                tab_rl_pets.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                tab_tv_pets.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tab_iv_pets.setImageResource(R.drawable.lunch_icon);

                tab_rl_equine.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                tab_tv_equine.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
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
                tab_rl_diary.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                tab_tv_dairy.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);

                tab_rl_pets.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_button_style_after_click));
                tab_tv_pets.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPink));
                tab_iv_pets.setImageResource(R.drawable.lunch_icon);

                tab_rl_equine.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                tab_tv_equine.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
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

                tab_rl_diary.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                tab_tv_dairy.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);

                tab_rl_pets.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                tab_tv_pets.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                tab_iv_pets.setImageResource(R.drawable.lunch_icon);

                tab_rl_equine.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_button_style_after_click));
                tab_tv_equine.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPink));
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
                        tab_rl_diary.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_button_style_after_click));
                        tab_tv_dairy.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPink));
                        tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);

                        tab_rl_pets.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                        tab_tv_pets.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        tab_iv_pets.setImageResource(R.drawable.lunch_icon);

                        tab_rl_equine.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                        tab_tv_equine.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        tab_iv_equine.setImageResource(R.drawable.dinner_icon);


                        break;
                    //}
                    case 1:
                        //if (position == 1){
                        tab_rl_diary.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                        tab_tv_dairy.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);

                        tab_rl_pets.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_button_style_after_click));
                        tab_tv_pets.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPink));
                        tab_iv_pets.setImageResource(R.drawable.lunch_icon);

                        tab_rl_equine.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                        tab_tv_equine.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        tab_iv_equine.setImageResource(R.drawable.dinner_icon);

                        break;

                    //}
                    case 2:
                        //if (position == 2){
                        tab_rl_diary.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                        tab_tv_dairy.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        tab_iv_dairy.setImageResource(R.drawable.breakfast_icon);

                        tab_rl_pets.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tabs_button_style));
                        tab_tv_pets.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        tab_iv_pets.setImageResource(R.drawable.lunch_icon);

                        tab_rl_equine.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_button_style_after_click));
                        tab_tv_equine.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPink));
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
    private void onBackPresArrow()
    {
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}