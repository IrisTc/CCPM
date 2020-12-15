package com.iris.ccpm.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iris.ccpm.R;
import com.iris.ccpm.model.Member;

import java.util.List;

public class MemberAdapter extends BaseAdapter {
    Context context;
    List<Member> memberList;

    public MemberAdapter(Context context, List<Member> members) {
        this.context = context;
        this.memberList = members;
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

        return view;
    }
}
