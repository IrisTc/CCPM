package com.iris.ccpm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.adapter.TaskDetailSpinnerAdapter;
import com.iris.ccpm.model.TaskModel;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.Calendar;
import java.util.Date;

public class TaskDetailActivity extends AppCompatActivity {
    private int index=0;
    private Spinner prioritySpinner;
    private Spinner completeSpinner;
    private Spinner executeSpinner;
    private TextView StartTimeView;
    private TextView EndTimeView;
    private Spinner claimerSpinner;
    private TextView taskNameView;
    private EditText predictTime;
    private EditText restTime;
    private EditText taskSynopsis;
    private TaskModel data=new TaskModel();


    private int startYear,endYear,startMonth,endMonth,startDay,endDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        taskNameView=(TextView)findViewById(R.id.TaskName);
        prioritySpinner=(Spinner)findViewById(R.id.prioritySpinner);
        completeSpinner=(Spinner)findViewById(R.id.completeSpinner);
        executeSpinner=(Spinner)findViewById(R.id.ExecuteState);
        StartTimeView=(TextView)findViewById(R.id.StartTime);
        EndTimeView=(TextView)findViewById(R.id.EndTime);
        claimerSpinner=(Spinner)findViewById(R.id.claimerSpinner);
        predictTime=(EditText)findViewById(R.id.predictEdit);
        restTime=(EditText)findViewById(R.id.remainEdit);
        taskSynopsis=(EditText)findViewById(R.id.taskSynopsis);

        startYear=startMonth=startDay=endYear=endMonth=endDay=-1;

        getData();

        InitSpinner();
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

    public void getData(){
        Request.clientGet(TaskDetailActivity.this, "task", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list=result.getJSONArray("list");
                JSONObject obj=list.getJSONObject(index);
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
//                String logStr=data.claimState+data.claim_uid+data.project_uid+data.taskEmergent+data.taskEndTime+data.taskName+data.taskPredictHours+data.taskRestHours+data.taskStartTime+data.taskStartTime+data.taskState+data.taskSynopsis+data.task_uid;
//                Log.d("TaskJson",logStr);
                LoadData();
            }

            @Override
            public void onMyFailure(String error) {
                Log.d("jsonError",error);
                Toast.makeText(TaskDetailActivity.this, error, Toast.LENGTH_LONG).show();
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

        String[] ClaimerItems = {"成员1","成员2","成员3","成员4","成员5"};
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
        String[] startStr=data.getTaskStartTime().split("-");
        startYear=Integer.parseInt(startStr[0]);
        startMonth=Integer.parseInt(startStr[1]);
        startDay=Integer.parseInt(startStr[2]);
        String[] endStr=data.getTaskEndTime().split("-");
        endYear=Integer.parseInt(endStr[0]);
        endMonth=Integer.parseInt(endStr[1]);
        endDay=Integer.parseInt(endStr[2]);
        taskNameView.setText(data.getTaskName());
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