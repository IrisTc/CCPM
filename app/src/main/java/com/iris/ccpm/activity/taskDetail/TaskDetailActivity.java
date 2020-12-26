package com.iris.ccpm.activity.taskDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.iris.ccpm.R;
import com.iris.ccpm.activity.ReportCreateActivity;
import com.iris.ccpm.adapter.ReportAdapter;
import com.iris.ccpm.model.GlobalData;
import com.iris.ccpm.model.Report;
import com.iris.ccpm.model.TaskModel;
import com.iris.ccpm.utils.setListViewHeight;

import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {
    private TextView tvExe;
    private TextView taskName;
    private TextView StartTimeView;
    private TextView EndTimeView;
    private TextView predictTime;
    private TextView restTime;
    private TextView taskSynopsis;
    private TextView tvComplete;
    private TextView tvNickname;
    private TextView tvPrio;
    TaskModel task;
    TaskDetailViewModel taskDetailViewModel;
    ListView lvReport;
    ReportAdapter reportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        task = (TaskModel) getIntent().getSerializableExtra("task");

        GlobalData app = (GlobalData) getApplication();
        app.setNow_task_id(task.getTask_uid());
        taskDetailViewModel = new ViewModelProvider(this).get(TaskDetailViewModel.class);

        findView();
        LoadData();

        taskDetailViewModel.getReportList().observe(this, new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                reportAdapter = new ReportAdapter(TaskDetailActivity.this, reports);
                lvReport.setAdapter(reportAdapter);
                setListViewHeight.set(lvReport, reportAdapter);
            }
        });

        //判断是否自己是否是执行者，是的话显示汇报按钮
        if (task.getClaim_uid() == app.getUid()) {
            TextView reportBtn = findViewById(R.id.reportBtn);
            reportBtn.setVisibility(View.VISIBLE);
            reportBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TaskDetailActivity.this, ReportCreateActivity.class);
                    intent.putExtra("task", task);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskDetailViewModel.update();
    }

    private void findView() {
        taskName = findViewById(R.id.TaskName);
        tvPrio = findViewById(R.id.tv_prio);
        tvComplete = findViewById(R.id.tv_complete);
        tvExe = findViewById(R.id.ExecuteState);
        StartTimeView = findViewById(R.id.StartTime);
        EndTimeView = findViewById(R.id.EndTime);
        tvNickname = findViewById(R.id.tv_nickname);
        predictTime = findViewById(R.id.predictEdit);
        restTime = findViewById(R.id.remainEdit);
        taskSynopsis = findViewById(R.id.taskSynopsis);
        lvReport = findViewById(R.id.lv_report);
    }

    public void LoadData() {

        String[] completeItems = {"未完成","已完成"};
        int[] completeColors={Color.RED,0xFF7FFFAA};
        int[] completeTextColors={Color.WHITE,Color.BLACK};

        String[] prioItems = {"普通", "紧急", "非常紧急"};
        int[] prioColors = {0xFF7FFFAA, 0xFFFF7F50, Color.RED};
        int[] prioTextColors = {Color.BLACK, Color.WHITE, Color.WHITE};

        String[] exeItems = {"拒绝","接受","未处理"};

        Integer complete = task.getTaskState();
        Integer prio = task.getTaskEmergent();
        Integer claim = task.getClaimState();

        tvComplete.setText(completeItems[complete]);
        tvComplete.setBackgroundColor((completeColors[complete]));
        tvComplete.setTextColor(completeTextColors[complete]);
        tvPrio.setText(prioItems[prio]);
        tvPrio.setBackgroundColor((prioColors[prio]));
        tvPrio.setTextColor(prioTextColors[prio]);
        tvExe.setText(exeItems[claim]);

        tvNickname.setText(task.getClaimNickName());
        taskName.setText(task.getTaskName());
        restTime.setText(task.getTaskRestHours());
        predictTime.setText(task.getTaskPredictHours());
        taskSynopsis.setText(task.getTaskSynopsis());
        StartTimeView.setText(task.getTaskStartTime());
        EndTimeView.setText(task.getTaskEndTime());
    }

}