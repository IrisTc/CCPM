package com.iris.ccpm.model;

import android.app.Application;
import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;

public class GlobalData extends Application {
    private static GlobalData instance;

    private String token;
    private String username;
    private String password;
    private String avatarUrl;
    private String nickName;
    private String realName;
    private String position;
    private String phoneNum;
    private String synopsis;
    private Integer uid;
    private String now_project_id;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Fresco.initialize(this);
    }

    public static Context getGlobalData() {
        return instance;
    }

    public static void save_account(JSONObject data, Context context) {
        final GlobalData app = (GlobalData) context.getApplicationContext();
        app.setUsername(data.getString("username"));
        app.setAvatarUrl(data.getString("avatarUrl"));
        app.setNickName(data.getString("nickName"));
        app.setRealName(data.getString("realName"));
        app.setPhoneNum(data.getString("phoneNum"));
        app.setSynopsis(data.getString("synopsis"));
        app.setPosition(data.getString("position"));
        app.setUid(data.getInteger("account_uid"));
    }

    public String getNow_project_id() {
        return now_project_id;
    }

    public void setNow_project_id(String now_project_id) {
        this.now_project_id = now_project_id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getName() {
        if (this.realName == null) {
            return this.nickName;
        }
        return this.nickName + "(" + this.realName + ")";
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
