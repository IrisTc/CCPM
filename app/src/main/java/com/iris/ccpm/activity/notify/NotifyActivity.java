package com.iris.ccpm.activity.notify;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.R;
import com.iris.ccpm.adapter.InviteAdapter;
import com.iris.ccpm.adapter.NotifyAdapter;
import com.iris.ccpm.model.Invite;
import com.iris.ccpm.model.Notify;
import com.iris.ccpm.ui.home.HomeViewModel;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

public class NotifyActivity extends AppCompatActivity {
    List<Notify> notifyList;
    NotifyViewModel notifyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notifyViewModel = new ViewModelProvider(this).get(NotifyViewModel.class);
        setContentView(R.layout.activity_notify);

        ListView lvInvite = findViewById(R.id.lv_invite);
        ListView lvNotify = findViewById(R.id.lv_tasknotify);

        notifyViewModel.getInviteList().observe(this, new Observer<List<Invite>>() {
            @Override
            public void onChanged(List<Invite> invites) {
                lvInvite.setAdapter(new InviteAdapter(NotifyActivity.this, invites));
            }
        });

        notifyViewModel.getNotifyList().observe(this, new Observer<List<Notify>>() {
            @Override
            public void onChanged(List<Notify> notifys) {
                lvNotify.setAdapter(new NotifyAdapter(NotifyActivity.this, notifys));
            }
        });
    }
}
