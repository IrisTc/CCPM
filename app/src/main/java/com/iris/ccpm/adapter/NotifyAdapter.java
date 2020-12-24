package com.iris.ccpm.adapter;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.R;
import com.iris.ccpm.model.GlobalData;
import com.iris.ccpm.model.Notify;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;

public class   NotifyAdapter extends BaseAdapter {
    Context context;
    List<Notify> notifyList;
    Integer my_uid;

    public NotifyAdapter(Context context, List<Notify> notifyList) {
        this.context = context;
        this.notifyList = notifyList;
    }

    @Override
    public int getCount() {
        return this.notifyList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.notifyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.notify_item_layout, parent, false);
        TextView tvContent = view.findViewById(R.id.tv_content);
        TextView tvProject = view.findViewById(R.id.tv_projectName);
        TextView tvManager = view.findViewById(R.id.tv_managerNickName);
        TextView tvTime = view.findViewById(R.id.tv_notiryTime);
        TextView tvDeal = view.findViewById(R.id.tv_deal);

        Notify notify = this.notifyList.get(position);
        int event = notify.getEventID();
        String content = "";
        switch (event){
            case 0:
                content = notify.getAccountNickName() + " 拒绝了任务 [" + notify.getTaskName() + "]";
                break;
            case 1:
                content = notify.getAccountNickName() + " 接受了任务 [" + notify.getTaskName() + "]";
                break;
            case 2:
                GlobalData app = (GlobalData) context.getApplicationContext();
                if (notify.getAccount_uid() == app.getUid()) {
                    tvDeal.setText("已处理");
                    content = "项目经理 " + notify.getManagerNickName() + " 给你指派了任务 [" + notify.getTaskName() + "]";
                    if (notify.getEventID() == 2) {
                        tvDeal.setText("需处理");
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setIcon(R.drawable.ic_warn);
                                builder.setTitle("警告");
                                builder.setMessage("任务【" + notify.getTaskName() + "】");
                                builder.setPositiveButton("接受", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        JSONObject body = new JSONObject();
                                        StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                                        Request.clientPost(context, "task/" + notify.getNotify_uid() + "/accept", entity, new NetCallBack() {
                                            @Override
                                            public void onMySuccess(JSONObject result) {
                                                System.out.println(result);
                                                Toast.makeText(context, "您已接受任务", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onMyFailure(String error) {
                                                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                                builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        JSONObject body = new JSONObject();
                                        StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                                        Request.clientPost(context, "project/" + notify.getNotify_uid() + "/reject", entity, new NetCallBack() {
                                            @Override
                                            public void onMySuccess(JSONObject result) {
                                                System.out.println(result);
                                                Toast.makeText(context, "您已拒绝任务", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onMyFailure(String error) {
                                                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });
                                builder.show();
                            }
                        });
                    }
                } else {
                    content = "你给 " + notify.getAccountNickName() +  " 指派了任务 [" + notify.getTaskName() + "]";
                }
                break;
            case 3:
                content = notify.getAccountNickName() + " 完成了任务 [" + notify.getTaskName() + "]";
                break;
        }
        tvContent.setText(content);
        tvProject.setText(notify.getProjectName());
        tvManager.setText(notify.getManagerNickName());
        tvTime.setText(notify.getNotifyTime());
        return view;
    }
}
