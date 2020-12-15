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
import com.iris.ccpm.adapter.MemberAdapter;
import com.iris.ccpm.adapter.MypagerAdapter;
import com.iris.ccpm.model.Dynamic;
import com.iris.ccpm.model.Member;
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
        TextView project_date_text = view.findViewById(R.id.project_date_text);
        TextView project_motto_text = view.findViewById(R.id.project_motto_text);
        TextView project_time_text = view.findViewById(R.id.project_time_text);
        TextView project_plan_text = view.findViewById(R.id.project_plan);

        project_name_text.setText(project.getProjectName());
        project_date_text.setText(project.getManagerNickName() + " 创建于 " + project.getProjectCreateTime());
        project_motto_text.setText(project.getProjectSynopsis());
        project_time_text.setText(project.getProjectStartTime() + "-" + project.getProjectEndTime());
        project_plan_text.setText(project.getProjectPlan());

        Request.clientGet(ProjectDetailActivity.this, "statistics?project_id=" + project_id, new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                System.out.println("statistics:" + result);
                TextView tvIngNumber = view.findViewById(R.id.ing_number);
                TextView tvDoneNumber = view.findViewById(R.id.done_number);
                TextView tvOverNumber = view.findViewById(R.id.overdue_number);
                TextView tvNoClaimNumber = view.findViewById(R.id.noclaim_number);
                TextView tvExpireNumber = view.findViewById(R.id.expiretoday_number);
                TextView tvCountNumber = view.findViewById(R.id.count_number);
                TextView tvMemberNumber = view.findViewById(R.id.member_number);

                tvMemberNumber.setText("项目成员（" + result.getString("proMemNum") + " 人）");
                tvIngNumber.setText(result.getString("ingTaskPro"));
                tvDoneNumber.setText(result.getString("doneTaskPro"));
                tvOverNumber.setText(result.getString("hasOverdue"));
                tvNoClaimNumber.setText(result.getString("noClaimTask"));
                tvExpireNumber.setText(result.getString("expireToday"));

                Integer count = result.getInteger("ingTaskPro") + result.getInteger("doneTaskPro") + result.getInteger("hasOverdue") + result.getInteger("noClaimTask");
                tvCountNumber.setText(String.valueOf(count));
            }

            @Override
            public void onMyFailure(String error) {

            }
        });

        Request.clientGet(ProjectDetailActivity.this, "project/" + project_id + "/member", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                System.out.println("member:" +result);
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);

                List<Member> memberList = JSONObject.parseArray(liststring, Member.class);//把字符串转换成集合
                ListView lvMember = view.findViewById(R.id.lv_member);
                MemberAdapter adapter = new MemberAdapter(ProjectDetailActivity.this, memberList);
                lvMember.setAdapter(adapter);
                setListViewHeightBasedOnChildren(lvMember, adapter);
            }

            @Override
            public void onMyFailure(String error) {

            }
        });
    }


    private void init_new(View new_view) {
        Request.clientGet(ProjectDetailActivity.this, "dynamic?project_uid=" + project_id, new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
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

    public void setListViewHeightBasedOnChildren(ListView listView, MemberAdapter adapter) {
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
