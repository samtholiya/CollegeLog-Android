package com.trak.sam.collegelog.service;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.trak.sam.collegelog.callback.DepartmentCallback;
import com.trak.sam.collegelog.config.Config;
import com.trak.sam.collegelog.helper.UtilHelper;
import com.trak.sam.collegelog.model.Department;
import com.trak.sam.collegelog.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DepartmentService {
    private static Gson gson = UtilHelper.getGsonInstance();
    private static AsyncHttpClient client = UtilHelper.getHttpClient();
    private static Context context;

    public static void setContext(Context context) {
        DepartmentService.context = context;
    }

    public static void getDepartments(DepartmentCallback callback) {
        User user = UserService.getCurrentUser();
        client.setBasicAuth(user.userName, user.password);
        client.get(UtilHelper.getAbsoluteUrl(Config.DEPARTMENT_ENDPOINT), new DepartmentRequestHandler(callback));
    }

    private static class DepartmentRequestHandler extends JsonHttpResponseHandler {

        private final DepartmentCallback callback;

        public DepartmentRequestHandler(DepartmentCallback callback){
            this.callback = callback;
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Department department = gson.fromJson(response.toString(), Department.class);
            callback.onDepartmentReceived(department);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Department[] departments = gson.fromJson(response.toString(), Department[].class);
            callback.onDepartmentsReceived(departments);
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
