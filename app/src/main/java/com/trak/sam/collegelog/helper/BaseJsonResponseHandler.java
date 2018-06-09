package com.trak.sam.collegelog.helper;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.trak.sam.collegelog.callback.BaseHttpCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class BaseJsonResponseHandler<T> extends JsonHttpResponseHandler {

    private BaseHttpCallback<T> mCallback;
    private static Gson gson = UtilHelper.getGsonInstance();
    private Class<T> mTypeClass;
    private Class<T[]> mTypeClasses;

    public BaseJsonResponseHandler(BaseHttpCallback<T> callback, Class<T> typeClass, Class<T[]> typeClasses) {
        this.mCallback = callback;
        this.mTypeClass = typeClass;
        this.mTypeClasses = typeClasses;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        T item = gson.fromJson(response.toString(), mTypeClass);
        mCallback.onItemReceived(item);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        T[] schools = gson.fromJson(response.toString(), mTypeClasses);
        mCallback.onItemsReceived(schools);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        mCallback.onFailed(new Exception(throwable));
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        mCallback.onFailed(new Exception(throwable));
    }

}
