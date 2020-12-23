package com.iris.ccpm;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.activity.projectDetail.ProjectDetailActivity;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.Calendar;

import cz.msebera.android.httpclient.entity.StringEntity;

public class EditProjectActivity extends AppCompatActivity {
    private EditText et_project_name;
    private EditText et_rate;
    private TextView date_picker;
    private EditText et_project_plan;
    private EditText et_project_synopsis;
    private Button edit_project;
    Project project;
    private String project_id;

    private Calendar calendar;  //显示当前日期
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);

        Intent intent  = this.getIntent();
        project = (Project) intent.getSerializableExtra("project");
        project_id = project.getProject_uid();
        System.out.println(project_id);

        initTabContent();
        init_intro();
        getDate();
        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener= new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int selected_year, int selected_month, int selected_day) {
                        date_picker.setText(selected_year+"-"+(++selected_month)+"-"+selected_day);//将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                        System.out.println(selected_year+"-"+selected_month+"-"+selected_day);
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(EditProjectActivity.this, 0,listener,year,month-1,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();
            }
        });

        edit_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String project_name = et_project_name.getText().toString();
                System.out.println(project_name);
                String project_rate = et_rate.getText().toString();
                System.out.println(project_rate);
                String end_date = date_picker.getText().toString();
                System.out.println(end_date);
                String project_plan = et_project_plan.getText().toString();
                System.out.println(project_plan);
                String project_synopsis = et_project_synopsis.getText().toString();
                System.out.println(project_synopsis);
                if(project_name.equals("") || project_rate.equals("") || end_date.equals("")){
                    Toast.makeText(EditProjectActivity.this,"请填写完整信息", Toast.LENGTH_LONG).show();
                }else{
                    JSONObject body = new JSONObject();
                    body.put("projectName", project_name);
                    body.put("projectRate",project_rate);
                    body.put("projectEndTime", end_date);
                    body.put("projectPlan", project_plan);
                    body.put("projectSynopsis", project_synopsis);
                    StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                    Request.clientPut(EditProjectActivity.this, "project/" + project_id + "/update", entity, new NetCallBack() {
                        @Override
                        public void onMySuccess(JSONObject result) {
                            Toast.makeText(EditProjectActivity.this,"更新成功", Toast.LENGTH_LONG).show();
                            back();
                        }

                        @Override
                        public void onMyFailure(String error) {
                            Toast.makeText(EditProjectActivity.this,"更新失败", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }

    private void back() {
        Intent intent = new Intent(this, ProjectDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("project", project);
        intent.putExtras(bundle);
        this.startActivity(intent);
        finish();
    }

    private void init_intro() {
        et_project_name.setText(project.getProjectName());
        et_rate.setText(project.getProjectRate().toString());
        date_picker.setText(project.getProjectEndTime());
        et_project_plan.setText(project.getProjectPlan());
        et_project_synopsis.setText(project.getProjectSynopsis());
    }

    private void getDate() {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);       //获取年月日时分秒
        month = calendar.get(Calendar.MONTH)+1;   //获取到的月份是从0开始计数
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void initTabContent() {
        et_project_name = (EditText) findViewById(R.id.et_project_name);
        et_rate = (EditText) findViewById(R.id.et_rate);
        date_picker = (TextView) findViewById(R.id.date_picker);
        et_project_plan = (EditText) findViewById(R.id.et_project_plan);
        et_project_synopsis = (EditText) findViewById(R.id.et_project_synopsis);
        edit_project = (Button) findViewById(R.id.edit_project);
    }
}