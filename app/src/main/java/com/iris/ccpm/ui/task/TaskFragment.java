package com.iris.ccpm.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iris.ccpm.R;
import com.iris.ccpm.TaskDetailActivity;
import com.iris.ccpm.adapter.MypagerAdapter;
import com.iris.ccpm.adapter.TaskAdapter;
import com.iris.ccpm.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {

    private TaskViewModel taskViewModel;
    ViewPager vpTask;
    TabLayout tbTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        View root = inflater.inflate(R.layout.fragment_task, container, false);

        findView(root);
        initTabContent();

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

    private void findView(View root) {
        vpTask = root.findViewById(R.id.vp_task);
        tbTask = root.findViewById(R.id.tb_task);
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
        taskViewModel.getMyTaskList().observe(getViewLifecycleOwner(), new Observer<List<TaskModel>>() {
            @Override
            public void onChanged(List<TaskModel> tasks) {
                TaskAdapter adapter = new TaskAdapter(getActivity(),R.layout.task_item_layout,tasks);
                lvMyTask.setAdapter(adapter);
                lvMyTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TaskModel task = tasks.get(position);
                        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                        intent.putExtra("isManager", false);
                        intent.putExtra("isCreate",false);
                        intent.putExtra("task", task);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void init_managerTask(View manager_task) {
        ListView lvManagerTask = manager_task.findViewById(R.id.lv_task);
        taskViewModel.getManagerTaskList().observe(getViewLifecycleOwner(), new Observer<List<TaskModel>>() {
            @Override
            public void onChanged(List<TaskModel> tasks) {
                TaskAdapter adapter = new TaskAdapter(getActivity(),R.layout.task_item_layout,tasks);

                lvManagerTask.setAdapter(adapter);
                lvManagerTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TaskModel task = tasks.get(position);
                        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                        intent.putExtra("isManager", true);
                        intent.putExtra("isCreate",false);
                        intent.putExtra("task", task);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
