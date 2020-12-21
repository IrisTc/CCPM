package com.iris.ccpm.ui.project;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.tabs.TabLayout;
import com.iris.ccpm.EditProjectActivity;
import com.iris.ccpm.ProjectDetailActivity;
import com.iris.ccpm.R;
import com.iris.ccpm.adapter.MypagerAdapter;
import com.iris.ccpm.adapter.ProjectAdapter;
import com.iris.ccpm.model.GlobalData;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.ArrayList;
import java.util.List;


public class ProjectFragment extends Fragment {

    private ProjectViewModel projectViewModel;
    List<Project>  projectList;
    ListView textView;
    ArrayList<View> viewList;
    MypagerAdapter mAdapter;
    TabLayout tbSelect;
    ViewPager vpChosen;
//    GlobalData app;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        projectViewModel =
                new ViewModelProvider(this).get(ProjectViewModel.class);
        View root = inflater.inflate(R.layout.fragment_project, container, false);

//        app = (GlobalData) getActivity().getApplication();
//        Uri uri = Uri.parse(app.getAvatarUrl());

        initView(root);
        initTabContent();

        tbSelect.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpChosen.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return root;
    }

    private void initView(View root) {
        vpChosen = (ViewPager) root.findViewById(R.id.vp_chosen);
        tbSelect = (TabLayout) root.findViewById(R.id.tb_detail);
    }

    private void initTabContent() {
        LayoutInflater li = getActivity().getLayoutInflater();

        View myProject_view = li.inflate(R.layout.my_detail_project, null, false);
        init_myProject(myProject_view);

        View otherProject_view = li.inflate(R.layout.my_detail_project, null, false);
        init_otherProject(otherProject_view);

        viewList = new ArrayList<View>();
        viewList.add(myProject_view);
        viewList.add(otherProject_view);
        mAdapter = new MypagerAdapter(viewList);
        vpChosen.setAdapter((mAdapter));
    }



    private void init_myProject(View myProject_view) {
        ListView project_list = myProject_view.findViewById(R.id.lv_project);

        Request.clientGet(getActivity(), "project?asManager=yes", new NetCallBack(){
            @Override
            public void onMySuccess(JSONObject result) {
                System.out.println("project:" + result);
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<Project> projectList = JSONObject.parseArray(liststring, Project.class);//把字符串转换成集合
                project_list.setAdapter(new ProjectAdapter(getActivity(), projectList));
                project_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Project project = projectList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("project", project);

                        Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onMyFailure(String error) {

            }
        });
    }

    private void init_otherProject(View otherProject_view) {
        ListView project_list = otherProject_view.findViewById(R.id.lv_project);

        Request.clientGet(getActivity(), "project?asManager=no", new NetCallBack(){
            @Override
            public void onMySuccess(JSONObject result) {
                System.out.println("project:" + result);
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<Project> projectList = JSONObject.parseArray(liststring, Project.class);//把字符串转换成集合
                project_list.setAdapter(new ProjectAdapter(getActivity(), projectList));
                project_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Project project = projectList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("project", project);

                        Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onMyFailure(String error) {

            }
        });
    }
}