package com.iris.ccpm.utils;

import android.content.Context;

import com.iris.ccpm.model.GlobalData;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.entity.StringEntity;

import static com.iris.ccpm.model.GlobalData.getGlobalData;

public abstract class Request {
    private static final String BASE_URL = "https://find-hdu.com/";

    public static AsyncHttpClient client = new AsyncHttpClient();


    public static void clientGet(String url, NetCallBack cb) {
        GlobalData app = (GlobalData) getGlobalData();
        client.addHeader("token", app.getToken());
        client.get(BASE_URL + url, cb);
    }

    public static void clientPost(Context context, String url, StringEntity entity, NetCallBack cb) {
        GlobalData app = (GlobalData) context.getApplicationContext();
        client.addHeader("token", app.getToken());
        client.post(context, BASE_URL + url, entity, "application/json", cb);
    }

    public static void clientPut(Context context, String url, StringEntity entity, NetCallBack cb) {
        GlobalData app = (GlobalData) context.getApplicationContext();
        client.addHeader("token", app.getToken());
        client.put(context, BASE_URL + url, entity, "application/json", cb);
    }
}