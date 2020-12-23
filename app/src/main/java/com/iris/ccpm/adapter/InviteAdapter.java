package com.iris.ccpm.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.MemberSearchActivity;
import com.iris.ccpm.R;
import com.iris.ccpm.model.Dynamic;
import com.iris.ccpm.model.Invite;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;

public class InviteAdapter extends BaseAdapter {
    Context context;
    List<Invite> inviteList;

    public InviteAdapter(Context context, List<Invite> inviteList) {
        this.context = context;
        this.inviteList = inviteList;
    }

    @Override
    public int getCount() {
        return this.inviteList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.inviteList.get(position);
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

        Invite invite = this.inviteList.get(position);
        viewHolder.tvProject.setText(invite.getProjectName());
        viewHolder.tvContent.setText("项目经理" + invite.getInviteNickName() + "邀请你加入项目 [" + invite.getProjectName() + "]");

        return convertView;
    }

    class ViewHolder {
        TextView tvProject;
        TextView tvContent;
        TextView tvDeal;
    }
}
