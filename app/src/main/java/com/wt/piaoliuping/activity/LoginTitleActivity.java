package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.haoxitech.HaoConnect.HaoUtility;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.wt.piaoliuping.App;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/10/17.
 */

public class LoginTitleActivity extends BaseTitleActivity implements View.OnClickListener {

    @BindView(R.id.text_tel)
    EditText textTel;
    @BindView(R.id.text_password)
    EditText textPassword;
    @BindView(R.id.text_forget)
    TextView textForget;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_agreement)
    CheckedTextView btnAgreement;
    @BindView(R.id.btn_register)
    TextView btnRegister;

    @Override
    public void initView() {
        setTitle("登录");
        hideBackBtn();
        btnRegister.setOnClickListener(this);
        textForget.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnAgreement.setOnClickListener(this);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_login;
    }

    public void onBtnLoginClicked() {
        if (TextUtils.isEmpty(textTel.getText().toString())) {
            showToast("账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(textPassword.getText().toString())) {
            showToast("密码不能为空");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("username", textTel.getText().toString());
        map.put("password", HaoUtility.encodeMD5String(textPassword.getText().toString()));

        HaoConnect.loadContent("user/login", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                if (result.isResultsOK()) {
                    Object authInfo = result.find("extraInfo>authInfo");
                    HaoConnect.setCurrentUserInfo(((JsonObject) authInfo).get("Userid").getAsString(), ((JsonObject) authInfo).get("Logintime").getAsString(), ((JsonObject) authInfo).get("Checkcode").getAsString());
                    App.app.userId = HaoConnect.getUserid();
                }

                new Thread(new Runnable() {
                    public void run() {
                        EMClient.getInstance().login(App.app.userId, textPassword.getText().toString(), new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(LoginTitleActivity.this, HomeTitleActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onError(int code, String error) {
                                showToast(error);
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
                showToast(result.errorStr);
            }
        }, this);
    }

    public void onTextForgetClicked() {
        Intent intent = new Intent(this, RegisterTitleActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    public void onBtnRegisterClicked() {
        startActivity(new Intent(this, RegisterTitleActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                onBtnLoginClicked();
                break;
            case R.id.btn_register:
                onBtnRegisterClicked();
                break;
            case R.id.text_forget:
                onTextForgetClicked();
                break;
            case R.id.btn_agreement: {
                Intent intent = new Intent(this, WebTitleActivity.class);
                intent.putExtra("title", "注册协议");
                startActivity(intent);
                break;
            }
        }
    }
}
