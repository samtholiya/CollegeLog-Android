package com.trak.sam.collegelog.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.trak.sam.collegelog.config.Config;

import static com.trak.sam.collegelog.config.Config.BASE_URL;

public class UtilHelper {
    public static String getAbsoluteUrl(String absoluteUri) {
        return BASE_URL + absoluteUri;
    }

    private static Gson gson;
    private static AsyncHttpClient asyncHttpClient;

    public static Gson getGsonInstance() {
        if (gson == null)
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        return gson;
    }

    public static SharedPreferences getSharedPreferencesForUser(Context context) {
        return context.getSharedPreferences(Config.USER_DATA_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static AsyncHttpClient getHttpClient() {
        if (asyncHttpClient == null)
            asyncHttpClient = new AsyncHttpClient();
        return asyncHttpClient;
    }
}
