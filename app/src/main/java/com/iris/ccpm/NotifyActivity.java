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
import com.iris.ccpm.ui.home.HomeFragment;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

public class NotifyActivity extends AppCompatActivity {
    private static String[] title = {"1", "2", "3", "4", "5", "6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        Request.clientGet(NotifyActivity.this, "notify?asManager=yes", new NetCallBack(){

            @Override
            public void onMySuccess(JSONObject result) {
                System.out.println(result);
                JSONArray list = result.getJSONArray("list");
                System.out.println(list);
            }

            @Override
            public void onMyFailure(String error) {

            }
        });

        final ListView lvNotify = findViewById(R.id.lv_notify);
        lvNotify.setAdapter(new NotifyActivity.Myadapter());
    }

    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public Object getItem(int position) {
            return title[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.notify_item_layout, parent, false);
            TextView titleText = (TextView) view.findViewById(R.id.content);
            titleText.setText(title[position]);
            return view;
        }
    }
}
