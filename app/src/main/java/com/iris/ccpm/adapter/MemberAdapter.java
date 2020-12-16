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
    Integer product_id;
    List<Member> memberList;
    View.OnClickListener listener;        //定义点击事件

    public MemberAdapter(Context context, Integer product_id, List<Member> members, View.OnClickListener listener) {
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
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.member_item, parent, false);

        TextView tvName = view.findViewById(R.id.member_name);
        TextView tvEmail = view.findViewById(R.id.member_email);
        TextView tvPosition = view.findViewById(R.id.member_position);
        SimpleDraweeView ivAvatar = view.findViewById(R.id.member_avatar);

        Member member = this.memberList.get(position);

        tvName.setText(member.getName());
        tvEmail.setText(member.getPhoneNum());
        tvPosition.setText(member.getPosition());
        Uri uri = Uri.parse(member.getAvatarUrl());
        ivAvatar.setImageURI(uri);

        Integer member_id = member.getAccount_uid();
        ImageView btDelete = view.findViewById(R.id.member_delete_button);
        ImageView btShow = view.findViewById(R.id.member_personal_button);
        btDelete.setOnClickListener(listener);
        btShow.setOnClickListener(listener);
        btDelete.setTag(position);
        btShow.setTag(position);

        return view;
    }
}
