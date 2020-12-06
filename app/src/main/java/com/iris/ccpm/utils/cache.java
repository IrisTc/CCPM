package com.iris.ccpm.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class cache {
    /*
     * 获取缓存的userInfo
     */
    public static Map<String, Object> getCachedUserInfo(Context context) throws JSONException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);
        Boolean autofix = sharedPreferences.getBoolean("autofix", true);
        Boolean autologin = sharedPreferences.getBoolean("autologin", true);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", username);
        map.put("password", password);
        map.put("autofix", autofix);
        map.put("autologin", autologin);
        return map;
    }

    /*
     * 获取缓存的token
     */
    public static String getCachedToken(Context context){
        return context.getSharedPreferences("cache", context.MODE_PRIVATE).getString("KEY_TOKEN", null);
    }

    /*
     * 缓存token
     */
    public static void cacheToken(Context context, String token){

        SharedPreferences.Editor e = context.getSharedPreferences("cache", context.MODE_PRIVATE).edit();
        e.putString("KEY_TOKEN", token);
        e.commit();
    }
}
