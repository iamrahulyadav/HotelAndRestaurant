package com.shoaibnwar.hotelandrestaurant.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoaibnwar.hotelandrestaurant.Activities.FoodSingleItem;
import com.shoaibnwar.hotelandrestaurant.R;

/**
 * Created by gold on 7/9/2018.
 */

public class DinnerFragment extends Fragment {

    RelativeLayout rl_item_1, rl_item_2, rl_item_3, rl_item_4, rl_item_5, rl_item_6, rl_item_7, rl_item_8;
    TextView tv_item_name_1, tv_item_name_2, tv_item_name_3, tv_item_name_4, tv_item_name_5, tv_item_name_6, tv_item_name_7, tv_item_name_8;
    TextView tv_item_price_1, tv_item_price_2, tv_item_price_3, tv_item_price_4, tv_item_price_5, tv_item_price_6, tv_item_price_7, tv_item_price_8;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_layout_dinner, container, false);

        init(v);
        clicksFunctions();
        return v;
    }

    private void init(View view) {

        rl_item_1 = (RelativeLayout) view.findViewById(R.id.rl_item_1);
        rl_item_2 = (RelativeLayout) view.findViewById(R.id.rl_item_2);
        rl_item_3 = (RelativeLayout) view.findViewById(R.id.rl_item_3);
        rl_item_4 = (RelativeLayout) view.findViewById(R.id.rl_item_4);
        rl_item_5 = (RelativeLayout) view.findViewById(R.id.rl_item_5);
        rl_item_6 = (RelativeLayout) view.findViewById(R.id.rl_item_6);
        rl_item_7 = (RelativeLayout) view.findViewById(R.id.rl_item_7);
        rl_item_8 = (RelativeLayout) view.findViewById(R.id.rl_item_8);

        tv_item_name_1 = (TextView) view.findViewById(R.id.tv_item_name_1);
        tv_item_name_2 = (TextView) view.findViewById(R.id.tv_item_name_2);
        tv_item_name_3 = (TextView) view.findViewById(R.id.tv_item_name_3);
        tv_item_name_4 = (TextView) view.findViewById(R.id.tv_item_name_4);
        tv_item_name_5 = (TextView) view.findViewById(R.id.tv_item_name_5);
        tv_item_name_6 = (TextView) view.findViewById(R.id.tv_item_name_6);
        tv_item_name_7 = (TextView) view.findViewById(R.id.tv_item_name_7);
        tv_item_name_8 = (TextView) view.findViewById(R.id.tv_item_name_8);

        tv_item_price_1 = (TextView) view.findViewById(R.id.tv_item_price_1);
        tv_item_price_2 = (TextView) view.findViewById(R.id.tv_item_price_2);
        tv_item_price_3 = (TextView) view.findViewById(R.id.tv_item_price_3);
        tv_item_price_4 = (TextView) view.findViewById(R.id.tv_item_price_4);
        tv_item_price_5 = (TextView) view.findViewById(R.id.tv_item_price_5);
        tv_item_price_6 = (TextView) view.findViewById(R.id.tv_item_price_6);
        tv_item_price_7 = (TextView) view.findViewById(R.id.tv_item_price_7);
        tv_item_price_8 = (TextView) view.findViewById(R.id.tv_item_price_8);



    }

    private void clicksFunctions(){
        item1Click();
        item2Click();
        item3Click();
        item4Click();
        item5Click();
        item6Click();
        item7Click();
        item8Click();
    }

    private void item1Click(){
        rl_item_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemName = tv_item_name_1.getText().toString();
                String itemPrice = tv_item_price_1.getText().toString();

                Intent i = new Intent(getActivity(), FoodSingleItem.class);
                i.putExtra("ImagePosition", "1");
                i.putExtra("itemName", itemName);
                i.putExtra("itemPrice", itemPrice);

                getActivity().startActivity(i);
            }
        });

    }
    private void item2Click(){
        rl_item_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemName = tv_item_name_2.getText().toString();
                String itemPrice = tv_item_price_2.getText().toString();

                Intent i = new Intent(getActivity(), FoodSingleItem.class);
                i.putExtra("ImagePosition", "2");
                i.putExtra("itemName", itemName);
                i.putExtra("itemPrice", itemPrice);
                getActivity().startActivity(i);
            }
        });
    }
    private void item3Click(){
        rl_item_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemName = tv_item_name_3.getText().toString();
                String itemPrice = tv_item_price_3.getText().toString();

                Intent i = new Intent(getActivity(), FoodSingleItem.class);
                i.putExtra("ImagePosition", "3");
                i.putExtra("itemName", itemName);
                i.putExtra("itemPrice", itemPrice);
                getActivity().startActivity(i);
            }
        });
    }
    private void item4Click(){
        rl_item_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = tv_item_name_4.getText().toString();
                String itemPrice = tv_item_price_4.getText().toString();

                Intent i = new Intent(getActivity(), FoodSingleItem.class);
                i.putExtra("ImagePosition", "4");
                i.putExtra("itemName", itemName);
                i.putExtra("itemPrice", itemPrice);
                getActivity().startActivity(i);
            }
        });
    }
    private void item5Click(){
        rl_item_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemName = tv_item_name_5.getText().toString();
                String itemPrice = tv_item_price_5.getText().toString();

                Intent i = new Intent(getActivity(), FoodSingleItem.class);
                i.putExtra("ImagePosition", "5");
                i.putExtra("itemName", itemName);
                i.putExtra("itemPrice", itemPrice);
                getActivity().startActivity(i);
            }
        });
    }

    private void item6Click(){
        rl_item_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemName = tv_item_name_6.getText().toString();
                String itemPrice = tv_item_price_6.getText().toString();

                Intent i = new Intent(getActivity(), FoodSingleItem.class);
                i.putExtra("ImagePosition", "6");
                i.putExtra("itemName", itemName);
                i.putExtra("itemPrice", itemPrice);
                getActivity().startActivity(i);
            }
        });
    }
    private void item7Click(){
        rl_item_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemName = tv_item_name_7.getText().toString();
                String itemPrice = tv_item_price_7.getText().toString();

                Intent i = new Intent(getActivity(), FoodSingleItem.class);
                i.putExtra("ImagePosition", "7");
                i.putExtra("itemName", itemName);
                i.putExtra("itemPrice", itemPrice);
                getActivity().startActivity(i);
            }
        });
    }
    private void item8Click(){
        rl_item_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemName = tv_item_name_8.getText().toString();
                String itemPrice = tv_item_price_8.getText().toString();

                Intent i = new Intent(getActivity(), FoodSingleItem.class);
                i.putExtra("ImagePosition", "8");
                i.putExtra("itemName", itemName);
                i.putExtra("itemPrice", itemPrice);
                getActivity().startActivity(i);
            }
        });
    }
}