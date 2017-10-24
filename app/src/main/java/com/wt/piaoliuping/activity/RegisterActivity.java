package com.wt.piaoliuping.activity;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.haoxitech.HaoConnect.HaoUtility;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/18.
 */

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.text_tel)
    EditText textTel;
    @BindView(R.id.text_code)
    EditText textCode;
    @BindView(R.id.text_code_send)
    TextView textCodeSend;
    @BindView(R.id.text_password)
    EditText textPassword;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.btn_agreement)
    CheckedTextView btnAgreement;
    @BindView(R.id.btn_login)
    TextView btnLogin;

    private int type;

    @Override
    public void initView() {
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            setTitle("注册");
        } else {
            setTitle("忘记密码");
        }
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.text_code_send)
    public void onTextCodeSendClicked() {
        if (TextUtils.isEmpty(textTel.getText().toString())) {
            showToast("账号不能为空");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("telephone", textTel.getText().toString());
        if (type == 0) {
            map.put("send_type", "1");
        } else {
            map.put("send_type", "2");
        }
        count.start();
        textCodeSend.setClickable(false);

        HaoConnect.loadContent("sms_verify/send_note", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                showToast("发送成功");
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    @OnClick(R.id.btn_submit)
    public void onBtnSubmitClicked() {
        if (TextUtils.isEmpty(textTel.getText().toString())) {
            showToast("账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(textCode.getText().toString())) {
            showToast("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(textPassword.getText().toString())) {
            showToast("密码不能为空");
            return;
        }

        if (type == 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("username", textTel.getText().toString());
            map.put("verify_code", textCode.getText().toString());
            map.put("password", HaoUtility.encodeMD5String(textPassword.getText().toString()));
            HaoConnect.loadContent("user/register", map, "post", new HaoResultHttpResponseHandler() {
                @Override
                public void onSuccess(HaoResult result) {
                    showToast("注册成功");
                    finish();
                }

                @Override
                public void onFail(HaoResult result) {
                    showToast(result.errorStr);
                }
            }, this);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("username", textTel.getText().toString());
            map.put("verify_code", textCode.getText().toString());
            map.put("password", HaoUtility.encodeMD5String(textPassword.getText().toString()));
            HaoConnect.loadContent("user/reset_password", map, "post", new HaoResultHttpResponseHandler() {
                @Override
                public void onSuccess(HaoResult result) {
                    showToast("重置成功");
                    finish();
                }

                @Override
                public void onFail(HaoResult result) {
                    showToast(result.errorStr);
                }
            }, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        count.onFinish();
    }

    MyCount count = new MyCount(60000, 1000);

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            textCodeSend.setText((millisUntilFinished / 1000) + "秒");
        }

        @Override
        public void onFinish() {
            textCodeSend.setText("获取验证码");
            textCodeSend.setClickable(true);
        }
    }
}
