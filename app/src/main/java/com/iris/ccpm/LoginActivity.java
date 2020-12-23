package com.iris.ccpm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.android.material.tabs.TabLayout;
import com.iris.ccpm.adapter.MypagerAdapter;
import com.iris.ccpm.model.GlobalData;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.entity.StringEntity;

import static com.iris.ccpm.utils.cache.getCachedUserInfo;
import static com.iris.ccpm.utils.cache.save_user;

public class LoginActivity extends AppCompatActivity {
    TabLayout tbSelect;
    ViewPager vpChosen;
    ArrayList<View> viewList;
    MypagerAdapter mAdapter;
    View loginView;
    View registerView;

    String username;
    String password;

    EditText etUsername;
    EditText etPassword;
    Button btLogin;
    CheckBox cbDisplayPassword;
    CheckBox cbRemeber;
    CheckBox cbautoLogin;
    Boolean autofix;
    Boolean autologin;

    EditText etUsernameRe;
    EditText etPasswordRe;
    EditText etRePasswordRe;
    Button btRegister;
    EditText etNickname;
    EditText etRealName;
    EditText etPhone;
    EditText etPosition;
    EditText etSynopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findView();
        initTabContent();
        getUserinfo(this);

        if (autofix) {
            cbRemeber.setChecked(true);
            autoFix();
        }

        Intent intent = getIntent();
        Boolean isLogout = intent.getBooleanExtra("logout", false);
        if (autologin) {
            cbautoLogin.setChecked(true);
            if (autofix && !isLogout) {
                autoLogin();
            }
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (username.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "请填写完整信息", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject body = new JSONObject();
                    body.put("username", username);
                    body.put("password", password);
                    StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                    login(entity);
                }
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_str = etUsernameRe.getText().toString();
                String pwd_str = etPasswordRe.getText().toString();
                String repwd_str = etRePasswordRe.getText().toString();
                String nickname_str = etNickname.getText().toString();
                String realname_str = etRealName.getText().toString();
                String phone_str = etPhone.getText().toString();
                String synopsis_str = etSynopsis.getText().toString();
                String position_str = etPosition.getText().toString();

                if (name_str.equals("") || repwd_str.equals("")) {
                    Toast.makeText(LoginActivity.this, "请填入完整信息！", Toast.LENGTH_LONG).show();
                } else {
                    AsyncHttpClient client = new AsyncHttpClient();
                    JSONObject body = new JSONObject();
                    body.put("username", name_str);
                    body.put("password", pwd_str);
                    body.put("nickName", nickname_str);
                    body.put("realName", realname_str);
                    body.put("phoneNum", phone_str);
                    body.put("synopsis", synopsis_str);
                    body.put("position", position_str);
                    StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                    Request.clientPost(LoginActivity.this, "account/add", entity, new NetCallBack() {
                        @Override
                        public void onMySuccess(JSONObject result) {
                            Toast.makeText(LoginActivity.this, "注册成功！", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onMyFailure(String error) {
                            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        cbDisplayPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //如果选中，显示密码
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });

        cbRemeber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autofix = isChecked;
            }
        });

        cbautoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autologin = isChecked;
            }
        });

        etUsernameRe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String name_str = etUsernameRe.getText().toString();
                    if (!isEmail(name_str)) {
                        Toast.makeText(LoginActivity.this, "邮箱格式不正确！", Toast.LENGTH_LONG).show();
                        etUsernameRe.setText("");
                    }
                }
            }
        });

        etRePasswordRe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String pwd_str = etPasswordRe.getText().toString();
                    String repwd_str = etRePasswordRe.getText().toString();
                    if (!pwd_str.equals(repwd_str)) {
                        Toast.makeText(LoginActivity.this, "两次输入密码不同！", Toast.LENGTH_LONG).show();
                        etRePasswordRe.setText("");
                    }
                }
            }
        });

        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String phone_str = etPhone.getText().toString();
                    if (isMobile(phone_str) || isPhone(phone_str)) {
                        Toast.makeText(LoginActivity.this, "输入号码格式不正确！", Toast.LENGTH_LONG).show();
                        etPhone.setText("");
                    }
                }
            }
        });

        tbSelect.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpChosen.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public static boolean isEmail(String strEmail) {
        String strPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)" + "|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }


    public static boolean isMobile(final String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static boolean isPhone(final String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    private void login(StringEntity entity) {
        Request.clientPost(LoginActivity.this, "login", entity, new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                save_user(LoginActivity.this, username, password, autofix, autologin);
                String token = result.getString("token");
                final GlobalData app = (GlobalData) getApplication();
                app.setToken(token);
                Request.clientGet("account/" + username, new NetCallBack() {
                    @Override
                    public void onMySuccess(JSONObject result) {
                        GlobalData.save_account(result, LoginActivity.this);
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onMyFailure(String error) {
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onMyFailure(String error) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
                etUsername.setText("");
                etPassword.setText("");
            }
        });
    }

    private void autoFix() {
        System.out.println(username);
        etUsername.setText(username);
        etPassword.setText(password);
    }

    private void autoLogin() {
        JSONObject body = new JSONObject();
        body.put("username", username);
        body.put("password", password);
        StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
        login(entity);
    }

    private void getUserinfo(Context context) {
        Map<String, Object> userInfo = getCachedUserInfo(LoginActivity.this);
        username = (String) userInfo.get("username");
        password = (String) userInfo.get("password");
        autofix = (Boolean) userInfo.get("autofix");
        autologin = (Boolean) userInfo.get("autologin");
        return;
    }

    private void initTabContent() {
        viewList = new ArrayList<View>();
        LayoutInflater li = getLayoutInflater();

        loginView = li.inflate(R.layout.login_login, null, false);
        registerView = li.inflate(R.layout.login_register, null, false);

        btLogin = loginView.findViewById(R.id.bt_login);
        btRegister = registerView.findViewById(R.id.bt_Register);

        etUsername = loginView.findViewById(R.id.et_username);
        etPassword = loginView.findViewById(R.id.et_password);
        cbDisplayPassword = loginView.findViewById(R.id.cb_displayPassword);
        cbRemeber = loginView.findViewById(R.id.cb_remeber);
        cbautoLogin = loginView.findViewById(R.id.cb_autologin);

        etUsernameRe = registerView.findViewById(R.id.et_username_re);
        etPasswordRe = registerView.findViewById(R.id.et_password_re);
        etRePasswordRe = registerView.findViewById(R.id.et_repassword_re);
        etNickname = registerView.findViewById(R.id.et_nickname);
        etPhone = registerView.findViewById(R.id.et_phoneNum);
        etRealName = registerView.findViewById(R.id.et_realname);
        etPosition = registerView.findViewById(R.id.et_position);
        etSynopsis = registerView.findViewById(R.id.et_synopsis);

        viewList.add(loginView);
        viewList.add(registerView);
        mAdapter = new MypagerAdapter(viewList);
        vpChosen.setAdapter((mAdapter));
    }


    private void findView() {
        vpChosen = (ViewPager) findViewById(R.id.vp_chosen);
        tbSelect = (TabLayout) findViewById(R.id.tb_select);
    }
}