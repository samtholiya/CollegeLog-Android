package com.trak.sam.collegelog.service;

import android.content.Context;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.trak.sam.collegelog.callback.BaseHttpCallback;
import com.trak.sam.collegelog.config.Config;
import com.trak.sam.collegelog.helper.BaseJsonResponseHandler;
import com.trak.sam.collegelog.helper.UtilHelper;
import com.trak.sam.collegelog.model.Role;
import com.trak.sam.collegelog.model.User;

public class RoleService {
    private static Gson gson = UtilHelper.getGsonInstance();
    private static AsyncHttpClient client = UtilHelper.getHttpClient();
    private static Context context;

    public static void setContext(Context context) {
        RoleService.context = context;
    }

    public static void getRoles(BaseHttpCallback<Role> callback) {
        User user = UserService.getCurrentUser();
        client.setBasicAuth(user.userName, user.password);
        client.get(UtilHelper.getAbsoluteUrl(Config.ROLE_ENDPOINT), new BaseJsonResponseHandler<Role>(callback, Role.class, Role[].class));
    }

}
