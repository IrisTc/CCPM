package com.iris.ccpm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.entity.StringEntity;

import static com.iris.ccpm.LoginActivity.isEmail;

public class AddMemberActivity extends AppCompatActivity {
    private EditText et_username;
    private Button submit;
    private String project_id;
    private String username;
    Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Intent intent = this.getIntent();
        project = (Project) intent.getSerializableExtra("project");
        project_id = project.getProject_uid();
        System.out.println(project_id);

        initView();

        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String username = et_username.getText().toString();
                    if (!isEmail(username)) {
                        Toast.makeText(AddMemberActivity.this, "邮箱格式不正确！", Toast.LENGTH_LONG).show();
                        et_username.setText("");
                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = et_username.getText().toString();
                System.out.println(username);
                if(username.equals("")){
                    Toast.makeText(AddMemberActivity.this, "请输入用户账号！", Toast.LENGTH_LONG).show();
                }else{
                    if (!isEmail(username)) {
                        Toast.makeText(AddMemberActivity.this, "邮箱格式不正确！", Toast.LENGTH_LONG).show();
                        et_username.setText("");
                    }else{
                        JSONObject body = new JSONObject();
                        StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                        Request.clientPost(AddMemberActivity.this, "project/" + project_id + "/invite/" + username, entity, new NetCallBack() {
                            @Override
                            public void onMySuccess(JSONObject result) {
                                System.out.println(result);
                                Toast.makeText(getApplicationContext(), "创建成功", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onMyFailure(String error) {
                                System.out.println(error);
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            }
        });
    }

    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        submit = (Button) findViewById(R.id.submit);
    }

    public static boolean isEmail(String strEmail) {
        String strPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)" + "|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }
}