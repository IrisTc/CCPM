package com.iris.ccpm.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.app.DatePickerDialog.OnDateSetListener;
import java.util.Calendar;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.MainActivity;
import com.iris.ccpm.R;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import cz.msebera.android.httpclient.entity.StringEntity;

public class ProjectCreateActivity extends AppCompatActivity {

    private EditText et_project_name;
    private TextView date_picker;
    private EditText et_project_plan;
    private EditText et_project_synopsis;
    private Button create_project;


    private Calendar calendar;  //显示当前日期
    private int year;
    private int month;
    private int day;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getDate();  //获取当前日期
        initTabContent();

        create_project.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String start_time = year+"-"+month+"-"+day+"-";
                String project_name = et_project_name.getText().toString();
                String end_date = date_picker.getText().toString();
                String project_plan = et_project_plan.getText().toString();
                String project_synopsis = et_project_synopsis.getText().toString();

                if(project_name.equals("") || end_date.equals("")){
                    Toast.makeText(ProjectCreateActivity.this,"请填写完整信息", Toast.LENGTH_LONG).show();
                }
                else{
                    JSONObject body = new JSONObject();
                    body.put("projectName", project_name);
                    body.put("projectStartTime",start_time);
                    body.put("projectEndTime", end_date);
                    body.put("projectPlan", project_plan);
                    body.put("projectSynopsis", project_synopsis);
                    StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                    Request.clientPost(ProjectCreateActivity.this, "project", entity, new NetCallBack(){

                        @Override
                        public void onMySuccess(JSONObject result) {
                            Toast.makeText(ProjectCreateActivity.this, "创建成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ProjectCreateActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onMyFailure(String error) {
                            Toast.makeText(ProjectCreateActivity.this, "创建失败", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        date_picker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnDateSetListener listener= new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int selected_year, int selected_month, int selected_day) {
                        date_picker.setText(selected_year+"-"+(++selected_month)+"-"+selected_day);//将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                        System.out.println(selected_year+"年"+selected_month+"月"+selected_day+"日");
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(ProjectCreateActivity.this, 0,listener,year,month-1,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();
            }
        });
    }

    private void initTabContent() {
        et_project_name = (EditText) findViewById(R.id.et_project_name);
        date_picker = (TextView) findViewById(R.id.date_picker);
        et_project_plan = (EditText) findViewById(R.id.et_project_plan);
        et_project_synopsis = (EditText) findViewById(R.id.et_project_synopsis);
        create_project = (Button) findViewById(R.id.create_project);
    }

    private void getDate() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);       //获取年月日时分秒
        month = calendar.get(Calendar.MONTH)+1;   //获取到的月份是从0开始计数
        day = calendar.get(Calendar.DAY_OF_MONTH);
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