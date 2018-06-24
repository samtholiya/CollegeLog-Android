package com.trak.sam.collegelog.service;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.trak.sam.collegelog.callback.BaseHttpCallback;
import com.trak.sam.collegelog.config.Config;
import com.trak.sam.collegelog.helper.BaseJsonResponseHandler;
import com.trak.sam.collegelog.helper.UtilHelper;
import com.trak.sam.collegelog.model.DeleteResult;
import com.trak.sam.collegelog.model.School;
import com.trak.sam.collegelog.model.User;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class SchoolService {

    private static Gson gson = UtilHelper.getGsonInstance();
    private static AsyncHttpClient client = UtilHelper.getHttpClient();
    private static Context context;


    public static void setContext(Context context) {
        SchoolService.context = context;
    }

    public static void getSchools(BaseHttpCallback<School> callback) {
        User user = UserService.getCurrentUser();
        client.setBasicAuth(user.userName, user.password);
        BaseJsonResponseHandler<School> httpCallback = new BaseJsonResponseHandler<School>(callback, School.class, School[].class);
        client.get(UtilHelper.getAbsoluteUrl(Config.SCHOOL_ENDPOINT + "/" + Config.LIST_ENDPOINT), httpCallback);
    }

    public static void saveSchool(School school, BaseHttpCallback<School> callback) {
        User tempUser = UserService.getCurrentUser();
        client.setBasicAuth(tempUser.userName, tempUser.password);
        try {
            StringEntity stringEntity = new StringEntity(gson.toJson(school));
            stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            BaseJsonResponseHandler<School> jsonResponseHandler = new BaseJsonResponseHandler<>(callback, School.class, School[].class);
            client.post(null, UtilHelper.getAbsoluteUrl(Config.SCHOOL_ENDPOINT), stringEntity, "application/json", jsonResponseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public static void deleteSchool(long id, BaseHttpCallback<DeleteResult> callback) {
        User tempUser = UserService.getCurrentUser();
        client.setBasicAuth(tempUser.userName, tempUser.password);
        BaseJsonResponseHandler<DeleteResult> jsonResponseHandler = new BaseJsonResponseHandler<>(callback, DeleteResult.class, DeleteResult[].class);
        client.delete(UtilHelper.getAbsoluteUrl(Config.SCHOOL_ENDPOINT + "/" + String.valueOf(id)), jsonResponseHandler);
    }

    public static void getSchools(long offset, long limit, BaseHttpCallback<School> callback) {
        User user = UserService.getCurrentUser();
        client.setBasicAuth(user.userName, user.password);
        BaseJsonResponseHandler<School> httpCallback = new BaseJsonResponseHandler<School>(callback, School.class, School[].class);
        StringBuilder stringBuilder = new StringBuilder(Config.SCHOOL_ENDPOINT);
        String endpoint = stringBuilder.append("/")
                .append(Config.LIST_ENDPOINT)
                .append("?")
                .append(Config.UserFilter.LIMIT)
                .append("=")
                .append(limit)
                .append("&")
                .append(Config.UserFilter.OFFSET)
                .append("=")
                .append(offset)
                .toString();
        client.get(UtilHelper.getAbsoluteUrl(endpoint), httpCallback);
    }
}
