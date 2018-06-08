package com.trak.sam.collegelog.service;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.trak.sam.collegelog.callback.SchoolCallback;
import com.trak.sam.collegelog.config.Config;
import com.trak.sam.collegelog.helper.UtilHelper;
import com.trak.sam.collegelog.model.School;
import com.trak.sam.collegelog.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SchoolService {
    private static Gson gson = UtilHelper.getGsonInstance();
    private static AsyncHttpClient client = UtilHelper.getHttpClient();
    private static Context context;

    public static void setContext(Context context) {
        SchoolService.context = context;
    }

    public static void getSchools(SchoolCallback callback) {
        User user = UserService.getCurrentUser();
        client.setBasicAuth(user.userName, user.password);
        client.get(UtilHelper.getAbsoluteUrl(Config.SCHOOL_ENDPOINT), new SchoolService.SchoolRequestHandler(callback));
    }

    private static class SchoolRequestHandler extends JsonHttpResponseHandler {

        private final SchoolCallback callback;

        public SchoolRequestHandler(SchoolCallback callback){
            this.callback = callback;
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            School school = gson.fromJson(response.toString(), School.class);
            callback.onSchoolReceived(school);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            School[] schools = gson.fromJson(response.toString(), School[].class);
            callback.onSchoolsReceived(schools);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            callback.onFailed(new Exception(throwable));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            callback.onFailed(new Exception(throwable));
        }
    }
}
