package com.iris.ccpm.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.activity.LoginActivity;
import com.iris.ccpm.R;
import com.iris.ccpm.ui.my.MyViewModel;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import cz.msebera.android.httpclient.entity.StringEntity;

public class SettingFragment extends Fragment {
    private MyViewModel myViewModel;
    TextView tvLogout;
    ConstraintLayout clAccount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myViewModel =
                new ViewModelProvider(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        clAccount = root.findViewById(R.id.cl_account);
        clAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("click");
                Intent intent = new Intent(getActivity(), EditAccountActivity.class);
                startActivity(intent);
            }
        });

        tvLogout = root.findViewById(R.id.tv_logout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringEntity entity = new StringEntity("", "UTF-8");
                Request.clientPost(getActivity(), "logout", entity, new NetCallBack() {
                    @Override
                    public void onMySuccess(JSONObject result) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.putExtra("logout", true);
                        startActivity(intent);
                    }

                    @Override
                    public void onMyFailure(String error) {
                        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return root;
    }
}
