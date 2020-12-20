package com.iris.ccpm.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.tabs.TabLayout;
import com.iris.ccpm.ProjectDetailActivity;
import com.iris.ccpm.R;
import com.iris.ccpm.TaskDetailActivity;
import com.iris.ccpm.adapter.MypagerAdapter;
import com.iris.ccpm.adapter.TaskAdapter;
import com.iris.ccpm.model.Member;
import com.iris.ccpm.model.TaskModel;
import com.iris.ccpm.ui.my.MyViewModel;
import com.iris.ccpm.ui.project.ProjectViewModel;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {

    private TaskViewModel taskViewModel;
    ViewPager vpTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        View root = inflater.inflate(R.layout.fragment_task, container, false);

        vpTask = root.findViewById(R.id.vp_task);
        initTabContent();

        TabLayout tbTask = root.findViewById(R.id.tb_task);
        tbTask.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpTask.setCurrentItem(tab.getPosition());
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

    private void initTabContent() {
        ArrayList<View> viewList = new ArrayList<View>();
        LayoutInflater li = getActivity().getLayoutInflater();

        View my_task = li.inflate(R.layout.task_list, null, false);
        init_myTask(my_task);

        View manager_task = li.inflate(R.layout.task_list, null, false);
        init_managerTask(manager_task);

        viewList.add(my_task);
        viewList.add(manager_task);
        MypagerAdapter mAdapter = new MypagerAdapter(viewList);
        vpTask.setAdapter((mAdapter));
    }

    private void init_myTask(View my_task) {
        ListView lvMyTask = my_task.findViewById(R.id.lv_task);
        Request.clientGet(getActivity(), "task?asMember=yes&asManager=no", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<TaskModel> taskList = JSONObject.parseArray(liststring, TaskModel.class);//把字符串转换成集合
                TaskAdapter adapter = new TaskAdapter(getActivity(),R.layout.task_item_layout,taskList);

                lvMyTask.setAdapter(adapter);
                lvMyTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TaskModel task = taskList.get(position);
                        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                        intent.putExtra("task",task);
                        System.out.println(position);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onMyFailure(String error) {
            }
        });
    }

    private void init_managerTask(View manager_task) {
        ListView lvManagerTask = manager_task.findViewById(R.id.lv_task);
        Request.clientGet(getActivity(), "task?asManager=yes", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                System.out.println(result);
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<TaskModel> taskList = JSONObject.parseArray(liststring, TaskModel.class);//把字符串转换成集合
                TaskAdapter adapter = new TaskAdapter(getActivity(),R.layout.task_item_layout,taskList);

                lvManagerTask.setAdapter(adapter);
                lvManagerTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TaskModel task = taskList.get(position);
                        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                        intent.putExtra("task",task);
                        System.out.println(position);
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
