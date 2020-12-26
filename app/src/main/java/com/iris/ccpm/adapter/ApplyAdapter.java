package com.iris.ccpm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iris.ccpm.R;
import com.iris.ccpm.model.Apply;
import com.iris.ccpm.model.Invite;

import java.util.List;

public class ApplyAdapter extends BaseAdapter {
    Context context;
    List<Apply> applyList;

    public ApplyAdapter(Context context, List<Apply> applyList) {
        this.context = context;
        this.applyList = applyList;
    }

    @Override
    public int getCount() {
        return applyList.size();
    }

    @Override
    public Object getItem(int position) {
        return applyList.get(position);
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
            convertView = inflater.inflate(R.layout.notify_item_layout, parent, false);

            viewHolder.tvProject = convertView.findViewById(R.id.tv_projectName);
            viewHolder.tvContent = convertView.findViewById(R.id.tv_content);
            viewHolder.tvDeal = convertView.findViewById(R.id.tv_deal);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvDeal.setText("需处理");

        Apply apply = this.applyList.get(position);
        viewHolder.tvProject.setText(apply.getProjectName());
        viewHolder.tvContent.setText(apply.getApplyAccountNickName() + " 申请加入您的项目 [" + apply.getProjectName() + "]");

        return convertView;
    }

    class ViewHolder {
        TextView tvProject;
        TextView tvContent;
        TextView tvDeal;
    }
}
