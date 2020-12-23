package com.iris.ccpm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iris.ccpm.R;
import com.iris.ccpm.model.Project;

import java.util.List;

public class ProjectAdapter extends BaseAdapter {
    Context context;
    List<Project> projectList;

    public ProjectAdapter(Context context, List<Project> projectList) {
        this.context = context;
        this.projectList = projectList;
    }

    @Override
    public int getCount() {
        return this.projectList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.projectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.project_item_layout, parent, false);

            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.title);
            viewHolder.tvDate = convertView.findViewById(R.id.date);
            viewHolder.pbRate = convertView.findViewById(R.id.pb_rate);
            viewHolder.tvRate = convertView.findViewById(R.id.tv_rate);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Project project = this.projectList.get(position);
        viewHolder.tvTitle.setText(project.getProjectName());
        viewHolder.tvDate.setText(project.getProjectStartTime() );
        viewHolder.pbRate.setProgress(project.getProjectRate());
        viewHolder.tvRate.setText(project.getProjectRate() + "%");

        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvDate;
        ProgressBar pbRate;
        TextView tvRate;
    }
}
