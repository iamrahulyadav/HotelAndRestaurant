package com.shoaibnwar.hotelandrestaurant.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoaibnwar.hotelandrestaurant.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodSingleItem extends AppCompatActivity {

    ImageView iv_item_image;
    ImageView iv_item_quantity_less, iv_item_quantity_more;
    TextView tv_quantitiy;
    TextView tv_unit_price;
    TextView tv_total_price;
    TextView tv_item_name;
    TextView tv_detail;
    String unitPriceOfItem;
    int unitPrice;
    RelativeLayout rl_confirm, rl_more;
    RelativeLayout rl_back_arrow;

    ArrayList<HashMap<String, String>> selectedITemList;
    String clickPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_signle_item);

        init();
        increaseQuantity();
        decreasingQuantity();
        orderConfirmation();
        onBackPresArrow();
        addMoreItems();

    }
    private void init(){
        iv_item_image = (ImageView) findViewById(R.id.iv_item_image);
        tv_unit_price = (TextView) findViewById(R.id.tv_unit_price);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_item_name = (TextView) findViewById(R.id.tv_item_name);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        iv_item_quantity_less = (ImageView) findViewById(R.id.iv_item_quantity_less);
        iv_item_quantity_more = (ImageView) findViewById(R.id.iv_item_quantity_more);
        tv_quantitiy = (TextView) findViewById(R.id.tv_quantitiy);
        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        rl_back_arrow.bringToFront();

        rl_confirm = (RelativeLayout) findViewById(R.id.rl_confirm);
        rl_more = (RelativeLayout) findViewById(R.id.rl_more);

        Intent i = getIntent();
        clickPosition = i.getStringExtra("ImagePosition");
        String itemName = i.getStringExtra("itemName");
        String itemPrice = i.getStringExtra("itemPrice");

        tv_item_name.setText(itemName);
        tv_unit_price.setText(itemPrice);
        tv_total_price.setText(itemPrice);

        unitPriceOfItem = itemPrice.split("\\.")[1];
        Log.e("TAG", "ADFASDFASD " + unitPriceOfItem);
        unitPrice = Integer.valueOf(unitPriceOfItem);

        if (clickPosition.equals("1")){
            iv_item_image.setImageResource(R.drawable.bissell_breakfast);
            tv_detail.setText("Assortment of Fried Egg, Sandwich, Lobia, Sausages & Green Salad");
        }
        if (clickPosition.equals("2")){
            iv_item_image.setImageResource(R.drawable.samosas);
            tv_detail.setText("3 samosas with tea, chattni & masala");}
        if (clickPosition.equals("3")){iv_item_image.setImageResource(R.drawable.coffee);
            tv_detail.setText("Coffee Biscuits & Activia");}
        if (clickPosition.equals("4")){iv_item_image.setImageResource(R.drawable.anda_paratha);
            tv_detail.setText("Fried Ege Butter & Paratha");}
        if (clickPosition.equals("5")){iv_item_image.setImageResource(R.drawable.burger);
            tv_detail.setText("Chicken Burger with salad & Tomato Ketchup");}
        if (clickPosition.equals("6")){iv_item_image.setImageResource(R.drawable.juice_coffee);
            tv_detail.setText("Mango Juice with Butter & Breds");}
        if (clickPosition.equals("7")){iv_item_image.setImageResource(R.drawable.halwa_poori);
            tv_detail.setText("Halwa puri & Cholly with Achar");}
        if (clickPosition.equals("8")){iv_item_image.setImageResource(R.drawable.patees_burgur);
            tv_detail.setText("Breakfast Tart of Beef");}

        selectedITemList = new ArrayList<>();

    }

    private void increaseQuantity()
    {

        iv_item_quantity_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String totalPriceFromTV = tv_total_price.getText().toString();
                String tPriceFromTV =  totalPriceFromTV.split("\\.")[1];
                int priceFromTV = Integer.valueOf(tPriceFromTV);
                int totalPrice = priceFromTV+unitPrice;
                tv_total_price.setText("Rs."+totalPrice);
                int quanityText  = Integer.valueOf(tv_quantitiy.getText().toString());
                int quantities = quanityText+1;
                tv_quantitiy.setText(String.valueOf(quantities));

            }
        });

    }

    private void decreasingQuantity()
    {

        iv_item_quantity_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quanityText  = Integer.valueOf(tv_quantitiy.getText().toString());
                if (quanityText>1) {
                    int quantities = quanityText - 1;
                    tv_quantitiy.setText(String.valueOf(quantities));
                    String totalPriceFromTV = tv_total_price.getText().toString();
                    String tPriceFromTV = totalPriceFromTV.split("\\.")[1];
                    int priceFromTV = Integer.valueOf(tPriceFromTV);
                    int totalPrice = priceFromTV - unitPrice;
                    tv_total_price.setText("Rs." + totalPrice);
                }
            }
        });

    }

    private void orderConfirmation(){
        rl_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(FoodSingleItem.this);
                alert.setTitle("Order Confirmation");
                alert.setMessage("Press Yes to confirm your order or press No to cancel or Press View Oreders to view your order listing" +
                        "No to cancel");
               /* alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        *//*HashMap<String, String> itmeList = new HashMap<>();
                        String itemName = tv_item_name.getText().toString();
                        String itemUnitPrice = tv_unit_price.getText().toString();
                        String itemQuantity = tv_quantitiy.getText().toString();
                        String itemDetail = tv_detail.getText().toString();
                        String itemTotalPrice = tv_total_price.getText().toString();
                        String itemImagePosition = clickPosition;
                        //putting to hashmap
                        itmeList.put("itemName", itemName);
                        itmeList.put("itemUnitPrice", itemUnitPrice);
                        itmeList.put("itemQuantity", itemQuantity);
                        itmeList.put("itemDetail", itemDetail);
                        itmeList.put("itemTotalPrice", itemTotalPrice);
                        itmeList.put("itemImagePosition", itemImagePosition);
                        //adding to array
                        selectedITemList.add(itmeList);

                        final Dialog customeDialog = new Dialog(FoodSingleItem.this);
                        customeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        customeDialog.setContentView(R.layout.order_confirmation_dialog);
                        RelativeLayout dialog_rl_ok = (RelativeLayout) customeDialog.findViewById(R.id.dialog_rl_ok);
                        dialog_rl_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customeDialog.dismiss();

                                Intent intent = new Intent(FoodSingleItem.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                        customeDialog.setCancelable(false);
                        customeDialog.show();
*//*

                        HashMap<String, String> itmeList = new HashMap<>();
                        String itemName = tv_item_name.getText().toString();
                        String itemUnitPrice = tv_unit_price.getText().toString();
                        String itemQuantity = tv_quantitiy.getText().toString();
                        String itemDetail = tv_detail.getText().toString();
                        String itemTotalPrice = tv_total_price.getText().toString();
                        String itemImagePosition = clickPosition;
                        //putting to hashmap
                        itmeList.put("itemName", itemName);
                        itmeList.put("itemUnitPrice", itemUnitPrice);
                        itmeList.put("itemQuantity", itemQuantity);
                        itmeList.put("itemDetail", itemDetail);
                        itmeList.put("itemTotalPrice", itemTotalPrice);
                        itmeList.put("itemImagePosition", itemImagePosition);
                        //adding to array
                        selectedITemList.add(itmeList);
                        Intent i = new Intent(FoodSingleItem.this, FoodSelectedOrderDetailActivity.class);
                        i.putExtra("mylist", selectedITemList);

                        //finish();
                    }
                });*/

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.setNeutralButton("View Oreders", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        HashMap<String, String> itmeList = new HashMap<>();
                        String itemName = tv_item_name.getText().toString();
                        String itemUnitPrice = tv_unit_price.getText().toString();
                        String itemQuantity = tv_quantitiy.getText().toString();
                        String itemDetail = tv_detail.getText().toString();
                        String itemTotalPrice = tv_total_price.getText().toString();
                        String itemImagePosition = clickPosition;
                        //putting to hashmap
                        itmeList.put("itemName", itemName);
                        itmeList.put("itemUnitPrice", itemUnitPrice);
                        itmeList.put("itemQuantity", itemQuantity);
                        itmeList.put("itemDetail", itemDetail);
                        itmeList.put("itemTotalPrice", itemTotalPrice);
                        itmeList.put("itemImagePosition", itemImagePosition);
                        //adding to array
                        selectedITemList.add(itmeList);
                        Intent i = new Intent(FoodSingleItem.this, FoodSelectedOrderDetailActivity.class);
                        i.putExtra("mylist", selectedITemList);
                        startActivityForResult(i, 12);

                    }
                });
                alert.show();
            }
        });
    }

    private void addMoreItems()
    {
        rl_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> itmeList = new HashMap<>();
                String itemName = tv_item_name.getText().toString();
                String itemUnitPrice = tv_unit_price.getText().toString();
                String itemQuantity = tv_quantitiy.getText().toString();
                String itemDetail = tv_detail.getText().toString();
                String itemTotalPrice = tv_total_price.getText().toString();
                String itemImagePosition = clickPosition;
                //putting to hashmap
                itmeList.put("itemName", itemName);
                itmeList.put("itemUnitPrice", itemUnitPrice);
                itmeList.put("itemQuantity", itemQuantity);
                itmeList.put("itemDetail", itemDetail);
                itmeList.put("itemTotalPrice", itemTotalPrice);
                itmeList.put("itemImagePosition", itemImagePosition);
                //adding to array
                selectedITemList.add(itmeList);
                Intent i = new Intent(FoodSingleItem.this, AddMoreBreakfast.class);
                i.putExtra("mylist", selectedITemList);
                startActivityForResult(i, 12);


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12) {

            if (data != null) {
                String empty =  data.getStringExtra("empty");
                if (empty.equals("no")) {
                    clickPosition = data.getStringExtra("ImagePosition");
                    String itemName = data.getStringExtra("itemName");
                    String itemPrice = data.getStringExtra("itemPrice");

                    tv_item_name.setText(itemName);
                    tv_unit_price.setText(itemPrice);
                    tv_total_price.setText(itemPrice);
                    tv_quantitiy.setText("1");

                    unitPriceOfItem = itemPrice.split("\\.")[1];
                    Log.e("TAG", "ADFASDFASD " + unitPriceOfItem);
                    unitPrice = Integer.valueOf(unitPriceOfItem);

                    if (clickPosition.equals("1")) {
                        iv_item_image.setImageResource(R.drawable.bissell_breakfast);
                        tv_detail.setText("Assortment of Fried Egg, Sandwich, Lobia, Sausages & Green Salad");
                    }
                    if (clickPosition.equals("2")) {
                        iv_item_image.setImageResource(R.drawable.samosas);
                        tv_detail.setText("3 samosas with tea, chattni & masala");
                    }
                    if (clickPosition.equals("3")) {
                        iv_item_image.setImageResource(R.drawable.coffee);
                        tv_detail.setText("Coffee Biscuits & Activia");
                    }
                    if (clickPosition.equals("4")) {
                        iv_item_image.setImageResource(R.drawable.anda_paratha);
                        tv_detail.setText("Fried Ege Butter & Paratha");
                    }
                    if (clickPosition.equals("5")) {
                        iv_item_image.setImageResource(R.drawable.burger);
                        tv_detail.setText("Chicken Burger with salad & Tomato Ketchup");
                    }
                    if (clickPosition.equals("6")) {
                        iv_item_image.setImageResource(R.drawable.juice_coffee);
                        tv_detail.setText("Mango Juice with Butter & Breds");
                    }
                    if (clickPosition.equals("7")) {
                        iv_item_image.setImageResource(R.drawable.halwa_poori);
                        tv_detail.setText("Halwa puri & Cholly with Achar");
                    }
                    if (clickPosition.equals("8")) {
                        iv_item_image.setImageResource(R.drawable.patees_burgur);
                        tv_detail.setText("Breakfast Tart of Beef");
                    }

                }
                else if (empty.equals("yes")){
                    Log.e("TAg", "the size of array list is: " + selectedITemList.size());
                    selectedITemList.remove(selectedITemList.size()-1);
                }
            }
        }

    }
}

