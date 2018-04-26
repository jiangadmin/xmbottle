package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.haoxitech.HaoConnect.HaoUtility;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.loopj.android.http.RequestHandle;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.dialog.Loading;
import com.wt.piaoliuping.manager.HXManager;
import com.wt.piaoliuping.manager.UserManager;
import com.wt.piaoliuping.view.TabToast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangtao on 2017/10/17.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    TextView textxy;
    EditText textTel;
    EditText textPassword;
    TextView textForget;
    Button btnLogin;
    CheckBox btnAgreement;
    TextView btnRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initview();
        evenview();
    }

    private void initview() {
        textxy = findViewById(R.id.login_xy);
        textTel = findViewById(R.id.text_tel);
        textPassword =findViewById(R.id.text_password);
        textForget = findViewById(R.id.text_forget);
        btnLogin = findViewById(R.id.btn_login);
        btnAgreement = findViewById(R.id.btn_agreement);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void evenview() {
        btnRegister.setOnClickListener(this);
        textForget.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        textxy.setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        String telephone = HaoConnect.getString("telephone");
        if (!TextUtils.isEmpty(telephone)) {
            textTel.setText(telephone);
        }
    }

    public void onBtnLoginClicked() {
        if (TextUtils.isEmpty(textTel.getText().toString())) {
            TabToast.makeText("账号不能为空！");
            return;
        }
        if (TextUtils.isEmpty(textPassword.getText().toString())) {
            TabToast.makeText("密码不能为空");
            return;
        }
        if (!btnAgreement.isChecked()) {
            TabToast.makeText("请先同意相关协议");
            return;
        }

        Loading.show(this,"登录中");

        Map<String, Object> map = new HashMap<>();
        map.put("username", textTel.getText().toString());
        map.put("password", HaoUtility.encodeMD5String(textPassword.getText().toString()));

        RequestHandle requestHandle = HaoConnect.loadContent("user/login", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                if (result.isResultsOK()) {
                    Object authInfo = result.find("extraInfo>authInfo");
                    HaoConnect.setCurrentUserInfo(((JsonObject) authInfo).get("Userid").getAsString(), ((JsonObject) authInfo).get("Logintime").getAsString(), ((JsonObject) authInfo).get("Checkcode").getAsString());
                }

                HaoConnect.putString("telephone", textTel.getText().toString());

                new Thread(new Runnable() {
                    public void run() {
                        EMClient.getInstance().login(UserManager.getInstance().getUserId(), HXManager.getInstance().formatPassword(UserManager.getInstance().getUserId()), new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Loading.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onError(int code, final String error) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Loading.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onFail(HaoResult result) {
                TabToast.makeText(result.errorStr);
                Loading.dismiss();
            }
        }, this);
    }

    /**
     * 启动到注册页面
     */
    public void onTextForgetClicked() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    /**
     * 启动到注册页面
     */
    public void onBtnRegisterClicked() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            登录
            case R.id.btn_login:
                onBtnLoginClicked();
                break;
//                注册
            case R.id.btn_register:
                onBtnRegisterClicked();
                break;
//                忘记密码
            case R.id.text_forget:
                onTextForgetClicked();
                break;
//                注册协议
            case R.id.login_xy:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("title", "注册协议");
                startActivity(intent);
                break;

        }
    }
}
