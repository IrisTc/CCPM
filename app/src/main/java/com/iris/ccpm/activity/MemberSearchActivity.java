package com.iris.ccpm.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iris.ccpm.R;
import com.iris.ccpm.model.Member;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import cz.msebera.android.httpclient.entity.StringEntity;

public class MemberSearchActivity extends AppCompatActivity {
    EditText etSearch;
    TextView btSearch;
    LinearLayout ly;
    String project_id;
    TextView tvName;
    TextView tvEmail;
    TextView tvPosition;
    SimpleDraweeView ivAvatar;
    ImageButton btShow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();
        project_id = intent.getStringExtra("project_id");
        
        findView();

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etSearch.getText().toString();
                Request.clientGet( "/account/" + content, new NetCallBack() {
                    @Override
                    public void onMySuccess(JSONObject result) {
                        Member member = JSON.toJavaObject(result, Member.class);

                        tvName.setText(member.getName());
                        tvEmail.setText(member.getUsername());
                        tvPosition.setText(member.getPosition());
                        Uri uri = Uri.parse(member.getAvatarUrl());
                        ivAvatar.setImageURI(uri);

                        ly.setVisibility(View.VISIBLE);
                        btShow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MemberSearchActivity.this);
                                builder.setIcon(R.drawable.ic_warn);
                                builder.setTitle("警告");
                                builder.setMessage("确定邀请该成员【" + member.getNickName() + "】吗？");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        JSONObject body = new JSONObject();
                                        StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                                        Request.clientPost(MemberSearchActivity.this, "project/" + project_id + "/invite/" + member.getUsername(), entity, new NetCallBack() {
                                            @Override
                                            public void onMySuccess(JSONObject result) {
                                                Toast.makeText(MemberSearchActivity.this, "邀请成功", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onMyFailure(String error) {
                                                Toast.makeText(MemberSearchActivity.this, error, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @Override
                    public void onMyFailure(String error) {
                        Toast.makeText(MemberSearchActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void findView() {
        etSearch = findViewById(R.id.et_search);
        btSearch = findViewById(R.id.bt_search);
        ly = findViewById(R.id.ly_find);
        tvName =findViewById(R.id.member_name);
        tvEmail = findViewById(R.id.member_email);
        tvPosition = findViewById(R.id.member_position);
        ivAvatar = findViewById(R.id.member_avatar);
        btShow = findViewById(R.id.member_personal_button);
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
