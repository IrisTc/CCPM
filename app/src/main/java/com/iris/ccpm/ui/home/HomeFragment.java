package com.iris.ccpm.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.MainActivity;
import com.iris.ccpm.R;

import com.loopj.android.http.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static String[] title = {"1", "2", "3", "4", "5", "6"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        MainActivity activity = (MainActivity) getActivity();
        final ListView lvNews = root.findViewById(R.id.lv_news);
        lvNews.setAdapter(new Myadapter());

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
            View view = inflater.inflate(R.layout.news_item_layout, parent, false);
            TextView titleText = (TextView) view.findViewById(R.id.title);
            titleText.setText(title[position]);
            return view;
        }
    }
}