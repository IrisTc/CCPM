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

import com.iris.ccpm.LoginActivity;
import com.iris.ccpm.R;
import com.iris.ccpm.model.GlobalData;
import com.iris.ccpm.ui.my.MyViewModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;

import cz.msebera.android.httpclient.Header;
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
                final GlobalData app = (GlobalData) getActivity().getApplication();
                AsyncHttpClient client = new AsyncHttpClient();
                String url = "https://find-hdu.com/logout";
                client.addHeader("token", app.getToken());
                StringEntity entity = new StringEntity("", "UTF-8");
                client.post(getActivity(), url, entity, "application/json", new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                        Integer code = null;
                        System.out.println(response);
                        try {
                            code = response.getInt("code");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code != 210) {
                            try {
                                String msg = response.getString("msg");
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.putExtra("logout", true);
                            startActivity(intent);
                        }
                    }

                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, org.json.JSONObject errorResponse) {
                        Toast.makeText(getActivity(), "出了点问题...", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return root;
    }
}
