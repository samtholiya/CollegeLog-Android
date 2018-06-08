package com.trak.sam.collegelog.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.trak.sam.collegelog.callback.UserCallback;
import com.trak.sam.collegelog.config.Config;
import com.trak.sam.collegelog.helper.UtilHelper;
import com.trak.sam.collegelog.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class UserService {
    private static Gson gson = UtilHelper.getGsonInstance();
    private static AsyncHttpClient client = UtilHelper.getHttpClient();
    private static Context context;

    public static void setContext(Context context) {
        UserService.context = context;
    }

    public static void login(String username, String password, final UserCallback callback) {
        client.setBasicAuth(username, password);
        client.get(UtilHelper.getAbsoluteUrl(Config.USER_ENDPOINT), new UserRequestHandler(new UserCallbackHandler(callback, password)));
    }

    public static void register(User user, UserCallback userCallback) {
        client.setBasicAuth(Config.ANONYMOUS_USER, Config.ANONYMOUS_PASSWORD);
        try {
            StringEntity stringEntity = new StringEntity(gson.toJson(user));
            stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(null, UtilHelper.getAbsoluteUrl(Config.USER_ENDPOINT), stringEntity, "application/json", new UserRequestHandler(userCallback));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public static void setCurrentUser(String username, String password) {
        SharedPreferences sharedPreferences = UtilHelper.getSharedPreferencesForUser(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    public static User getCurrentUser() {
        SharedPreferences sharedPreferences = UtilHelper.getSharedPreferencesForUser(context);
        User user = new User();
        user.userName = sharedPreferences.getString("username", Config.ANONYMOUS_USER);
        user.password = sharedPreferences.getString("password", Config.ANONYMOUS_PASSWORD);
        return user;
    }

    private static class UserCallbackHandler implements UserCallback {

        private UserCallback userCallback;
        private String password;

        public UserCallbackHandler(UserCallback userCallback, String password) {
            this.userCallback = userCallback;
            this.password = password;
        }

        @Override
        public void onUserReceived(User user) {
            setCurrentUser(user.userName, this.password);
            this.userCallback.onUserReceived(user);
        }

        @Override
        public void onUsersReceived(User[] users) {
            this.onUsersReceived(users);
        }

        @Override
        public void onFailed(Exception e) {
            this.userCallback.onFailed(e);
        }
    }

    private static class UserRequestHandler extends JsonHttpResponseHandler {

        private final UserCallback userCallback;
        private Gson gson;

        public UserRequestHandler(UserCallback userCallback) {
            this.userCallback = userCallback;
            gson = UtilHelper.getGsonInstance();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            User user = gson.fromJson(response.toString(), User.class);
            this.userCallback.onUserReceived(user);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            User[] users = gson.fromJson(response.toString(), User[].class);
            this.userCallback.onUsersReceived(users);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            this.userCallback.onFailed(new Exception("Status code %s"));
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            this.userCallback.onFailed(new Exception("Status code %s"));
        }

    }

}
