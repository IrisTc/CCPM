package com.iris.ccpm.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iris.ccpm.R;
import com.iris.ccpm.model.TaskModel;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<TaskModel> {
    private int resourceId;

    public TaskAdapter(Context context,int textViewResourceId, List<TaskModel> objects){
        super(context, textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        TaskModel task=getItem(position);

        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.title=view.findViewById(R.id.title);
            viewHolder.date=view.findViewById(R.id.date);
            viewHolder.priority=view.findViewById(R.id.priority);

            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        viewHolder.title.setText(task.getTaskName());
        viewHolder.date.setText(task.getTaskStartTime());
        String prio="普通";
        int color=0xFF7FFFAA;
        int textColor=Color.BLACK;
        if(task.getTaskEmergent()==0) {
            prio="普通";
            color=0xFF7FFFAA;
            textColor=Color.BLACK;
        }
        else if(task.getTaskEmergent()==1) {
            prio="紧急";
            color=0xFFFF7F50;
            textColor=Color.WHITE;
        }
        else if(task.getTaskEmergent()==2) {
            prio="非常紧急";
            color= Color.RED;
            textColor=Color.WHITE;
        }
        viewHolder.priority.setText(prio);
        viewHolder.priority.setBackgroundColor(color);
        viewHolder.priority.setTextColor(textColor);
        return view;
    }

    class ViewHolder{
        TextView title;
        TextView date;
        TextView priority;
    }
}

