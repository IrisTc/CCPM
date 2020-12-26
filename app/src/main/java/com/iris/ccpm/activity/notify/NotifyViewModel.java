package com.iris.ccpm.activity.notify;

import android.widget.ListView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.R;
import com.iris.ccpm.adapter.InviteAdapter;
import com.iris.ccpm.adapter.NotifyAdapter;
import com.iris.ccpm.model.Apply;
import com.iris.ccpm.model.Invite;
import com.iris.ccpm.model.Notify;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

public class NotifyViewModel extends ViewModel {
    private MutableLiveData<List<Invite>> inviteList;
    private MutableLiveData<List<Apply>> applyList;
    private MutableLiveData<List<Notify>> notifyList;

    public NotifyViewModel() {
        inviteList = new MutableLiveData<>();
        applyList = new MutableLiveData<>();
        notifyList = new MutableLiveData<>();

        Request.clientGet("project/getInvite", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<Invite> invites = JSONObject.parseArray(liststring, Invite.class);//把字符串转换成集合
                inviteList.setValue(invites);
            }

            @Override
            public void onMyFailure(String error) {

            }
        });

        Request.clientGet("project/getApply", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                System.out.println("appay:" + list);
                String liststring = JSONObject.toJSONString(list);
                List<Apply> applys  = JSONObject.parseArray(liststring, Apply.class);//把字符串转换成集合
                applyList.setValue(applys);
            }

            @Override
            public void onMyFailure(String error) {
                System.out.println("error");
            }
        });

        Request.clientGet("notify?asMember=yes&asManager=yes", new NetCallBack(){
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<Notify> notifys = JSONObject.parseArray(liststring, Notify.class);//把字符串转换成集合
                notifyList.setValue(notifys);
            }

            @Override
            public void onMyFailure(String error) {

            }
        });
    }

    public MutableLiveData<List<Notify>> getNotifyList() {
        return notifyList;
    }

    public MutableLiveData<List<Invite>> getInviteList() {
        return inviteList;
    }

    public MutableLiveData<List<Apply>> getApplyList() {
        return applyList;
    }
}
