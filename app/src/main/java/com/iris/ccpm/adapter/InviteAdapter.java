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
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.notify_item_layout, parent, false);

        TextView tvProject = view.findViewById(R.id.tv_projectName);
        TextView tvContent = view.findViewById(R.id.tv_content);
        TextView tvDeal = view.findViewById(R.id.tv_deal);
        tvDeal.setText("需处理");

        Invite invite = this.inviteList.get(position);
        tvProject.setText(invite.getProjectName());
        tvContent.setText("项目经理" + invite.getInviteNickName() + "邀请你加入项目 [" + invite.getProjectName() + "]");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_warn);
                builder.setTitle("警告");
                builder.setMessage("确定加入项目【" + invite.getProjectName() + "】吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JSONObject body = new JSONObject();
                        StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                        Request.clientPost(context, "project/invite/" + invite.getInvite_uid() + "/accept", entity, new NetCallBack() {
                            @Override
                            public void onMySuccess(JSONObject result) {
                                System.out.println(result);
                                Toast.makeText(context, "加入成功", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onMyFailure(String error) {
                                System.out.println(error);
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        return view;
    }
}
