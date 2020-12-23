package com.iris.ccpm.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iris.ccpm.R;
import com.iris.ccpm.model.Member;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;

public class MemberAdapter extends BaseAdapter {
    Context context;
    String product_id;
    List<Member> memberList;
    View.OnClickListener listener;        //定义点击事件

    public MemberAdapter(Context context, String product_id, List<Member> members, View.OnClickListener listener) {
        this.context = context;
        this.product_id = product_id;
        this.memberList = members;
        this.listener = listener;
    }


    @Override
    public int getCount() {
        return this.memberList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.memberList.get(position);
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
            convertView = inflater.inflate(R.layout.member_item, parent, false);

            viewHolder.tvName = convertView.findViewById(R.id.member_name);
            viewHolder.tvEmail = convertView.findViewById(R.id.member_email);
            viewHolder.tvPosition = convertView.findViewById(R.id.member_position);
            viewHolder.ivAvatar = convertView.findViewById(R.id.member_avatar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Member member = this.memberList.get(position);

        viewHolder.tvName.setText(member.getName());
        viewHolder.tvEmail.setText(member.getPhoneNum());
        viewHolder.tvPosition.setText(member.getPosition());
        Uri uri = Uri.parse(member.getAvatarUrl());
        viewHolder.ivAvatar.setImageURI(uri);

        viewHolder.btDelete = convertView.findViewById(R.id.member_delete_button);
        viewHolder.btShow = convertView.findViewById(R.id.member_personal_button);
        viewHolder.btDelete.setOnClickListener(listener);
        viewHolder.btShow.setOnClickListener(listener);
        viewHolder.btDelete.setTag(position);
        viewHolder.btShow.setTag(position);

        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvEmail;
        TextView tvPosition;
        SimpleDraweeView ivAvatar;
        ImageView btDelete;
        ImageView btShow;
    }
}
