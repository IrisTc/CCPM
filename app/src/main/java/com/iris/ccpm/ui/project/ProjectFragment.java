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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;
import com.iris.ccpm.R;
import com.iris.ccpm.activity.projectDetail.ProjectDetailActivity;
import com.iris.ccpm.adapter.MypagerAdapter;
import com.iris.ccpm.adapter.ProjectAdapter;
import com.iris.ccpm.model.Project;

import java.util.ArrayList;
import java.util.List;


public class ProjectFragment extends Fragment {

    private ProjectViewModel projectViewModel;
    ArrayList<View> viewList;
    MypagerAdapter mAdapter;
    TabLayout tbSelect;
    ViewPager vpChosen;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        projectViewModel =
                new ViewModelProvider(this).get(ProjectViewModel.class); //获取ViewModel,让ViewModel与此activity绑定
        View root = inflater.inflate(R.layout.fragment_project, container, false);

        findView(root);
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

    @Override
    public void onResume() {
        super.onResume();
        projectViewModel.update();
    }

    private void findView(View root) {
        vpChosen = (ViewPager) root.findViewById(R.id.vp_chosen);
        tbSelect = (TabLayout) root.findViewById(R.id.tb_detail);
    }

    private void initTabContent() {
        LayoutInflater li = getActivity().getLayoutInflater();

        View myProject_view = li.inflate(R.layout.my_detail_project, null, false);
        MutableLiveData<List<Project>> myList = projectViewModel.getMyProjectList();
        getProject(myProject_view, myList);

        View otherProject_view = li.inflate(R.layout.my_detail_project, null, false);
        MutableLiveData<List<Project>> otherList = projectViewModel.getOtherProjectList();
        getProject(otherProject_view, otherList);

        viewList = new ArrayList<View>();
        viewList.add(myProject_view);
        viewList.add(otherProject_view);
        mAdapter = new MypagerAdapter(viewList);
        vpChosen.setAdapter((mAdapter));
    }

    private void getProject(View otherProject_view, MutableLiveData<List<Project>> list) {
        ListView lvProject = otherProject_view.findViewById(R.id.lv_project);
        list.observe(getViewLifecycleOwner(), new Observer<List<Project>>() {
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
}