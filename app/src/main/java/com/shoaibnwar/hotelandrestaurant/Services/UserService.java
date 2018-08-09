package com.shoaibnwar.hotelandrestaurant.Services;

import android.app.Activity;
import android.content.Context;

import com.shoaibnwar.hotelandrestaurant.Model.ProductModel;
import com.shoaibnwar.hotelandrestaurant.Utils.Urls;

import java.util.HashMap;

/**
 * Created by gold on 7/11/2018.
 */

public class UserService extends BaseService
{
    public UserService(Activity act) {
        super(act);
    }

    public UserService(Context context) {
        super((Activity) context);
    }

    public void signInServiceHit(String email, String password, String device, CallBack callBack, boolean progress) {
        String url = Urls.SIGN_IN;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Email", email);
        params.put("Password", password);
        params.put("Device", device);
        //params.put("Action", "UserLogin");

        this.post(url, params, callBack, ProductModel.getInstance(), progress);
    }

    public void signUpServiceHit(String email, String password, String device, CallBack callBack, boolean progress) {
        String url = Urls.SIGN_UP;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Email", email);
        params.put("Password", password);
        params.put("Device", device);

        this.post(url, params, callBack, ProductModel.getInstance(), progress);
    }

    public void getAssets(String lat, String lng, String radius, String type, CallBack callBack, boolean progress) {
        String url = Urls.GET_ASSETS;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Lat", lat);
        params.put("Long", lng);
        params.put("Radius", radius);
        params.put("AssetType", type);

        this.post(url, params, callBack, ProductModel.getInstance(), progress);

    }

    public void newRequest(String lat, String lng, String radius, String type, CallBack callBack, boolean progress) {
        String url = Urls.GET_ASSETS;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Lat", lat);
        params.put("Long", lng);
        params.put("Radius", radius);
        params.put("AssetType", type);

        this.post(url, params, callBack, ProductModel.getInstance(), progress);

    }
}

