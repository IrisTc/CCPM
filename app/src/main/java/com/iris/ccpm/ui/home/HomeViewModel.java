package com.iris.ccpm.ui.home;

import android.widget.ListView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.R;
import com.iris.ccpm.adapter.DynamicAdapter;
import com.iris.ccpm.model.Dynamic;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

public class  HomeViewModel extends ViewModel {

    private MutableLiveData<List<Dynamic>> dynamicList;

    public HomeViewModel() {
        dynamicList = new MutableLiveData<List<Dynamic>>();
        System.out.println("homeview");
        update();
    }

    public void update() {
        System.out.println("home update");
        Request.clientGet("dynamic", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<Dynamic> dynamics = JSONObject.parseArray(liststring, Dynamic.class);//把字符串转换成集合
                dynamicList.setValue(dynamics);
            }

            @Override
            public void onMyFailure(String error) {

            }
        });
    }

    public MutableLiveData<List<Dynamic>> getDynamicList() {
        return dynamicList;
    }
}