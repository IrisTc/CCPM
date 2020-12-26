package com.iris.ccpm.activity.taskCreate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.R;
import com.iris.ccpm.adapter.TaskDetailSpinnerAdapter;
import com.iris.ccpm.model.Member;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;

public class TaskCreateActivity extends AppCompatActivity {
    private Spinner prioritySpinner;
    private Spinner completeSpinner;
    private TextView tvExe;
    private Button saveBtn;
    private EditText taskName;
    private TextView StartTimeView;
    private TextView EndTimeView;
    private Spinner claimerSpinner;
    private EditText predictTime;
    private EditText restTime;
    private EditText taskSynopsis;
    private String[] claimers;
    private List<Integer> claimers_uid = new ArrayList<>(0);
    Integer manager_id;
    TaskCreateViewModel taskCreateViewModel;

    private int startYear, endYear, startMonth, endMonth, startDay, endDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager_id = getIntent().getIntExtra("manager_id", -1);

        taskCreateViewModel = new ViewModelProvider(this).get(TaskCreateViewModel.class);
        taskCreateViewModel.getMemberList().observe(this, new Observer<List<Member>>() {
            @Override
            public void onChanged(List<Member> members) {
                claimers = new String[members.size() - 1];
                Iterator<Member> iterator = members.iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    Member member = iterator.next();
                    Integer account_id = member.getAccount_uid();
                    if (account_id != manager_id) {
                        claimers_uid.add(account_id);
                        claimers[i] = member.getNickName();
                        i++;
                    }
                }
                System.out.println(claimers.length);
                InitSpinner();
            }
        });

        findView();

        startYear = startMonth = startDay = endYear = endMonth = endDay = -1;
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isCreate = getIntent().getBooleanExtra("isCreate", true);
                String project_id = getIntent().getStringExtra("project_id");
                if (isCreate) {
                    JSONObject obj = new JSONObject();
                    if (Integer.parseInt(String.valueOf(restTime.getText())) < 0 || Integer.parseInt(String.valueOf(predictTime.getText())) <= 0) {
                        Toast.makeText(TaskCreateActivity.this, "预估/剩余工时不得小于0", Toast.LENGTH_LONG).show();
                        return;
                    }
                    //obj.put("claimState", executeSpinner.getSelectedItemPosition());
                    obj.put("claim_uid", claimers_uid.get(claimerSpinner.getSelectedItemPosition()));
                    obj.put("project_uid", project_id);
                    obj.put("taskEmergent", prioritySpinner.getSelectedItemPosition());
                    obj.put("taskEndTime", endYear + "-" + endMonth + "-" + endDay);
                    obj.put("taskName", taskName.getText());
                    obj.put("taskPredictHours", predictTime.getText());
                    obj.put("taskRestHours", restTime.getText());
                    obj.put("taskStartTime", startYear + "-" + startMonth + "-" + startDay);
                    obj.put("taskState", completeSpinner.getSelectedItemPosition());
                    obj.put("taskSynopsis", taskSynopsis.getText());
                    System.out.println(obj.toJSONString());
                    StringEntity entity = new StringEntity(obj.toJSONString(), "UTF-8");
                    Request.clientPost(TaskCreateActivity.this, "project/" + project_id + "/task", entity, new NetCallBack() {
                        @Override
                        public void onMySuccess(JSONObject result) {
                            Toast.makeText(TaskCreateActivity.this, "创建成功", Toast.LENGTH_LONG).show();
                            TaskCreateActivity.this.finish();
                        }

                        @Override
                        public void onMyFailure(String error) {
                            System.out.println(error);
                            Toast.makeText(TaskCreateActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        StartTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(TaskCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String st;
                        month++;
                        st = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
                        startYear = year;
                        startMonth = month;
                        startDay = day;
                        if (endDay == -1 && endMonth == -1 && endYear == -1) {
                            StartTimeView.setText(st);
                            return;
                        }
                        if (startYear > endYear) {
                            Toast.makeText(TaskCreateActivity.this, "请设置小于结束时间的日期", Toast.LENGTH_LONG).show();
                        } else if (startYear == endYear) {
                            if (startMonth > endMonth) {
                                Toast.makeText(TaskCreateActivity.this, "请设置小于结束时间的日期", Toast.LENGTH_LONG).show();
                            } else if (startMonth == endMonth) {
                                if (startDay >= endDay)
                                    Toast.makeText(TaskCreateActivity.this, "请设置小于结束时间的日期", Toast.LENGTH_LONG).show();
                                else StartTimeView.setText(st);
                            } else StartTimeView.setText(st);
                        } else StartTimeView.setText(st);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        EndTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(TaskCreateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String st;
                        month++;
                        st = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
                        endYear = year;
                        endMonth = month;
                        endDay = day;
                        if (startDay == -1 && startMonth == -1 && startYear == -1) {
                            EndTimeView.setText(st);
                            return;
                        }
                        if (startYear > endYear) {
                            Toast.makeText(TaskCreateActivity.this, "请设置大于开始时间的日期", Toast.LENGTH_LONG).show();
                        } else if (startYear == endYear) {
                            if (startMonth > endMonth) {
                                Toast.makeText(TaskCreateActivity.this, "请设置大于开始时间的日期", Toast.LENGTH_LONG).show();
                            } else if (startMonth == endMonth) {
                                if (startDay >= endDay)
                                    Toast.makeText(TaskCreateActivity.this, "请设置大于开始时间的日期", Toast.LENGTH_LONG).show();
                                else EndTimeView.setText(st);
                            } else EndTimeView.setText(st);
                        } else EndTimeView.setText(st);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
    }

    private void findView() {
        taskName = (EditText) findViewById(R.id.TaskName);
        prioritySpinner = (Spinner) findViewById(R.id.prioritySpinner);
        completeSpinner = (Spinner) findViewById(R.id.completeSpinner);
        tvExe = (TextView) findViewById(R.id.ExecuteState);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        StartTimeView = (TextView) findViewById(R.id.StartTime);
        EndTimeView = (TextView) findViewById(R.id.EndTime);
        claimerSpinner = (Spinner) findViewById(R.id.claimerSpinner);
        predictTime = (EditText) findViewById(R.id.predictEdit);
        restTime = (EditText) findViewById(R.id.remainEdit);
        taskSynopsis = (EditText) findViewById(R.id.taskSynopsis);
    }

    public void InitSpinner() {
        prioritySpinner.setDropDownWidth(400);
        prioritySpinner.setDropDownHorizontalOffset(100);
        prioritySpinner.setDropDownVerticalOffset(100);

        completeSpinner.setDropDownWidth(400);
        completeSpinner.setDropDownHorizontalOffset(100);
        completeSpinner.setDropDownVerticalOffset(100);

        claimerSpinner.setDropDownWidth(400);
        claimerSpinner.setDropDownHorizontalOffset(100);
        claimerSpinner.setDropDownVerticalOffset(100);

        String[] spinnerItems = {"普通", "紧急", "非常紧急"};
        int[] prioColors = {0xFF7FFFAA, 0xFFFF7F50, Color.RED};
        int[] prioTextColors = {Color.BLACK, Color.WHITE, Color.WHITE};

        String[] completeItems = {"未完成", "已完成"};
        int[] completeColors = {Color.RED, 0xFF7FFFAA};
        int[] completeTextColors = {Color.WHITE, Color.BLACK};

        String[] ClaimerItems = claimers;
        int[] ClaimerColors = {Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT};
        int[] ClaimerTextColors = {Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};

        TaskDetailSpinnerAdapter completeAdapter = new TaskDetailSpinnerAdapter(this, completeItems, completeColors, completeTextColors);
        completeAdapter.setDropDownViewResource(R.layout.task_spinner_item_drop);
        completeSpinner.setAdapter(completeAdapter);

        TaskDetailSpinnerAdapter prioAdapter = new TaskDetailSpinnerAdapter(this, spinnerItems, prioColors, prioTextColors);
        prioAdapter.setDropDownViewResource(R.layout.task_spinner_item_drop);
        prioritySpinner.setAdapter(prioAdapter);

        TaskDetailSpinnerAdapter ClaimerAdapter = new TaskDetailSpinnerAdapter(this, ClaimerItems, ClaimerColors, ClaimerTextColors);
        ClaimerAdapter.setDropDownViewResource(R.layout.task_spinner_item_drop);
        claimerSpinner.setAdapter(ClaimerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}