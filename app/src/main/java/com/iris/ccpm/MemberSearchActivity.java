package com.iris.ccpm;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iris.ccpm.model.Member;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

public class MemberSearchActivity extends AppCompatActivity {
    EditText etSearch;
    TextView btSearch;
    LinearLayout ly;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_search);

        etSearch = findViewById(R.id.et_search);
        btSearch = findViewById(R.id.bt_search);
        ly = findViewById(R.id.ly_find);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etSearch.getText().toString();
                Request.clientGet(MemberSearchActivity.this, "/account/" + content, new NetCallBack() {
                    @Override
                    public void onMySuccess(JSONObject result) {
                        System.out.println(result);
                        TextView tvName =findViewById(R.id.member_name);
                        TextView tvEmail = findViewById(R.id.member_email);
                        TextView tvPosition = findViewById(R.id.member_position);
                        SimpleDraweeView ivAvatar = findViewById(R.id.member_avatar);
                        ImageView btShow = findViewById(R.id.member_personal_button);

                        Member member = JSON.toJavaObject(result, Member.class);

                        tvName.setText(member.getName());
                        tvEmail.setText(member.getPhoneNum());
                        tvPosition.setText(member.getPosition());
                        Uri uri = Uri.parse(member.getAvatarUrl());
                        ivAvatar.setImageURI(uri);

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

                                    }
                                });
                            }
                        });

                        ly.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onMyFailure(String error) {
                        Toast.makeText(MemberSearchActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
