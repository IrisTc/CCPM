package com.iris.ccpm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.iris.ccpm.model.GlobalData;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HeaderElement;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

public class CreateActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_create);

        getDate();  //获取当前日期
        initTabContent();

        create_project.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String start_time = year+"年"+month+"月"+day+"日";
                System.out.println(start_time);
                String project_name = et_project_name.getText().toString();
                System.out.println(project_name);
                String end_date = date_picker.getText().toString();
                System.out.println(end_date);
                String project_plan = et_project_plan.getText().toString();
                System.out.println(project_plan);
                String project_synopsis = et_project_synopsis.getText().toString();
                System.out.println(project_synopsis);

                if(project_name.equals("") || end_date.equals("")){
                    Toast.makeText(CreateActivity.this,"请填写完整信息", Toast.LENGTH_LONG).show();
                }
                else{
                    GlobalData app = (GlobalData) getApplicationContext();
                    AsyncHttpClient client = new AsyncHttpClient();
                    String url = "https://find-hdu.com/project";
                    JSONObject body = new JSONObject();
                    body.put("projectName", project_name);
                    body.put("projectStartTime",start_time);
                    body.put("projectEndTime", end_date);
                    body.put("projectPlan", project_plan);
                    body.put("projectSynopsis", project_synopsis);
                    StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                    client.addHeader("Token", app.getToken());
                    client.post(CreateActivity.this, url, entity, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            for (int i = 0; i < headers.length; i++) {
                                String name = headers[i].getName();
                                String value = headers[i].getValue();
                                Log.d("header", "Http request: Name—>" + name + ",Value—>" + value);
                            }
                            String content = new String(responseBody);
                            Log.d("response:", content);
                            JSONObject jsonObject = JSONObject.parseObject(content);
                            Integer code = jsonObject.getInteger("code");
                            Log.d("code:", code.toString());
                            if(code != 200){
                                Toast.makeText(CreateActivity.this, "创建失败", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(CreateActivity.this, "创建成功", Toast.LENGTH_LONG).show();
                                back();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(CreateActivity.this, "请检查网络设置", Toast.LENGTH_LONG).show();
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
                DatePickerDialog dialog=new DatePickerDialog(CreateActivity.this, 0,listener,year,month-1,day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
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

    private void back(){
        Intent intent = new Intent(this,MainActivity.class);
        this.startActivity(intent);
        finish();
    }
}