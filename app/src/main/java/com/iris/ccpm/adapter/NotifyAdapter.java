package com.iris.ccpm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iris.ccpm.R;
import com.iris.ccpm.model.Notify;
import com.iris.ccpm.model.Project;

import java.util.List;

public class NotifyAdapter extends BaseAdapter {
    Context context;
    List<Notify> notifyList;

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

        Notify notify = this.notifyList.get(position);
        int event = notify.getEventID();
        String content = "";
        switch (event){
            case 0:
                content = notify.getAccountNickName() + " 拒绝了任务：" + notify.getTaskName();

            case 1:
                content = notify.getAccountNickName() + " 接受了任务：" + notify.getTaskName();
            case 2:
                content = "项目经理" + notify.getManagerNickName() +  "给你指派了任务：" + notify.getTaskName();
            case 3:
                content = notify.getAccountNickName() + " 完成了任务：" + notify.getTaskName();
        }
        tvContent.setText(content);
        tvProject.setText(notify.getProjectName());
        tvManager.setText(notify.getManagerNickName());
        tvTime.setText(notify.getNotifyTime());
        return view;
    }
}
