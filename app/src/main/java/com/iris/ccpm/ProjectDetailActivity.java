package com.iris.ccpm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.iris.ccpm.adapter.Members;
import com.iris.ccpm.adapter.MypagerAdapter;
import com.iris.ccpm.adapter.Posts;
import com.iris.ccpm.adapter.ProjectMembersAdapter;
import com.iris.ccpm.adapter.ProjectPostsAdapter;
import com.iris.ccpm.model.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends AppCompatActivity {

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
        System.out.println(project.getProjectName());

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

    private void init_intro(View view) {
        TextView project_name_text = view.findViewById(R.id.project_name_text);
        project_name_text.setText(project.getProjectName());
        TextView project_date_text = view.findViewById(R.id.project_date_text);
        project_date_text.setText(project.getProjectStartTime());
        TextView project_motto_text = view.findViewById(R.id.project_motto_text);
        project_motto_text.setText(project.getProjectSynopsis());
        TextView project_time_text = view.findViewById(R.id.project_time_text);
        project_time_text.setText(project.getProjectStartTime() + "-" + project.getProjectEndTime());
    }

    private void initTabContent() {
        viewList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();
        View intro_view = li.inflate(R.layout.project_detail_intro, null, false);
        init_intro(intro_view);

        View posts_view = li.inflate(R.layout.project_detail_posts, null, false);
        View members_view = li.inflate(R.layout.project_detail_member, null, false);

        ImageView post_avatar=(ImageView)intro_view.findViewById(R.id.project_icon);
        post_avatar.setImageResource(R.drawable.logo);

        List<Posts> postList = new ArrayList<Posts>();
        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
        postList.add(new Posts("用户名","xxxx年xx月xx日",R.drawable.logo));
        ProjectPostsAdapter adapter = new ProjectPostsAdapter(this,R.layout.list_item,postList);
        ListView list = (ListView)posts_view.findViewById(R.id.post_list);
        list.setAdapter(adapter);

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
        ListView member_list = (ListView)members_view.findViewById(R.id.member_list);
        member_list.setAdapter(member_adapter);

        viewList.add(intro_view);
        viewList.add(posts_view);
        viewList.add(members_view);
        mAdapter = new MypagerAdapter(viewList);
        vpChosen.setAdapter((mAdapter));
    }

    private void findView() {
        vpChosen = (ViewPager) findViewById(R.id.vp_chosen);
        tbSelect = (TabLayout) findViewById(R.id.tb_detail);
    }
}