package com.iris.ccpm.ui.items;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.MainActivity;
import com.iris.ccpm.R;
import com.iris.ccpm.ui.home.HomeFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;



public class ItemsFragment extends Fragment {

    private ItemsViewModel itemsViewModel;
    private static String[] title= {"1", "2", "3", "4", "5", "6"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        itemsViewModel =
                new ViewModelProvider(this).get(ItemsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_items, container, false);

        MainActivity activity = (MainActivity)getActivity();
        final ListView textView = root.findViewById(R.id.text_slideshow);
        textView.setAdapter(new Myadapter());
        Android_Async_Http_Get();

        return root;
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

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.program_item_layout, parent, false);
            TextView titleText = (TextView)view.findViewById(R.id.title);
            titleText.setText(title[position]);
            return view;
        }
    }

    //Get请求
    private void Android_Async_Http_Get() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://ts.tcualhp.cn/api/ukulele/music?type=sing";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getActivity(), "Get请求成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Get请求失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}