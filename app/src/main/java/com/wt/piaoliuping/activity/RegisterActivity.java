package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.haoxitech.HaoConnect.HaoUtility;
import com.wt.piaoliuping.App;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;
import com.wt.piaoliuping.utils.DisplayUtil;
import com.wt.piaoliuping.widgt.SexDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/18.
 */

public class RegisterActivity extends BaseTitleActivity {
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
    CheckBox btnAgreement;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.text_sex)
    TextView textSex;

    private int type;

    @Override
    public void initView() {
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            setTitle("注册");
        } else {
            setTitle("忘记密码");
            findViewById(R.id.layout_sex).setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(this, 135));
            findViewById(R.id.layout_content).setLayoutParams(layoutParams);
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
        if (!btnAgreement.isChecked()) {
            showToast("请先同意相关协议");
            return;
        }

        startLoading();

        if (type == 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("username", textTel.getText().toString());
            map.put("verify_code", textCode.getText().toString());
            if (textSex.getText().toString().equals("男")) {
                map.put("sex", "1");
            } else {
                map.put("sex", "2");
            }
            map.put("password", HaoUtility.encodeMD5String(textPassword.getText().toString()));
            HaoConnect.loadContent("user/register", map, "post", new HaoResultHttpResponseHandler() {
                @Override
                public void onSuccess(HaoResult result) {
                    App.app.userName = textTel.getText().toString();
                    showToast("注册成功");
                    finish();
                    stopLoading();
                }

                @Override
                public void onFail(HaoResult result) {
                    showToast(result.errorStr);
                }
            }, this);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("telephone", textTel.getText().toString());
            map.put("verify_code", textCode.getText().toString());
            map.put("password", HaoUtility.encodeMD5String(textPassword.getText().toString()));
            HaoConnect.loadContent("user/reset_password", map, "post", new HaoResultHttpResponseHandler() {
                @Override
                public void onSuccess(HaoResult result) {
                    showToast("重置成功");
                    finish();
                    stopLoading();
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

    @OnClick(R.id.text_sex)
    public void onViewClicked() {
        final SexDialog dialog = SexDialog.create(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.layout_female) {
                    textSex.setText("女");
                } else if (v.getId() == R.id.layout_male) {
                    textSex.setText("男");
                }
            }
        });
        dialog.show();
    }


    @OnClick(R.id.btn_agreement)
    public void onBtnViewClicked() {

        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("title", "注册协议");
        startActivity(intent);
    }

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
