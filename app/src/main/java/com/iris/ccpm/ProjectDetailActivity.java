package com.iris.ccpm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.tabs.TabLayout;
import com.iris.ccpm.adapter.DynamicAdapter;
import com.iris.ccpm.adapter.MemberAdapter;
import com.iris.ccpm.adapter.MypagerAdapter;
import com.iris.ccpm.model.Dynamic;
import com.iris.ccpm.model.Member;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.adapter.TaskAdapter;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.model.TaskModel;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;

import static com.loopj.android.http.AsyncHttpClient.log;

public class ProjectDetailActivity extends AppCompatActivity implements View.OnClickListener {

    String project_id;
    TabLayout tbSelect;
    ViewPager vpChosen;
    ArrayList<View> viewList;
    MypagerAdapter mAdapter;
    Project project;
    List<Member> memberList;
    MemberAdapter memberAdapter;
    View intro_view;


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

        intro_view = li.inflate(R.layout.project_detail_intro, null, false);
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
        ArrayList<TaskModel> tasks;
        tasks=new ArrayList<>();
        ListView lvTask =task_view.findViewById(R.id.task_list);
        Request.clientGet(ProjectDetailActivity.this, "task?project=" + project_id , new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<TaskModel> taskList = JSONObject.parseArray(liststring, TaskModel.class);//把字符串转换成集合
                TaskAdapter adapter = new TaskAdapter(ProjectDetailActivity.this ,R.layout.task_item_layout,taskList);
                lvTask.setAdapter(adapter);
                lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TaskModel task = taskList.get(position);
                        Intent intent = new Intent(ProjectDetailActivity.this, TaskDetailActivity.class);
                        intent.putExtra("task",task);
                        System.out.println(position);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onMyFailure(String error) {
                Toast.makeText(ProjectDetailActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });

        Button addBtn=task_view.findViewById(R.id.addTaskButton);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskModel task=new TaskModel();
                JSONObject obj=new JSONObject();
                obj.put("claimState",task.getClaimState());
                obj.put("claim_uid",task.getClaim_uid());
                obj.put("project_uid",1336756231);
                obj.put("taskEmergent",task.getTaskEmergent());
                obj.put("taskEndTime",task.getTaskEmergent());
                obj.put("taskName",task.getTaskName());
                obj.put("taskPredictHours",task.getTaskPredictHours());
                obj.put("taskRestHours",task.getTaskRestHours());
                obj.put("taskStartTime",task.getTaskStartTime());
                obj.put("taskState",task.getTaskState());
                obj.put("taskSynopsis",task.getTaskSynopsis());
                obj.put("task_uid",task.getTask_uid());
                StringEntity entity=new StringEntity(obj.toJSONString(),"UTF-8");
//                Request.clientPost(ProjectDetailActivity.this,"project/1336756231/task",entity,new NetCallBack(){
//                    @Override
//                    public void onMySuccess(JSONObject result) {
//                        Toast.makeText(ProjectDetailActivity.this, "创建成功", Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onMyFailure(String error) {
//                        System.out.println(obj.toJSONString());
//                        Toast.makeText(ProjectDetailActivity.this, error, Toast.LENGTH_LONG).show();
//                    }
//                });
            }
        });
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

        Request.clientGet(ProjectDetailActivity.this, "statistics?ingTaskPro=yes&doneTaskPro=yes&hasOverdue=yes&noClaimTask&expireToday=yes&proMemNum=yes&project_uid=" + project_id, new NetCallBack() {
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
        ListView lvMember = view.findViewById(R.id.lv_member);
        getMember(lvMember);
    }

    private void getMember(ListView lvMember) {
        Request.clientGet(ProjectDetailActivity.this, "project/" + project_id + "/member", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                memberList = JSONObject.parseArray(liststring, Member.class);//把字符串转换成集合
                MemberAdapter memberAdapter = new MemberAdapter(ProjectDetailActivity.this, project_id, memberList, ProjectDetailActivity.this);
                lvMember.setAdapter(memberAdapter);
                setListViewHeightBasedOnChildren(lvMember, memberAdapter);
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

    @Override
    public void onClick(View v) {
        int pos = (int) v.getTag();
        Member member = memberList.get(pos);
        switch (v.getId()) {
            case R.id.member_delete_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectDetailActivity.this);
                builder.setIcon(R.drawable.ic_warn);
                builder.setTitle("警告");
                builder.setMessage("确定移除该成员【" + member.getNickName() + "】吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        JSONObject body = new JSONObject();
                        body.put("project_uid", project_id);
                        body.put("username", member.getUsername());
                        StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                        Request.clientPost(ProjectDetailActivity.this, "/project/" + project_id + "/remove/" + member.getUsername(), entity, new NetCallBack() {
                            @Override
                            public void onMySuccess(JSONObject result) {
                                Toast.makeText(ProjectDetailActivity.this, "移除成功！", Toast.LENGTH_SHORT).show();
                                Request.clientGet(ProjectDetailActivity.this, "project/" + project_id + "/member", new NetCallBack() {
                                    @Override
                                    public void onMySuccess(JSONObject result) {
                                        JSONArray list = result.getJSONArray("list");
                                        String liststring = JSONObject.toJSONString(list);
                                        memberList = JSONObject.parseArray(liststring, Member.class);//把字符串转换成集合
                                    }

                                    @Override
                                    public void onMyFailure(String error) {
                                    }
                                });
                                memberAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onMyFailure(String error) {

                            }
                        });
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which){}
                });
                builder.show();
                break;
            case R.id.member_personal_button:
                Intent intent = new Intent(ProjectDetailActivity.this, MemberDetailActivity.class);
                intent.putExtra("account_id", member.getAccount_uid());
                startActivity(intent);
        }
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
