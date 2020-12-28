package com.iris.ccpm.activity.projectDetail;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.tabs.TabLayout;
import com.iris.ccpm.activity.EditProjectActivity;
import com.iris.ccpm.activity.MemberDetailActivity;
import com.iris.ccpm.activity.MemberSearchActivity;
import com.iris.ccpm.R;
import com.iris.ccpm.activity.taskCreate.TaskCreateActivity;
import com.iris.ccpm.activity.taskDetail.TaskDetailActivity;
import com.iris.ccpm.adapter.DynamicAdapter;
import com.iris.ccpm.adapter.MemberAdapter;
import com.iris.ccpm.adapter.MypagerAdapter;
import com.iris.ccpm.adapter.TaskAdapter;
import com.iris.ccpm.model.Dynamic;
import com.iris.ccpm.model.GlobalData;
import com.iris.ccpm.model.Member;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.model.TaskModel;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;
import com.iris.ccpm.utils.setListViewHeight;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.entity.StringEntity;

public class ProjectDetailActivity extends AppCompatActivity implements View.OnClickListener {
    ProjectDetailViewModel projectDetailViewModel;
    String project_id;
    TabLayout tbSelect;
    ViewPager vpChosen;
    ArrayList<View> viewList;
    MypagerAdapter mAdapter;
    Project project;
    List<Member> memberList;
    MemberAdapter memberAdapter;
    View intro_view;
    Boolean isManager = false;
    ListView lvTask;
    TextView tvIngNumber;
    TextView tvDoneNumber;
    TextView tvOverNumber;
    TextView tvNoClaimNumber;
    TextView tvExpireNumber;
    TextView tvCountNumber;
    TextView tvMemberNumber;
    ListView lvDynamic;
    ListView lvMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        Toolbar toolbar = findViewById(R.id.project_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标

        Intent intent  = this.getIntent();
        project = (Project) intent.getSerializableExtra("project");
        project_id = project.getProject_uid();
        GlobalData app = (GlobalData) getApplication();
        app.setNow_project_id(project_id);
        projectDetailViewModel = new ViewModelProvider(this).get(ProjectDetailViewModel.class);

        if (project.getManager_uid().equals(app.getUid())) {
            isManager = true;
        }

        findView();
        initTabContent();
        getData();

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

    @Override
    protected void onResume() {
        super.onResume();
        projectDetailViewModel.update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.project_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case R.id.edit_project:{
                editProject();  //点击跳转至更新项目
                break;
            }
            case R.id.add_member:{
                addMember();    //点击跳转至添加成员
                break;
            }
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initTabContent() {
        viewList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();

        intro_view = li.inflate(R.layout.project_detail_intro, null, false);
        init_intro(intro_view);

        View new_view = li.inflate(R.layout.project_detail_new, null, false);
        lvDynamic = new_view.findViewById(R.id.lv_news);

        View task_view = li.inflate(R.layout.project_detail_task, null, false);
        init_task(task_view);

        viewList.add(new_view);
        viewList.add(task_view);
        viewList.add(intro_view);

        mAdapter = new MypagerAdapter(viewList);
        vpChosen.setAdapter((mAdapter));
    }

    private void init_task(View task_view) {
        lvTask = task_view.findViewById(R.id.task_list);

        Button addBtn = task_view.findViewById(R.id.addTaskButton);
        if (isManager) {
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ProjectDetailActivity.this, TaskCreateActivity.class);
                    intent.putExtra("project_id", project.getProject_uid());
                    intent.putExtra("manager_id", project.getManager_uid());
                    startActivity(intent);
                }
            });
        } else {
            addBtn.setVisibility(View.GONE);
        }
    }

    private void init_intro(View view) {
        ImageView post_avatar = (ImageView) view.findViewById(R.id.project_icon);
        post_avatar.setImageResource(R.drawable.logo);

        TextView project_name_text = view.findViewById(R.id.project_name_text);
        TextView project_date_text = view.findViewById(R.id.project_date_text);
        TextView project_motto_text = view.findViewById(R.id.project_motto_text);
        TextView project_time_text = view.findViewById(R.id.project_time_text);
        TextView project_plan_text = view.findViewById(R.id.project_plan);
        TextView project_uid = view.findViewById(R.id.project_uid);

        tvIngNumber = view.findViewById(R.id.ing_number);
        tvDoneNumber = view.findViewById(R.id.done_number);
        tvOverNumber = view.findViewById(R.id.overdue_number);
        tvNoClaimNumber = view.findViewById(R.id.noclaim_number);
        tvExpireNumber = view.findViewById(R.id.expiretoday_number);
        tvCountNumber = view.findViewById(R.id.count_number);
        tvMemberNumber = view.findViewById(R.id.member_number);

        project_name_text.setText(project.getProjectName());
        project_date_text.setText(project.getManagerNickName() + " 创建于 " + project.getProjectCreateTime());
        project_motto_text.setText(project.getProjectSynopsis());
        project_time_text.setText(project.getProjectStartTime() + "-" + project.getProjectEndTime());
        project_plan_text.setText(project.getProjectPlan());
        project_uid.setText("唯一标识码：" + project.getProject_uid());

        lvMember = view.findViewById(R.id.lv_member);
        Button btMemberAdd = view.findViewById(R.id.bt_member_add);
        btMemberAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectDetailActivity.this, MemberSearchActivity.class);
                intent.putExtra("project_id", project_id);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        projectDetailViewModel.getStatistics().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject result) {
                tvMemberNumber.setText("项目成员（" + result.getString("proMemNum") + " 人）");
                tvIngNumber.setText(result.getString("ingTaskPro"));
                tvDoneNumber.setText(result.getString("doneTaskPro"));
                tvOverNumber.setText(result.getString("hasOverdue"));
                tvNoClaimNumber.setText(result.getString("noClaimTask"));
                tvExpireNumber.setText(result.getString("expireToday"));

                Integer count = result.getInteger("ingTaskPro") + result.getInteger("doneTaskPro") + result.getInteger("hasOverdue") + result.getInteger("noClaimTask");
                tvCountNumber.setText(String.valueOf(count));
            }
        });

        projectDetailViewModel.getTaskList().observe(this, new Observer<List<TaskModel>>() {
            @Override
            public void onChanged(List<TaskModel> tasks) {
                TaskAdapter adapter = new TaskAdapter(ProjectDetailActivity.this ,R.layout.task_item_layout,tasks);
                lvTask.setAdapter(adapter);
                lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TaskModel task = tasks.get(position);
                        Intent intent = new Intent(ProjectDetailActivity.this, TaskDetailActivity.class);
                        intent.putExtra("task", task);
                        startActivity(intent);
                    }
                });
            }
        });

        projectDetailViewModel.getMemberList().observe(this, new Observer<List<Member>>() {
            @Override
            public void onChanged(List<Member> members) {
                memberList = members;
                memberAdapter = new MemberAdapter(ProjectDetailActivity.this, project_id, members, ProjectDetailActivity.this);
                lvMember.setAdapter(memberAdapter);
                setListViewHeight.set(lvMember, memberAdapter);
            }
        });

        projectDetailViewModel.getDynamicList().observe(this, new Observer<List<Dynamic>>() {
            @Override
            public void onChanged(List<Dynamic> dynamics) {
                lvDynamic.setAdapter(new DynamicAdapter(ProjectDetailActivity.this, dynamics));
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
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JSONObject body = new JSONObject();
                        body.put("project_uid", project_id);
                        body.put("username", member.getUsername());
                        StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                        Request.clientPost(ProjectDetailActivity.this, "/project/" + project_id + "/remove/" + member.getUsername(), entity, new NetCallBack() {
                            @Override
                            public void onMySuccess(JSONObject result) {
                                Toast.makeText(ProjectDetailActivity.this, "移除成功！", Toast.LENGTH_SHORT).show();
                                projectDetailViewModel.update();
                            }

                            @Override
                            public void onMyFailure(String error) {
                                Toast.makeText(ProjectDetailActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                break;
            case R.id.member_personal_button:
                Intent intent = new Intent(ProjectDetailActivity.this, MemberDetailActivity.class);
                intent.putExtra("account_id", member.getAccount_uid());
                startActivity(intent);
        }
    }

    private void findView() {
        vpChosen = (ViewPager) findViewById(R.id.vp_chosen);
        tbSelect = (TabLayout) findViewById(R.id.tb_detail);
    }

    public void editProject() {
        Intent intent = new Intent(this, EditProjectActivity.class);     //页面跳转至更新项目
        Bundle bundle = new Bundle();
        bundle.putSerializable("project", project);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    private void addMember() {
        Intent intent = new Intent(this, MemberSearchActivity.class);       //页面跳转至添加成员
        intent.putExtra("project_id", project_id);
        this.startActivity(intent);
    }
}
