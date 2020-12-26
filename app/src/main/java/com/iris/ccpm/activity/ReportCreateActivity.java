package com.iris.ccpm.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.R;
import com.iris.ccpm.adapter.TaskDetailSpinnerAdapter;
import com.iris.ccpm.model.TaskModel;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import cz.msebera.android.httpclient.entity.StringEntity;

public class ReportCreateActivity extends AppCompatActivity {
    TaskModel task;
    Spinner completeSpinner;
    TextView tvTaskName;
    TextView tvNickname;
    TextView tvStart;
    TextView tvEnd;
    TextView tvMemo;
    TextView tvPrio;
    TextView tvPredictTime;
    TextView tvRestTime;
    EditText etWorkhour;
    EditText etContent;
    EditText etPreHour;
    Button btReport;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_add);

        task = (TaskModel) getIntent().getSerializableExtra("task");

        findView();
        initSpinner();
        loadData();

        btReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etContent.getText().toString();
                String workHour = etWorkhour.getText().toString();
                String preHour = etPreHour.getText().toString();
                Integer state = 0;
                JSONObject body = new JSONObject();
                body.put("workingContent", content);
                body.put("workingTime", workHour);
                body.put("restWorkingTime", preHour);
                body.put("taskState", state);
                StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                Request.clientPut(ReportCreateActivity.this, "task/" + task.getTask_uid(), entity, new NetCallBack() {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        Toast.makeText(ReportCreateActivity.this, "汇报完成", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onMyFailure(String error) {
                        Toast.makeText(ReportCreateActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void loadData() {
        String[] spinnerItems = {"普通", "紧急", "非常紧急"};
        int[] prioColors = {0xFF7FFFAA, 0xFFFF7F50, Color.RED};
        int[] prioTextColors = {Color.BLACK, Color.WHITE, Color.WHITE};

        Integer prio = task.getTaskEmergent();
        tvPrio.setText(spinnerItems[prio]);
        tvPrio.setBackgroundColor((prioColors[prio]));
        tvPrio.setTextColor(prioTextColors[prio]);

        tvTaskName.setText(task.getTaskName());
        tvNickname.setText(task.getClaimNickName());
        tvStart.setText(task.getTaskStartTime());
        tvEnd.setText(task.getTaskEndTime());
        tvMemo.setText(task.getTaskSynopsis());
        tvPredictTime.setText(task.getTaskPredictHours());
        tvRestTime.setText(task.getTaskRestHours());
    }

    private void findView() {
        tvTaskName = findViewById(R.id.tv_taskName);
        tvNickname = findViewById(R.id.tv_nickname);
        tvStart = findViewById(R.id.tv_startTime);
        tvEnd = findViewById(R.id.tv_endTime);
        tvMemo = findViewById(R.id.tv_memo);
        tvPrio = findViewById(R.id.tv_prio);
        tvPredictTime = findViewById(R.id.tv_predictTime);
        tvRestTime = findViewById(R.id.tv_restTime);
        etWorkhour = findViewById(R.id.et_workhour);
        etContent = findViewById(R.id.et_content);
        etPreHour = findViewById(R.id.et_prehour);
        btReport = findViewById(R.id.bt_report);
        completeSpinner = findViewById(R.id.completeSpinner);
    }

    private void initSpinner() {
        completeSpinner.setDropDownWidth(400);
        completeSpinner.setDropDownHorizontalOffset(100);
        completeSpinner.setDropDownVerticalOffset(100);

        String[] completeItems = {"未完成", "已完成"};
        int[] completeColors = {Color.RED, 0xFF7FFFAA};
        int[] completeTextColors = {Color.WHITE, Color.BLACK};

        TaskDetailSpinnerAdapter completeAdapter = new TaskDetailSpinnerAdapter(this, completeItems, completeColors, completeTextColors);
        completeAdapter.setDropDownViewResource(R.layout.task_spinner_item_drop);
        completeSpinner.setAdapter(completeAdapter);

        completeSpinner.setSelection(task.getTaskState());
    }
}
