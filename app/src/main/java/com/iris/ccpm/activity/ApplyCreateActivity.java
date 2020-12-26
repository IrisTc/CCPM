package com.iris.ccpm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.R;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import cz.msebera.android.httpclient.entity.StringEntity;

public class ApplyCreateActivity extends AppCompatActivity {
    private TextView title, date, tv_rate;
    private ProgressBar pb_rate;
    private LinearLayout project_item;
    private EditText et_search;
    private TextView bt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findView();

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = et_search.getText().toString();
                if (et_search.length() == 0) {
                    Toast.makeText(ApplyCreateActivity.this, "请输入项目唯一标识码!", Toast.LENGTH_LONG).show();
                    return;
                }
                Request.clientGet("project/" + uid, new NetCallBack() {
                    @Override
                    public void onMySuccess(JSONObject result) {
                        title.setText(result.getString("projectName"));
                        date.setText(result.getString("projectStartTime"));
                        pb_rate.setProgress(result.getInteger("projectRate"));
                        tv_rate.setText(result.getInteger("projectRate").toString() + "%");
                        project_item.setVisibility(View.VISIBLE);
                        project_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myShowDialog(result.getString("projectName"), uid);
                            }
                        });
                    }

                    @Override
                    public void onMyFailure(String error) {
                        Toast.makeText(ApplyCreateActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void findView() {
        project_item = (LinearLayout) findViewById(R.id.project_item);
        title = (TextView) findViewById(R.id.title);
        date = (TextView) findViewById(R.id.date);
        pb_rate = (ProgressBar) findViewById(R.id.pb_rate);
        tv_rate = (TextView) findViewById(R.id.tv_rate);
        et_search = (EditText) findViewById(R.id.et_search);
        bt_search = (TextView) findViewById(R.id.bt_search);
        project_item.setVisibility(View.INVISIBLE);
    }

    private void myShowDialog(String name, String uid) {
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(ApplyCreateActivity.this);
        normalDialog.setTitle("加入项目\"" + name + "\"");
        normalDialog.setMessage("确定要申请加入此项目吗?");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request.clientPost(ApplyCreateActivity.this, "project/" + uid + "/apply", new StringEntity("\"project_id\":\"" + uid + "\"", "UTF-8"), new NetCallBack() {
                    @Override
                    public void onMySuccess(JSONObject result) {
                        Toast.makeText(ApplyCreateActivity.this, "申请成功!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onMyFailure(String error) {
                        Toast.makeText(ApplyCreateActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        normalDialog.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        normalDialog.show();
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