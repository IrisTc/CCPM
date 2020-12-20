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

import com.iris.ccpm.adapter.DynamicAdapter;
import com.iris.ccpm.adapter.ProjectAdapter;
import com.iris.ccpm.model.Dynamic;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;
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
    List<Dynamic> dynamicList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Request.clientGet(getActivity(), "dynamic", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                System.out.println("project:" + result);
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);

                dynamicList = JSONObject.parseArray(liststring, Dynamic.class);//把字符串转换成集合
                ListView lvNew = root.findViewById(R.id.lv_news);
                lvNew.setAdapter(new DynamicAdapter(getActivity(), dynamicList));
            }

            @Override
            public void onMyFailure(String error) {

            }
        });

        return root;
    }
}