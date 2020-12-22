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
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.project_item_layout, parent, false);
        TextView tvTitle = (TextView)view.findViewById(R.id.title);
        ProgressBar pbRate = view.findViewById(R.id.pb_rate);
        TextView tvRate = view.findViewById(R.id.tv_rate);

        Project project = this.projectList.get(position);
        tvTitle.setText(project.getProjectName());
        pbRate.setProgress(project.getProjectRate());
        tvRate.setText(project.getProjectRate() + "%");

        return view;
    }
}
