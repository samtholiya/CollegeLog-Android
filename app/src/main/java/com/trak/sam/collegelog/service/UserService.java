package com.trak.sam.collegelog.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.trak.sam.collegelog.callback.BaseHttpCallback;
import com.trak.sam.collegelog.config.Config;
import com.trak.sam.collegelog.helper.BaseJsonResponseHandler;
import com.trak.sam.collegelog.helper.UtilHelper;
import com.trak.sam.collegelog.model.DeleteResult;
import com.trak.sam.collegelog.model.Role;
import com.trak.sam.collegelog.model.User;

import java.io.UnsupportedEncodingException;

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

    public static void login(String username, String password, BaseHttpCallback<User> callback) {
        client.setBasicAuth(username, password);
        BaseHttpCallback<User> httpResponseHandler = new UserCallbackHandler(callback, password);
        BaseJsonResponseHandler<User> jsonResponseHandler = new BaseJsonResponseHandler<>(httpResponseHandler, User.class, User[].class);
        client.get(UtilHelper.getAbsoluteUrl(Config.USER_ENDPOINT), jsonResponseHandler);
    }

    public static void deleteUserWithId(long id, BaseHttpCallback<DeleteResult> callback) {
        User tempUser = getCurrentUser();
        client.setBasicAuth(tempUser.userName, tempUser.password);
        BaseJsonResponseHandler<DeleteResult> jsonResponseHandler = new BaseJsonResponseHandler<>(callback, DeleteResult.class, DeleteResult[].class);
        client.delete(UtilHelper.getAbsoluteUrl(Config.USER_ENDPOINT + "/" + String.valueOf(id)), jsonResponseHandler);
    }


    public static void getUsersOfRole(String role, long offset, long limit, BaseHttpCallback<User> callback) {
        User tempUser = getCurrentUser();
        client.setBasicAuth(tempUser.userName, tempUser.password);
        BaseJsonResponseHandler<User> jsonResponseHandler = new BaseJsonResponseHandler<>(callback, User.class, User[].class);
        //Used stringBuilder for better performance
        StringBuilder stringBuilder = new StringBuilder(Config.USER_ENDPOINT);
        String endpoint = stringBuilder.append("/")
                .append(Config.LIST_ENDPOINT)
                .append("?")
                .append(Config.UserFilter.ROLE_TYPE)
                .append("=")
                .append(role)
                .append("&")
                .append(Config.UserFilter.LIMIT)
                .append("=")
                .append(limit)
                .append("&")
                .append(Config.UserFilter.OFFSET)
                .append("=")
                .append(offset)
                .toString();
        client.get(UtilHelper.getAbsoluteUrl(endpoint), jsonResponseHandler);
    }

    public static void register(User user, BaseHttpCallback<User> userCallback) {
        User tempUser = getCurrentUser();
        client.setBasicAuth(tempUser.userName, tempUser.password);
        try {
            StringEntity stringEntity = new StringEntity(gson.toJson(user));
            stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            BaseJsonResponseHandler<User> jsonResponseHandler = new BaseJsonResponseHandler<>(userCallback, User.class, User[].class);
            client.post(null, UtilHelper.getAbsoluteUrl(Config.USER_ENDPOINT), stringEntity, "application/json", jsonResponseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public static void setCurrentUser(String username, String password, String role) {
        SharedPreferences sharedPreferences = UtilHelper.getSharedPreferencesForUser(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("role", role);
        editor.apply();
    }

    public static User getCurrentUser() {
        SharedPreferences sharedPreferences = UtilHelper.getSharedPreferencesForUser(context);
        User user = new User();
        user.userName = sharedPreferences.getString("username", Config.ANONYMOUS_USER);
        user.password = sharedPreferences.getString("password", Config.ANONYMOUS_PASSWORD);
        user.role = new Role();
        user.role.name = sharedPreferences.getString("role", Config.ANONYMOUS_ROLE);
        return user;
    }

    private static class UserCallbackHandler implements BaseHttpCallback<User> {

        private BaseHttpCallback<User> userCallback;
        private String password;

        public UserCallbackHandler(BaseHttpCallback<User> userCallback, String password) {
            this.userCallback = userCallback;
            this.password = password;
        }

        @Override
        public void onItemReceived(User item) {
            setCurrentUser(item.userName, this.password, item.role.name);
            this.userCallback.onItemReceived(item);
        }

        @Override
        public void onItemsReceived(User[] items) {
            this.userCallback.onItemsReceived(items);
        }

        @Override
        public void onFailed(Exception e) {
            this.userCallback.onFailed(e);
        }
    }

}
