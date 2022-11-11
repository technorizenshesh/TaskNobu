package com.tasknobu.retrofit;

import android.app.Activity;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constant {

//  public static final String BASE_URL = "https://myspotbh.com/nobutask/webservice/";https://technorizen.com/nobutask/
    public static final String BASE_URL = "https://nobu.es/tasknobu/webservice/";
    public final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String IS_USER_LOGGED_IN = "IsUserLoggedIn";
    public static final String USER_ID = "userID";
    public static final int LOCATION_REQUEST = 1000;
    public static final int GPS_REQUEST = 1001;

    public static void showToast(Activity context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
