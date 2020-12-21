package com.iris.ccpm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.adapter.NotifyAdapter;
import com.iris.ccpm.model.Notify;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.ui.home.HomeFragment;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

public class NotifyActivity extends AppCompatActivity {
    private static String[] title = {"1", "2", "3", "4", "5", "6"};
    List<Notify> notifyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        Request.clientGet(NotifyActivity.this, "notify?asMember=yes&asManager=yes", new NetCallBack(){

            @Override
            public void onMySuccess(JSONObject result) {
                System.out.println("notify:" + result);
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);

                notifyList = JSONObject.parseArray(liststring, Notify.class);//把字符串转换成集合
                final ListView lvNotify = findViewById(R.id.lv_notify);
                lvNotify.setAdapter(new NotifyAdapter(NotifyActivity.this, notifyList));
            }

            @Override
            public void onMyFailure(String error) {

            }
        });
    }
}
