package com.iris.ccpm.utils;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public abstract class NetCallBack extends AsyncHttpResponseHandler {

    @Override
    public void onStart() {
        System.out.println("start");
        super.onStart();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        System.out.println("error: " + error);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        String string = new String(responseBody);
        String ccpmcode = headers[0].getValue();
        JSONObject response = JSONObject.parseObject(string);

        if (ccpmcode == "200") {
            onMySuccess(response.getJSONObject("data"));
        } else {
            onMyFailure(response.getString("msg"));
        }
    }


    public abstract void onMySuccess(JSONObject result);


    public abstract void onMyFailure(String error);

}