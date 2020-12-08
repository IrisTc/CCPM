package com.iris.ccpm.ui.setting;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.snackbar.Snackbar;
import com.iris.ccpm.R;
import com.iris.ccpm.model.GlobalData;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import cz.msebera.android.httpclient.entity.StringEntity;

import static com.iris.ccpm.utils.Image.loadImageFromNetwork;

public class EditAccountActivity extends AppCompatActivity {
    TextView tvUsername;
    EditText etNickname;
    EditText etRealname;
    EditText etPhonenum;
    EditText etPosition;
    EditText etSynopsis;
    SimpleDraweeView ivAvatar;
    GlobalData app;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);
        app = (GlobalData) getApplication();

        findView();
        initInfo();

        findViewById(R.id.bt_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname_str = etNickname.getText().toString();
                String realname_str = etRealname.getText().toString();
                String phone_str = etPhonenum.getText().toString();
                String synopsis_str = etSynopsis.getText().toString();
                String position_str = etPosition.getText().toString();

                JSONObject body = new JSONObject();
                body.put("username", app.getUsername());
                body.put("nickName", nickname_str);
                body.put("realName", realname_str);
                body.put("phoneNum", phone_str);
                body.put("synopsis", synopsis_str);
                body.put("position", position_str);
                StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                Request.clientPut(EditAccountActivity.this, "/account", entity, new NetCallBack() {
                    @Override
                    public void onMySuccess(JSONObject result) {
                        GlobalData.save_account(body, EditAccountActivity.this);
                        initInfo();
                        Snackbar.make(v, "修改成功！", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Toast.makeText(EditAccountActivity.this, "修改成功！", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onMyFailure(String error) {
                        Toast.makeText(EditAccountActivity.this, "修改失败！", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void initInfo(){
        tvUsername.setText(app.getUsername() + "(不可更改)");
        etNickname.setText(app.getNickName());
        etRealname.setText(app.getRealName());
        etPhonenum.setText(app.getPhoneNum());
        etPosition.setText(app.getPosition());
        etSynopsis.setText(app.getSynopsis());
        Uri uri = Uri.parse(app.getAvatarUrl());
        ivAvatar.setImageURI(uri);
    }

    private void findView() {
        tvUsername = findViewById(R.id.tv_username);
        etNickname = findViewById(R.id.et_nickname);
        etRealname = findViewById(R.id.et_realname);
        etPhonenum = findViewById(R.id.et_phoneNum);
        etPosition = findViewById(R.id.et_position);
        etSynopsis = findViewById(R.id.et_synopsis);
        ivAvatar = findViewById(R.id.iv_avatar);
    }
}
