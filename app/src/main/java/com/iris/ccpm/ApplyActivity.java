package com.iris.ccpm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.adapter.ProjectAdapter;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;

public class ApplyActivity extends AppCompatActivity {
    private ImageView project_avatar;
    private TextView title,date,tv_rate;
    private ProgressBar pb_rate;
    private LinearLayout project_item;
    private EditText et_search;
    private TextView bt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        project_avatar=(ImageView)findViewById(R.id.project_avatar);
        project_item=(LinearLayout)findViewById(R.id.project_item);
        title=(TextView)findViewById(R.id.title);
        date=(TextView)findViewById(R.id.date);
        pb_rate=(ProgressBar)findViewById(R.id.pb_rate);
        tv_rate=(TextView)findViewById(R.id.tv_rate);
        et_search=(EditText)findViewById(R.id.et_search);
        bt_search=(TextView)findViewById(R.id.bt_search);
        project_item.setVisibility(View.INVISIBLE);

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=et_search.getText().toString();
                if(et_search.length()==0){
                    Toast.makeText(ApplyActivity.this,"请输入项目uid!",Toast.LENGTH_LONG).show();
                    return;
                }
                Request.clientGet(ApplyActivity.this, "project/"+uid, new NetCallBack() {
                    @Override
                    public void onMySuccess(JSONObject result) {
                        String liststring = JSONObject.toJSONString(result);
                        System.out.println(liststring);
                        title.setText(result.getString("projectName"));
                        date.setText(result.getString("projectStartTime"));
                        pb_rate.setProgress(result.getInteger("projectRate"));
                        tv_rate.setText(result.getInteger("projectRate").toString()+"%");
                        project_item.setVisibility(View.VISIBLE);
                        project_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myShowDialog(result.getString("projectName"),uid);
                            }
                        });
                    }

                    @Override
                    public void onMyFailure(String error) {
                        Toast.makeText(ApplyActivity.this,error,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void myShowDialog(String name,String uid){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(ApplyActivity.this);
        normalDialog.setTitle("加入项目\""+name+"\"");
        normalDialog.setMessage("确定要申请加入此项目吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Request.clientPost(ApplyActivity.this,"project/"+uid+"/apply",new StringEntity("\"project_id\":\""+uid+"\"","UTF-8"),new NetCallBack(){
                            @Override
                            public void onMySuccess(JSONObject result) {
                                Toast.makeText(ApplyActivity.this,"申请成功!",Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onMyFailure(String error) {
                                Toast.makeText(ApplyActivity.this,error,Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        normalDialog.show();
    }
}