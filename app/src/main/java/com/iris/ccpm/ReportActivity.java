package com.iris.ccpm;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.adapter.TaskDetailSpinnerAdapter;
import com.iris.ccpm.model.TaskModel;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;
import com.loopj.android.http.AsyncHttpClient;

import cz.msebera.android.httpclient.entity.StringEntity;

public class ReportActivity extends AppCompatActivity {
    TaskModel task;
    Spinner completeSpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_add);

        task=(TaskModel) getIntent().getSerializableExtra("task");
        System.out.println(task);

        TextView tvTaskName = findViewById(R.id.tv_taskName);
        TextView tvStart = findViewById(R.id.tv_startTime);
        TextView tvEnd = findViewById(R.id.tv_endTime);
        TextView tvMemo = findViewById(R.id.tv_memo);
        TextView tvPrio = findViewById(R.id.tv_prio);
        TextView tvPredictTime = findViewById(R.id.tv_predictTime);
        TextView tvRestTime = findViewById(R.id.tv_restTime);

        String[] spinnerItems = {"普通","紧急","非常紧急"};
        int[] prioColors={0xFF7FFFAA,0xFFFF7F50,Color.RED};
        int[] prioTextColors={Color.BLACK,Color.WHITE,Color.WHITE};

        Integer prio = task.getTaskEmergent();
        tvPrio.setText(spinnerItems[prio]);
        tvPrio.setBackgroundColor((prioColors[prio]));
        tvPrio.setTextColor(prioTextColors[prio]);

        tvTaskName.setText(task.getTaskName());
        tvStart.setText(task.getTaskStartTime());
        tvEnd.setText(task.getTaskEndTime());
        tvMemo.setText(task.getTaskSynopsis());
        tvPredictTime.setText(task.getTaskPredictHours());
        tvRestTime.setText(task.getTaskRestHours());
        initSpinner();
        completeSpinner.setSelection(task.getTaskState());

        EditText etWorkhour = findViewById(R.id.et_workhour);
        EditText etContent = findViewById(R.id.et_content);
        EditText etPreHour = findViewById(R.id.et_prehour);

        Button btReport = findViewById(R.id.bt_report);
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
                Request.clientPut(ReportActivity.this, "task/" + task.getTask_uid(), entity, new NetCallBack() {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        System.out.println(result);
                    }

                    @Override
                    public void onMyFailure(String error) {

                    }
                });
            }
        });
    }

    private void initSpinner() {
        completeSpinner = findViewById(R.id.completeSpinner);
        completeSpinner.setDropDownWidth(400);
        completeSpinner.setDropDownHorizontalOffset(100);
        completeSpinner.setDropDownVerticalOffset(100);

        String[] completeItems = {"未完成","已完成"};
        int[] completeColors={Color.RED,0xFF7FFFAA};
        int[] completeTextColors={Color.WHITE,Color.BLACK};

        TaskDetailSpinnerAdapter completeAdapter = new TaskDetailSpinnerAdapter(this,completeItems,completeColors,completeTextColors);
        completeAdapter.setDropDownViewResource(R.layout.task_spinner_item_drop);
        completeSpinner.setAdapter(completeAdapter);
    }
}
