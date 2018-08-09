package com.shoaibnwar.hotelandrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gold on 7/11/2018.
 */

public class ProductModel
{
    private static ProductModel instance = null;

    public boolean success;
    public String message;

    public static ProductModel getInstance()
    {
        if(instance == null)
        {
            instance = new ProductModel();
        }
        return instance;
    }

    public void setModel(ProductModel obj)
    {
        instance = obj;
    }

    public class Table implements Serializable
    {
        @SerializedName("Vid")
        public String v_id;

        @SerializedName("Vtype")
        public String v_type;

        @SerializedName("V_ThumbImg")
        public String img;

        @SerializedName("V_Model")
        public String model;

        @SerializedName("V_City")
        public String city;

        @SerializedName("V_Title")
        public String title;

        @SerializedName("V_Lat")
        public String lat;

        @SerializedName("V_Long")
        public String lng;

        @SerializedName("Distance")
        public String distance;

        @SerializedName("Price")
        public String price;

        @SerializedName("RequestId")
        public String req_id;

        @SerializedName("DriverMobile")
        public String driver_mobile;

        @SerializedName("DriverName")
        public String driver_name;

        @SerializedName("VRegNo")
        public String v_reg_no;

        @SerializedName("CustomerRating")
        public String customer_rating;

        @SerializedName("DriverImage")
        public String driver_img;

        @SerializedName("AssetImage")
        public String asset_img;

        @SerializedName("LicenseNo")
        public String license_no;

        @SerializedName("V_Model")
        public String v_model;
    }
}
