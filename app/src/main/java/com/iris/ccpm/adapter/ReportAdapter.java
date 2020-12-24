package com.iris.ccpm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iris.ccpm.R;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.model.Report;
import com.iris.ccpm.model.TaskModel;

import java.util.List;

public class ReportAdapter extends BaseAdapter {
    Context context;
    List<Report> reportList;

    public ReportAdapter(Context context, List<Report> reportList) {
        this.context = context;
        this.reportList = reportList;
    }


    @Override
    public int getCount() {
        return this.reportList.size();
    }

    @Override
    public Report getItem(int position) {
        return this.reportList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.report_item_layout,parent,false);

            viewHolder.tvContent = convertView.findViewById(R.id.tv_content);
            viewHolder.tvNickName = convertView.findViewById(R.id.tv_nickname);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_time);
            viewHolder.tvWorkingTime = convertView.findViewById(R.id.tv_workTime);
            viewHolder.tvRestTime = convertView.findViewById(R.id.tv_restTime);
            viewHolder.tvState = convertView.findViewById(R.id.tv_state);

            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Report report = getItem(position);
        viewHolder.tvNickName.setText(report.getAccountNickName());
        viewHolder.tvContent.setText(report.getWorkingContent());
        viewHolder.tvTime.setText(report.getReportTime());
        viewHolder.tvWorkingTime.setText(report.getWorkingTime());
        viewHolder.tvRestTime.setText(report.getRestTime());

        Integer state = report.getTaskState();
        if (state == 1) {
            viewHolder.tvState.setText("已完成");
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvContent;
        TextView tvTime;
        TextView tvNickName;
        TextView tvRestTime;
        TextView tvWorkingTime;
        TextView tvState;
    }
}