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
    View.OnClickListener listener;

    public NotifyAdapter(Context context, List<Notify> notifyList, View.OnClickListener listener) {
        this.context = context;
        this.notifyList = notifyList;
        this.listener = listener;
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
        ViewHolder viewHolder;
//        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.notify_item_layout, parent, false);

            viewHolder.tvContent = convertView.findViewById(R.id.tv_content);
            viewHolder.tvProject = convertView.findViewById(R.id.tv_projectName);
            viewHolder.tvManager = convertView.findViewById(R.id.tv_managerNickName);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_notiryTime);
            viewHolder.tvDeal = convertView.findViewById(R.id.tv_deal);

//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }

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
                    viewHolder.tvDeal.setText("需处理");
                    content = "项目经理 " + notify.getManagerNickName() + " 给你指派了任务 [" + notify.getTaskName() + "]";
                    convertView.setOnClickListener(listener);
                    convertView.setTag(position);
                } else {
                    content = "你给 " + notify.getAccountNickName() +  " 指派了任务 [" + notify.getTaskName() + "]";
                }
                break;
            case 3:
                content = notify.getAccountNickName() + " 完成了任务 [" + notify.getTaskName() + "]";
                break;
        }

        viewHolder.tvContent.setText(content);
        viewHolder.tvProject.setText(notify.getProjectName());
        viewHolder.tvManager.setText(notify.getManagerNickName());
        viewHolder.tvTime.setText(notify.getNotifyTime());

        return convertView;
    }

    class ViewHolder {
        TextView tvContent;
        TextView tvProject;
        TextView tvManager;
        TextView tvTime;
        TextView tvDeal;
    }
}
