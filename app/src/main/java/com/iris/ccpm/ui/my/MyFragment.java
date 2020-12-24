package com.iris.ccpm.ui.my;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.tabs.TabLayout;
import com.iris.ccpm.activity.projectDetail.ProjectDetailActivity;
import com.iris.ccpm.R;
import com.iris.ccpm.activity.taskDetail.TaskDetailActivity;
import com.iris.ccpm.adapter.MypagerAdapter;
import com.iris.ccpm.adapter.ProjectAdapter;
import com.iris.ccpm.adapter.TaskAdapter;
import com.iris.ccpm.model.GlobalData;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment {

    private MyViewModel myViewModel;
    TabLayout tbSelect;
    ViewPager vpChosen;
    SimpleDraweeView ivAvatar;
    ArrayList<View> viewList;
    MypagerAdapter mAdapter;
    TextView tvNickname;
    TextView tvEmail;
    GlobalData app;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myViewModel =
                new ViewModelProvider(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my, container, false);

        findView(root);
        getMyGlobalData();
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

    private void getMyGlobalData() {
        app = (GlobalData) getActivity().getApplication();
        tvNickname.setText(app.getName());
        tvEmail.setText(app.getUsername());
        Uri uri = Uri.parse(app.getAvatarUrl());
        ivAvatar.setImageURI(uri);
    }

    private void findView(View root) {
        tvNickname = root.findViewById(R.id.tv_nickname);
        tvEmail = root.findViewById(R.id.tv_email);
        ivAvatar = root.findViewById(R.id.iv_avatar);
        vpChosen = (ViewPager) root.findViewById(R.id.vp_chosen);
        tbSelect = (TabLayout) root.findViewById(R.id.tb_detail);
    }

    private void initTabContent() {
        LayoutInflater li = getActivity().getLayoutInflater();

        View intro_view = li.inflate(R.layout.my_detail_intro, null, false);
        init_intro(intro_view);

        View project_view = li.inflate(R.layout.my_detail_project, null, false);
        init_project(project_view);

        View task_view = li.inflate(R.layout.my_detail_task, null, false);
        init_task(task_view);

        viewList = new ArrayList<View>();
        viewList.add(intro_view);
        viewList.add(project_view);
        viewList.add(task_view);
        mAdapter = new MypagerAdapter(viewList);
        vpChosen.setAdapter((mAdapter));
    }

    private void init_task(View task_view) {
        ListView lvTask = task_view.findViewById(R.id.lv_task);
        myViewModel.getTaskList().observe(getViewLifecycleOwner(), new Observer<List<TaskModel>>() {
            @Override
            public void onChanged(List<TaskModel> tasks) {
                TaskAdapter adapter = new TaskAdapter(getActivity(),R.layout.task_item_layout,tasks);
                lvTask.setAdapter(adapter);
                lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private void init_project(View project_view) {
        ListView lvProject = project_view.findViewById(R.id.lv_project);
        myViewModel.getProjectList().observe(getViewLifecycleOwner(), new Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> projects) {
                lvProject.setAdapter(new ProjectAdapter(getActivity(), projects));
                lvProject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Project project = projects.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("project", project);

                        Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void init_intro(View intro_view) {
        TextView tvUsername = intro_view.findViewById(R.id.tv_username);
        TextView tvNickname = intro_view.findViewById(R.id.tv_nickname);
        TextView tvRealname = intro_view.findViewById(R.id.tv_realname);
        TextView tvPhonenum = intro_view.findViewById(R.id.tv_phoneNum);
        TextView tvPosition = intro_view.findViewById(R.id.tv_position);
        TextView tvSynopsis = intro_view.findViewById(R.id.tv_synopsis);
        TextView tvProjectCount = intro_view.findViewById(R.id.project_count);
        TextView tvTaskCount = intro_view.findViewById(R.id.task_count);
        TextView tvIngProject = intro_view.findViewById(R.id.ingproject);
        TextView tvIngTask = intro_view.findViewById(R.id.ingtask);

        tvUsername.setText(app.getUsername());
        tvNickname.setText(app.getNickName());
        tvRealname.setText(app.getRealName());
        tvPhonenum.setText(app.getPhoneNum());
        tvPosition.setText(app.getPosition());
        tvSynopsis.setText(app.getSynopsis());

        myViewModel.getMyStatistics().observe(getViewLifecycleOwner(), new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject result) {
                tvProjectCount.setText(result.getString("projectNum"));
                tvTaskCount.setText(result.getString("taskNum"));
                tvIngProject.setText(result.getString("ingProject"));
                tvIngTask.setText(result.getString("ingTask"));
            }
        });
    }
}