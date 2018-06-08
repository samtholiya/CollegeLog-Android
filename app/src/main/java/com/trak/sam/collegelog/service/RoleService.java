package com.trak.sam.collegelog.service;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.trak.sam.collegelog.callback.RoleCallback;
import com.trak.sam.collegelog.config.Config;
import com.trak.sam.collegelog.helper.UtilHelper;
import com.trak.sam.collegelog.model.Role;
import com.trak.sam.collegelog.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RoleService {
    private static Gson gson = UtilHelper.getGsonInstance();
    private static AsyncHttpClient client = UtilHelper.getHttpClient();
    private static Context context;

    public static void setContext(Context context) {
        RoleService.context = context;
    }

    public static void getRoles(RoleCallback callback) {
        User user = UserService.getCurrentUser();
        client.setBasicAuth(user.userName, user.password);
        client.get(UtilHelper.getAbsoluteUrl(Config.ROLE_ENDPOINT), new RoleService.RoleRequestHandler(callback));
    }

    private static class RoleRequestHandler extends JsonHttpResponseHandler {

        private final RoleCallback callback;

        public RoleRequestHandler(RoleCallback callback){
            this.callback = callback;
        }
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Role role = gson.fromJson(response.toString(), Role.class);
            callback.onRoleReceived(role);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Role[] roles = gson.fromJson(response.toString(), Role[].class);
            callback.onRolesReceived(roles);
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
