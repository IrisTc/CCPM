package com.iris.ccpm.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iris.ccpm.R;
import com.iris.ccpm.model.Dynamic;

import java.util.List;

public class DynamicAdapter extends BaseAdapter {
    Context context;
    List<Dynamic> dynamicList;

    public DynamicAdapter(Context context, List<Dynamic> dynamicList) {
        this.context = context;
        this.dynamicList = dynamicList;
    }

    @Override
    public int getCount() {
        return this.dynamicList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.dynamicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.news_item_layout, parent, false);
        TextView tvProject = view.findViewById(R.id.tv_projectName);
        TextView tvDynamicTime = view.findViewById(R.id.lv_dynamicTime);
        TextView tvEvent = view.findViewById(R.id.tv_event);
        TextView tvAccountName = view.findViewById(R.id.tv_accountName);
        TextView tvTaskName = view.findViewById(R.id.tv_taskName);

        Dynamic dynamic = this.dynamicList.get(position);

        int eventID = dynamic.getEventID();
        String event = "";
        switch (eventID){
            case 0: event = "[拒绝认领任务]"; break;
            case 1: event = "[接受了任务]"; break;
            case 3: event = "[完成了任务]"; break;
            case 4: event = "[汇报了任务]"; break;
            case 5: event = "[被指派了任务]"; break;
            case 100:   event = "[创建了项目]"; break;
            case 300:   event = "[加入了项目]"; break;
            case 301:   event = "[退出了项目]"; break;
            case 302:   event = "[被移出了项目]"; break;
        }

        tvProject.setText(dynamic.getProjectName());
        tvDynamicTime.setText(dynamic.getDynamicTime());
        tvAccountName.setText(dynamic.getAccountNickName());
        if (!TextUtils.isEmpty(dynamic.getTaskName())) {
            tvTaskName.setText(dynamic.getTaskName());
        }
        tvEvent.setText(event);

        return view;
    }
}
