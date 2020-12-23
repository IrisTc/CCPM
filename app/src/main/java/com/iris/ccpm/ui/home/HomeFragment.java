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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class); //获取ViewModel,让ViewModel与此activity绑定
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ListView lvNew = root.findViewById(R.id.lv_news);

        homeViewModel.getDynamicList().observe(getViewLifecycleOwner(), new Observer<List<Dynamic>>() { //注册观察者
            @Override
            public void onChanged(List<Dynamic> dynamics) {
                System.out.println("data change");
                lvNew.setAdapter(new DynamicAdapter(getActivity(), dynamics));
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        homeViewModel.update();
    }
}