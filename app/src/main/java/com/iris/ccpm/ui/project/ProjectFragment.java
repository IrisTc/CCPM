package com.iris.ccpm.ui.project;


import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.ProjectDetailActivity;
import com.iris.ccpm.R;
import com.iris.ccpm.adapter.ProjectAdapter;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;


public class ProjectFragment extends Fragment {

    private ProjectViewModel projectViewModel;
    List<Project>  projectList;
    ListView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        projectViewModel =
                new ViewModelProvider(this).get(ProjectViewModel.class);
        View root = inflater.inflate(R.layout.fragment_project, container, false);

        Request.clientGet(getActivity(), "project?asManager=yes", new NetCallBack(){

            @Override
            public void onMySuccess(JSONObject result) {
                System.out.println("project:" + result);
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                System.out.println(liststring);

                projectList = JSONObject.parseArray(liststring, Project.class);//把字符串转换成集合
                textView = root.findViewById(R.id.text_slideshow);
                textView.setAdapter(new ProjectAdapter(getActivity(), projectList));

                textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        return root;
    }
}