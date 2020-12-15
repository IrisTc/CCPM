package com.iris.ccpm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.tabs.TabLayout;
import com.iris.ccpm.adapter.DynamicAdapter;
import com.iris.ccpm.adapter.Members;
import com.iris.ccpm.adapter.MypagerAdapter;
import com.iris.ccpm.adapter.ProjectMembersAdapter;
import com.iris.ccpm.model.Dynamic;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends AppCompatActivity {

    Integer project_id;
    TabLayout tbSelect;
    ViewPager vpChosen;
    ArrayList<View> viewList;
    MypagerAdapter mAdapter;
    Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        Intent intent  = this.getIntent();
        project = (Project) intent.getSerializableExtra("project");
        project_id = project.getProject_uid();

        findView();

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

    }

    private void initTabContent() {
        viewList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();

        View intro_view = li.inflate(R.layout.project_detail_intro, null, false);
        init_intro(intro_view);

        View new_view = li.inflate(R.layout.project_detail_new, null, false);
        init_new(new_view);

        View task_view = li.inflate(R.layout.project_detail_task, null, false);
        init_task(task_view);

        viewList.add(new_view);
        viewList.add(task_view);
        viewList.add(intro_view);

        mAdapter = new MypagerAdapter(viewList);
        vpChosen.setAdapter((mAdapter));
    }

    private void init_task(View task_view) {
    }

    private void init_intro(View view) {
        ImageView post_avatar=(ImageView)view.findViewById(R.id.project_icon);
        post_avatar.setImageResource(R.drawable.logo);
        TextView project_name_text = view.findViewById(R.id.project_name_text);
        project_name_text.setText(project.getProjectName());
        TextView project_date_text = view.findViewById(R.id.project_date_text);
        project_date_text.setText(project.getProjectStartTime());
        TextView project_motto_text = view.findViewById(R.id.project_motto_text);
        project_motto_text.setText(project.getProjectSynopsis());
        TextView project_time_text = view.findViewById(R.id.project_time_text);
        project_time_text.setText(project.getProjectStartTime() + "-" + project.getProjectEndTime());
        List<Members> memberList = new ArrayList<Members>();
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        ProjectMembersAdapter member_adapter = new ProjectMembersAdapter(this,R.layout.member_item,memberList);
        ListView member_list = (ListView)view.findViewById(R.id.member_list);
        member_list.setAdapter(member_adapter);
        setListViewHeightBasedOnChildren(member_list, member_adapter);
    }


    private void init_new(View new_view) {
        Request.clientGet(ProjectDetailActivity.this, "dynamic?project_uid=" + project_id, new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                System.out.println("projectnews:" + result);
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);

                List<Dynamic> dynamicList = JSONObject.parseArray(liststring, Dynamic.class);//把字符串转换成集合
                ListView lvNew = new_view.findViewById(R.id.lv_news);
                lvNew.setAdapter(new DynamicAdapter(ProjectDetailActivity.this, dynamicList));
            }

            @Override
            public void onMyFailure(String error) {

            }
        });
    }

    public void setListViewHeightBasedOnChildren(ListView listView, ProjectMembersAdapter adapter) {
        // 获取ListView对应的Adapter
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        System.out.println(params.height);
        listView.setLayoutParams(params);
    }

    private void findView() {
        vpChosen = (ViewPager) findViewById(R.id.vp_chosen);
        tbSelect = (TabLayout) findViewById(R.id.tb_detail);
    }
}
