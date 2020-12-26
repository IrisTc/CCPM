package com.iris.ccpm.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iris.ccpm.R;
import com.iris.ccpm.model.Member;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

public class MemberDetailActivity extends AppCompatActivity {
    SimpleDraweeView ivAvatar;
    TextView tvNickname;
    TextView tvEmail;
    TextView tvUsername;
    TextView tvRealname;
    TextView tvPhonenum;
    TextView tvPosition;
    TextView tvSynopsis;
    TextView tvProjectCount;
    TextView tvTaskCount;
    TextView tvIngProject;
    TextView tvIngTask;
    Integer account_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        Intent intent = this.getIntent();
        account_id = intent.getIntExtra("account_id", 0);

        findView();
        init_intro();
    }

    private void findView() {
        tvNickname = findViewById(R.id.tv_nickname);
        tvEmail = findViewById(R.id.tv_email);
        ivAvatar = findViewById(R.id.iv_avatar);
        tvUsername = findViewById(R.id.tv_username);
        tvNickname = findViewById(R.id.tv_nickname);
        tvRealname = findViewById(R.id.tv_realname);
        tvPhonenum = findViewById(R.id.tv_phoneNum);
        tvPosition = findViewById(R.id.tv_position);
        tvSynopsis = findViewById(R.id.tv_synopsis);
        tvProjectCount = findViewById(R.id.project_count);
        tvTaskCount = findViewById(R.id.task_count);
        tvIngProject = findViewById(R.id.ingproject);
        tvIngTask = findViewById(R.id.ingtask);
    }


    private void init_intro() {
        Request.clientGet("account/" + account_id, new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                System.out.println(result);
                Member member = JSON.toJavaObject(result, Member.class);
                tvUsername.setText(member.getUsername());
                tvNickname.setText(member.getNickName());
                tvRealname.setText(member.getRealName());
                tvPhonenum.setText(member.getPhoneNum());
                tvPosition.setText(member.getPosition());
                tvSynopsis.setText(member.getSynopsis());
                tvEmail.setText(member.getUsername());
                Uri uri = Uri.parse(member.getAvatarUrl());
                ivAvatar.setImageURI(uri);
            }

            @Override
            public void onMyFailure(String error) {

            }
        });

        Request.clientGet("statistics?projectNum=yes&taskNum=yes&ingProject=yes&ingTask=yes&account_uid=" + account_id, new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                tvProjectCount.setText(result.getString("projectNum"));
                tvTaskCount.setText(result.getString("taskNum"));
                tvIngProject.setText(result.getString("ingProject"));
                tvIngTask.setText(result.getString("ingTask"));
            }

            @Override
            public void onMyFailure(String error) {

            }
        });
    }
}
