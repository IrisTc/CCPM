package com.iris.ccpm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.tabs.TabLayout;
import com.iris.ccpm.adapter.Members;
import com.iris.ccpm.adapter.MypagerAdapter;
import com.iris.ccpm.adapter.ProjectMembersAdapter;
import com.iris.ccpm.adapter.TaskAdapter;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.model.TaskModel;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;

import static com.loopj.android.http.AsyncHttpClient.log;

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
        ArrayList<TaskModel> tasks;
        tasks=new ArrayList<>();
        ListView taskList=task_view.findViewById(R.id.task_list);
        Request.clientGet(ProjectDetailActivity.this, "task", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                TaskModel data=new TaskModel();
                JSONArray list=result.getJSONArray("list");
                for(int i=0;i<list.size();++i) {
                    JSONObject obj = list.getJSONObject(i);
                    data.setClaimState(obj.getInteger("claimState"));
                    data.setClaim_uid(obj.getInteger("claim_uid"));
                    data.setProject_uid(obj.getString("project_uid"));
                    data.setTaskEmergent(obj.getInteger("taskEmergent"));
                    data.setTaskEndTime(obj.getString("taskEndTime"));
                    data.setTaskName(obj.getString("taskName"));
                    data.setTaskPredictHours(obj.getString("taskPredictHours"));
                    data.setTaskRestHours(obj.getString("taskRestHours"));
                    data.setTaskStartTime(obj.getString("taskStartTime"));
                    data.setTaskState(obj.getInteger("taskState"));
                    data.setTaskSynopsis(obj.getString("taskSynopsis"));
                    data.setTask_uid(obj.getInteger("task_uid"));
                    tasks.add(data);
                }
                TaskAdapter adapter=new TaskAdapter(ProjectDetailActivity.this,R.layout.task_item_layout,tasks);
                taskList.setAdapter(adapter);
                taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(ProjectDetailActivity.this, TaskDetailActivity.class);
                        intent.putExtra("taskIndex",position);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onMyFailure(String error) {
                Log.d("jsonError",error);
                Toast.makeText(ProjectDetailActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });

        Button addBtn=task_view.findViewById(R.id.addTaskButton);
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TaskModel task=new TaskModel();
//                JSONObject obj=new JSONObject();
//                obj.put("claimState",task.getClaimState());
//                obj.put("claim_uid",task.getClaim_uid());
//                obj.put("project_uid",task.getProject_uid());
//                obj.put("taskEmergent",task.getTaskEmergent());
//                obj.put("taskEndTime",task.getTaskEmergent());
//                obj.put("taskName",task.getTaskName());
//                obj.put("taskPredictHours",task.getTaskPredictHours());
//                obj.put("taskRestHours",task.getTaskRestHours());
//                obj.put("taskStartTime",task.getTaskStartTime());
//                obj.put("taskState",task.getTaskState());
//                obj.put("taskSynopsis",task.getTaskSynopsis());
//                obj.put("task_uid",task.getTask_uid());
//                StringEntity entity=new StringEntity(obj.toJSONString(),"UTF-8");
//                Request.clientPost(ProjectDetailActivity.this,"task",entity,new NetCallBack(){
//                    @Override
//                    public void onMySuccess(JSONObject result) {
//
//                    }
//
//                    @Override
//                    public void onMyFailure(String error) {
//                        Toast.makeText(ProjectDetailActivity.this, error, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        });
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
        for(int i=0;i<4;++i)
            memberList.add(new Members("用户名","xxxxxx@xx.com","xx部门",R.drawable.logo));
        ProjectMembersAdapter member_adapter = new ProjectMembersAdapter(this,R.layout.member_item,memberList);
        ListView member_list = (ListView)view.findViewById(R.id.member_list);
        member_list.setAdapter(member_adapter);
        setListViewHeightBasedOnChildren(member_list, member_adapter);
    }


    private void init_new(View new_view) {

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
