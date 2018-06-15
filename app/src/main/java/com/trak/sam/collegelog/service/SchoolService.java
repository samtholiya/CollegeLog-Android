package com.trak.sam.collegelog.service;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.trak.sam.collegelog.callback.BaseHttpCallback;
import com.trak.sam.collegelog.config.Config;
import com.trak.sam.collegelog.helper.BaseJsonResponseHandler;
import com.trak.sam.collegelog.helper.UtilHelper;
import com.trak.sam.collegelog.model.School;
import com.trak.sam.collegelog.model.User;

public class SchoolService {
    private static AsyncHttpClient client = UtilHelper.getHttpClient();
    private static Context context;

    public static void setContext(Context context) {
        SchoolService.context = context;
    }

    public static void getSchools(BaseHttpCallback<School> callback) {
        User user = UserService.getCurrentUser();
        client.setBasicAuth(user.userName, user.password);
        BaseJsonResponseHandler<School> httpCallback = new BaseJsonResponseHandler<School>(callback, School.class, School[].class);
        client.get(UtilHelper.getAbsoluteUrl(Config.SCHOOL_ENDPOINT), httpCallback);
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
