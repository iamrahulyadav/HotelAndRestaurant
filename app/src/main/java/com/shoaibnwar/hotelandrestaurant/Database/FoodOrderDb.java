package com.shoaibnwar.hotelandrestaurant.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shoaibnwar.hotelandrestaurant.DataseHelper.OrderFoodDbHelper;

import java.util.ArrayList;

/**
 * Created by gold on 7/13/2018.
 */

public class FoodOrderDb extends SQLiteOpenHelper {
    Context context;

    public static final String DATABASE_NAME = "foodorder.db";
    private static final int DatabaseVersion = 1;
    public static final String NAME_OF_TABLE = "foodordertable";
    public static final String Col_1 = "id";
    public static final String Col_2 = "trackingid";
    public static final String Col_3 = "itemname";
    public static final String Col_4 = "uniprice";
    public static final String Col_5 = "quanities";
    public static final String Col_6 = "totalprice";
    public static final String Col_7 = "detail";
    public static final String Col_8 = "ordersumprice";
    public static final String Col_9 = "itemimageposition";
    public static final String Col_10 = "roomnumber";
    public static final String Col_11 = "assignstatus";
    public static final String Col_12 = "currentTime";
    public static final String Col_13 = "currentDate";




    String CREATE_TABLE_CALL = "CREATE TABLE " + NAME_OF_TABLE
            + "("
            + Col_1 + " integer PRIMARY KEY AUTOINCREMENT,"
            + Col_2 + " TEXT, "
            + Col_3 + " TEXT, "
            + Col_4 + " TEXT, "
            + Col_5 + " TEXT, "
            + Col_6 + " TEXT, "
            + Col_7 + " TEXT, "
            + Col_8 + " TEXT, "
            + Col_9 + " TEXT, "
            + Col_10 + " TEXT, "
            + Col_11 + " TEXT, "
            + Col_12 + " TEXT, "
            + Col_13 + " TEXT "
            + ")";


    public FoodOrderDb(Context context) {
        super(context, DATABASE_NAME, null, DatabaseVersion);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_CALL);
        //db.execSQL(Create_Virtual_Table_Call);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME_OF_TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + Create_Virtual_Table_Call);

    }

    //inserting post in databse
    public long insertDatatoDb(OrderFoodDbHelper helper) {
        long result;


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(Col_1, post.getId());
        values.put(Col_2, helper.getTrackinId());
        values.put(Col_3, helper.getItemname());
        values.put(Col_4, helper.getUnitPrice());
        values.put(Col_5, helper.getQuantities());
        values.put(Col_6, helper.getTotalPrice());
        values.put(Col_7, helper.getDetail());
        values.put(Col_8, helper.getOrderSumPrice());
        values.put(Col_9, helper.getItemImagePosition());
        values.put(Col_10, helper.getRoomNumber());
        values.put(Col_11, helper.getAssignStatus());
        values.put(Col_12, helper.getCurrentTime());
        values.put(Col_13, helper.getCurrentDate());



        //inserting valuse into table columns
        result = db.insert(NAME_OF_TABLE, null, values);
        db.close();
        return result;

    }

    /* fetching records from Database Table*/
    public ArrayList<OrderFoodDbHelper> getAllAsignments() {
        String query = "SELECT * FROM " + NAME_OF_TABLE;

        ArrayList<OrderFoodDbHelper> addingToList = new ArrayList<OrderFoodDbHelper>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                OrderFoodDbHelper myHelper = new OrderFoodDbHelper();
                String id = c.getString(c.getColumnIndex(Col_1));
                String trackingId = c.getString(c.getColumnIndex(Col_2));
                String itemName = c.getString(c.getColumnIndex(Col_3));
                String unitPrice = c.getString(c.getColumnIndex(Col_4));
                String quantities = c.getString(c.getColumnIndex(Col_5));
                String totalPrice = c.getString(c.getColumnIndex(Col_6));
                String detail = c.getString(c.getColumnIndex(Col_7));
                String orderSumPrice = c.getString(c.getColumnIndex(Col_8));
                String itemImagePosition = c.getString(c.getColumnIndex(Col_9));
                String roomNumber = c.getString(c.getColumnIndex(Col_10));
                String assignStatus = c.getString(c.getColumnIndex(Col_11));
                String currentTime = c.getString(c.getColumnIndex(Col_12));
                String currentDate = c.getString(c.getColumnIndex(Col_13));

                myHelper.setId(id);
                myHelper.setTrackinId(trackingId);
                myHelper.setItemname(itemName);
                myHelper.setUnitPrice(unitPrice);
                myHelper.setQuantities(quantities);
                myHelper.setTotalPrice(totalPrice);
                myHelper.setDetail(detail);
                myHelper.setOrderSumPrice(orderSumPrice);
                myHelper.setItemImagePosition(itemImagePosition);
                myHelper.setRoomNumber(roomNumber);
                myHelper.setAssignStatus(assignStatus);
                myHelper.setCurrentTime(currentTime);
                myHelper.setCurrentDate(currentDate);

                //adding data to array list
                addingToList.add(myHelper);

            }
        }

        db.close();
        return addingToList;

    }


    //fetch single record
    public ArrayList<OrderFoodDbHelper> getOrderByTrackingId(String mTrackingId) {
        String query = "SELECT * FROM " + NAME_OF_TABLE +" WHERE trackingid="+mTrackingId;
        Log.e("TAg", "the id is: " + query);
        ArrayList<OrderFoodDbHelper> addingToList = new ArrayList<OrderFoodDbHelper>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                OrderFoodDbHelper myHelper = new OrderFoodDbHelper();
                String id = c.getString(c.getColumnIndex(Col_1));
                String trackingId = c.getString(c.getColumnIndex(Col_2));
                String itemName = c.getString(c.getColumnIndex(Col_3));
                String unitPrice = c.getString(c.getColumnIndex(Col_4));
                String quantities = c.getString(c.getColumnIndex(Col_5));
                String totalPrice = c.getString(c.getColumnIndex(Col_6));
                String detail = c.getString(c.getColumnIndex(Col_7));
                String orderSumPrice = c.getString(c.getColumnIndex(Col_8));
                String itemImagePosition = c.getString(c.getColumnIndex(Col_9));
                String roomNumber = c.getString(c.getColumnIndex(Col_10));
                String assignStatus = c.getString(c.getColumnIndex(Col_11));
                String currentTime = c.getString(c.getColumnIndex(Col_12));
                String currentDate = c.getString(c.getColumnIndex(Col_13));

                myHelper.setId(id);
                myHelper.setTrackinId(trackingId);
                myHelper.setItemname(itemName);
                myHelper.setUnitPrice(unitPrice);
                myHelper.setQuantities(quantities);
                myHelper.setTotalPrice(totalPrice);
                myHelper.setDetail(detail);
                myHelper.setOrderSumPrice(orderSumPrice);
                myHelper.setItemImagePosition(itemImagePosition);
                myHelper.setRoomNumber(roomNumber);
                myHelper.setAssignStatus(assignStatus);
                myHelper.setCurrentTime(currentTime);
                myHelper.setCurrentDate(currentDate);

                //adding data to array list
                addingToList.add(myHelper);

            }
        }

        db.close();
        return addingToList;

    }

    //dateWise items

    //fetch single record
    public ArrayList<OrderFoodDbHelper> getAllCurrentDateRecord(String date) {
        String query = "SELECT * FROM " + NAME_OF_TABLE +" WHERE "+Col_7+"="+"'"+date+"'";
        Log.e("TAg", "the id is: " + query);
        ArrayList<OrderFoodDbHelper> addingToList = new ArrayList<OrderFoodDbHelper>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                OrderFoodDbHelper myHelper = new OrderFoodDbHelper();
                String id = c.getString(c.getColumnIndex(Col_1));
                String trackingId = c.getString(c.getColumnIndex(Col_2));
                String itemName = c.getString(c.getColumnIndex(Col_3));
                String unitPrice = c.getString(c.getColumnIndex(Col_4));
                String quantities = c.getString(c.getColumnIndex(Col_5));
                String totalPrice = c.getString(c.getColumnIndex(Col_6));
                String detail = c.getString(c.getColumnIndex(Col_7));
                String orderSumPrice = c.getString(c.getColumnIndex(Col_8));
                String itemImagePosition = c.getString(c.getColumnIndex(Col_9));
                String roomNumber = c.getString(c.getColumnIndex(Col_10));
                String assignStatus = c.getString(c.getColumnIndex(Col_11));
                String currentTime = c.getString(c.getColumnIndex(Col_12));
                String currentDate = c.getString(c.getColumnIndex(Col_13));

                myHelper.setId(id);
                myHelper.setTrackinId(trackingId);
                myHelper.setItemname(itemName);
                myHelper.setUnitPrice(unitPrice);
                myHelper.setQuantities(quantities);
                myHelper.setTotalPrice(totalPrice);
                myHelper.setDetail(detail);
                myHelper.setOrderSumPrice(orderSumPrice);
                myHelper.setItemImagePosition(itemImagePosition);
                myHelper.setRoomNumber(roomNumber);
                myHelper.setAssignStatus(assignStatus);
                myHelper.setCurrentTime(currentTime);
                myHelper.setCurrentDate(currentDate);

                //adding data to array list
                addingToList.add(myHelper);

            }
        }

        db.close();
        return addingToList;

    }

    //Updatating post
    public boolean updateTable(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_11,status);
        db.update(NAME_OF_TABLE, contentValues, "id = ?", new String[]{Integer.toString(id)});
        db.close();
        return true;
    }

    //deleting post
    public boolean deleteFromTable(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NAME_OF_TABLE, Col_1 + "=" + rowId, null);
        db.close();

        return true;

    }

    //

    public int getCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + NAME_OF_TABLE;
        return db.rawQuery(query, null).getCount();
    }

}


