package com.iris.ccpm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.adapter.TaskDetailSpinnerAdapter;
import com.iris.ccpm.model.GlobalData;
import com.iris.ccpm.model.TaskModel;
import com.iris.ccpm.ui.project.ProjectFragment;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bolts.Task;
import cz.msebera.android.httpclient.entity.StringEntity;

public class TaskDetailActivity extends AppCompatActivity {
    private Spinner prioritySpinner;
    private Spinner completeSpinner;
    private Spinner executeSpinner;
    private Button saveBtn;
    private EditText taskName;
    private TextView StartTimeView;
    private TextView EndTimeView;
    private Spinner claimerSpinner;
    private EditText predictTime;
    private EditText restTime;
    private EditText taskSynopsis;
    private String[] claimers;
    private List<Integer> claimers_uid=new ArrayList<>(0);
    private TaskModel data=new TaskModel();
    Boolean isManager = false;
    Boolean isClaimer = false;

    private int startYear,endYear,startMonth,endMonth,startDay,endDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        taskName=(EditText) findViewById(R.id.TaskName);
        prioritySpinner=(Spinner)findViewById(R.id.prioritySpinner);
        completeSpinner=(Spinner)findViewById(R.id.completeSpinner);
        executeSpinner=(Spinner)findViewById(R.id.ExecuteState);
        saveBtn=(Button)findViewById(R.id.saveBtn);
        StartTimeView=(TextView)findViewById(R.id.StartTime);
        EndTimeView=(TextView)findViewById(R.id.EndTime);
        claimerSpinner=(Spinner)findViewById(R.id.claimerSpinner);
        predictTime=(EditText)findViewById(R.id.predictEdit);
        restTime=(EditText)findViewById(R.id.remainEdit);
        taskSynopsis=(EditText)findViewById(R.id.taskSynopsis);

        startYear=startMonth=startDay=endYear=endMonth=endDay=-1;

        LoadData();

        if (isManager) {
            saveBtn.setVisibility(View.VISIBLE);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean isCreate = getIntent().getBooleanExtra("isCreate", true);
                    String project_id = getIntent().getStringExtra("project_id");
                    if (isCreate) {
                        JSONObject obj = new JSONObject();
                        if (Integer.parseInt(String.valueOf(restTime.getText())) < 0 || Integer.parseInt(String.valueOf(predictTime.getText())) <= 0) {
                            Toast.makeText(TaskDetailActivity.this, "预估/剩余工时不得小于0", Toast.LENGTH_LONG).show();
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
                        Request.clientPost(TaskDetailActivity.this, "project/" + project_id + "/task", entity, new NetCallBack() {
                            @Override
                            public void onMySuccess(JSONObject result) {
                                Toast.makeText(TaskDetailActivity.this, "创建成功", Toast.LENGTH_LONG).show();
                                TaskDetailActivity.this.finish();
                            }

                            @Override
                            public void onMyFailure(String error) {
                                System.out.println(error);
                                Toast.makeText(TaskDetailActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {

                    }
                }
            });
        }

        if (isClaimer) {
            TextView reportBtn = findViewById(R.id.reportBtn);
            reportBtn.setVisibility(View.VISIBLE);
        }

        StartTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                DatePickerDialog dpd=new DatePickerDialog(TaskDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String st;
                        month++;
                        st = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
                        startYear=year;
                        startMonth=month;
                        startDay=day;
                        if(endDay==-1&&endMonth==-1&&endYear==-1){
                            StartTimeView.setText(st);
                            return;
                        }
                        if(startYear>endYear){
                            Toast.makeText(TaskDetailActivity.this,"请设置小于结束时间的日期",Toast.LENGTH_LONG).show();
                        }else if(startYear==endYear){
                            if(startMonth>endMonth){
                                Toast.makeText(TaskDetailActivity.this,"请设置小于结束时间的日期",Toast.LENGTH_LONG).show();
                            }else if(startMonth==endMonth){
                                if(startDay>=endDay) Toast.makeText(TaskDetailActivity.this,"请设置小于结束时间的日期",Toast.LENGTH_LONG).show();
                                else StartTimeView.setText(st);
                            }else StartTimeView.setText(st);
                        }else StartTimeView.setText(st);
                    }
                },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });

        EndTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                DatePickerDialog dpd=new DatePickerDialog(TaskDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String st;
                        month++;
                        st = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
                        endYear=year;
                        endMonth=month;
                        endDay=day;
                        if(startDay==-1&&startMonth==-1&&startYear==-1){
                            EndTimeView.setText(st);
                            return;
                        }
                        if(startYear>endYear){
                            Toast.makeText(TaskDetailActivity.this,"请设置大于开始时间的日期",Toast.LENGTH_LONG).show();
                        }else if(startYear==endYear){
                            if(startMonth>endMonth){
                                Toast.makeText(TaskDetailActivity.this,"请设置大于开始时间的日期",Toast.LENGTH_LONG).show();
                            }else if(startMonth==endMonth){
                                if(startDay>=endDay) Toast.makeText(TaskDetailActivity.this,"请设置大于开始时间的日期",Toast.LENGTH_LONG).show();
                                else EndTimeView.setText(st);
                            }else EndTimeView.setText(st);
                        }else EndTimeView.setText(st);
                    }
                },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
    }

    public void InitSpinner(){
        prioritySpinner.setDropDownWidth(400);
        prioritySpinner.setDropDownHorizontalOffset(100);
        prioritySpinner.setDropDownVerticalOffset(100);

        completeSpinner.setDropDownWidth(400);
        completeSpinner.setDropDownHorizontalOffset(100);
        completeSpinner.setDropDownVerticalOffset(100);

        executeSpinner.setDropDownWidth(400);
        executeSpinner.setDropDownHorizontalOffset(100);
        executeSpinner.setDropDownVerticalOffset(100);

        claimerSpinner.setDropDownWidth(400);
        claimerSpinner.setDropDownHorizontalOffset(100);
        claimerSpinner.setDropDownVerticalOffset(100);

        String[] spinnerItems = {"普通","紧急","非常紧急"};
        int[] prioColors={0xFF7FFFAA,0xFFFF7F50,Color.RED};
        int[] prioTextColors={Color.BLACK,Color.WHITE,Color.WHITE};

        String[] completeItems = {"未完成","已完成"};
        int[] completeColors={Color.RED,0xFF7FFFAA};
        int[] completeTextColors={Color.WHITE,Color.BLACK};

        String[] exeItems = {"拒绝","接受","未处理"};
        int[] exeColors={Color.TRANSPARENT,Color.TRANSPARENT,Color.TRANSPARENT};
        int[] exeTextColors={Color.BLACK,Color.BLACK,Color.BLACK};

        String[] ClaimerItems = claimers;
        int[] ClaimerColors={Color.TRANSPARENT,Color.TRANSPARENT,Color.TRANSPARENT,Color.TRANSPARENT,Color.TRANSPARENT};
        int[] ClaimerTextColors={Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK};

        TaskDetailSpinnerAdapter completeAdapter = new TaskDetailSpinnerAdapter(this,completeItems,completeColors,completeTextColors);
        completeAdapter.setDropDownViewResource(R.layout.task_spinner_item_drop);
        completeSpinner.setAdapter(completeAdapter);

        TaskDetailSpinnerAdapter prioAdapter = new TaskDetailSpinnerAdapter(this,spinnerItems,prioColors,prioTextColors);
        prioAdapter.setDropDownViewResource(R.layout.task_spinner_item_drop);
        prioritySpinner.setAdapter(prioAdapter);

        TaskDetailSpinnerAdapter exeAdapter = new TaskDetailSpinnerAdapter(this,exeItems,exeColors,exeTextColors);
        exeAdapter.setDropDownViewResource(R.layout.task_spinner_item_drop);
        executeSpinner.setAdapter(exeAdapter);

        TaskDetailSpinnerAdapter ClaimerAdapter = new TaskDetailSpinnerAdapter(this,ClaimerItems,ClaimerColors,ClaimerTextColors);
        ClaimerAdapter.setDropDownViewResource(R.layout.task_spinner_item_drop);
        claimerSpinner.setAdapter(ClaimerAdapter);
    }

    public void LoadData(){
        TaskModel task=(TaskModel) getIntent().getSerializableExtra("task");
        Boolean isCreate = getIntent().getBooleanExtra("isCreate",true);
        isManager = getIntent().getBooleanExtra("isManager",false);
        GlobalData app = (GlobalData) getApplication();
        System.out.println(task.getClaim_uid());
        System.out.println(app.getUid());
        if (task.getClaim_uid() == app.getUid()) {
            isClaimer = true;
        }
        TextView saveBtn = findViewById(R.id.saveBtn);
        if(isCreate){
            executeSpinner.setClickable(false);
            executeSpinner.setEnabled(false);
            saveBtn.setText("发布");
        }
        String project_id = task.getProject_uid();
        data=task;
        //System.out.println(data.getProject_uid());
        Request.clientGet(TaskDetailActivity.this, "project/"+project_id+"/member", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list=result.getJSONArray("list");
                claimers=new String[list.size()];
                for(int i=0;i<list.size();++i) {
                    claimers[i]=list.getJSONObject(i).getString("nickName");
                    claimers_uid.add(list.getJSONObject(i).getInteger("account_uid"));
                    System.out.println(claimers[i]);
                    InitSpinner();
                }
            }

            @Override
            public void onMyFailure(String error) {

            }
        });
        String[] startStr=data.getTaskStartTime().split("-");
        startYear=Integer.parseInt(startStr[0]);
        startMonth=Integer.parseInt(startStr[1]);
        startDay=Integer.parseInt(startStr[2]);
        String[] endStr=data.getTaskEndTime().split("-");
        endYear=Integer.parseInt(endStr[0]);
        endMonth=Integer.parseInt(endStr[1]);
        endDay=Integer.parseInt(endStr[2]);
        taskName.setText(data.getTaskName());
        completeSpinner.setSelection(data.getTaskState());
        executeSpinner.setSelection(data.getClaimState());
        prioritySpinner.setSelection(data.getTaskEmergent());
        restTime.setText(data.getTaskRestHours());
        predictTime.setText(data.getTaskPredictHours());
        taskSynopsis.setText(data.getTaskSynopsis());
        StartTimeView.setText(data.getTaskStartTime());
        EndTimeView.setText(data.getTaskEndTime());
    }

}