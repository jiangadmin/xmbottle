package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/26.
 */

public class WithdrewActivity extends BaseTitleActivity {
    @BindView(R.id.text_right_title)
    TextView textRightTitle;
    @BindView(R.id.text_star)
    TextView textStar;
    @BindView(R.id.edit_num)
    EditText editNum;
    @BindView(R.id.edit_money)
    EditText editMoney;
    @BindView(R.id.layout_1)
    LinearLayout layout1;
    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.layout_2)
    LinearLayout layout2;
    @BindView(R.id.layout_3)
    LinearLayout layout3;
    @BindView(R.id.btn_charge)
    Button btnCharge;

    String amount;
    @BindView(R.id.btn_withdrew_right)
    ImageButton btnWithdrewRight;

    @Override
    public void initView() {
        setTitle("提现");
        textRightTitle.setText("提现详情");
        loadUserInfo();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_withdrew;
    }

    @OnClick({R.id.text_right_title, R.id.btn_charge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_right_title:
                Intent intent = new Intent(this, WithdrewListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_charge:
                withdrew();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUser();
    }

    private void loadUser() {
        HaoConnect.loadContent("user/my_detail", null, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                textStar.setText("我的星星：" + result.findAsString("amount"));
                amount = result.findAsString("amount");
            }
        }, this);
    }

    private void loadUserInfo() {
        HaoConnect.loadContent("extraction/detail_user", null, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                if (!TextUtils.isEmpty(result.findAsString("extrUsername"))) {
                    editAccount.setText(result.findAsString("extrUsername"));
                    editMoney.setText(result.findAsString("extrName"));
                }
            }
        }, this);
    }
    private void withdrew() {
        if (editMoney.getText().toString().isEmpty()) {
            showToast("用户名不能为空");
            return;
        }
        if (editAccount.getText().toString().isEmpty()) {
            showToast("账户不能为空");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        String score;
        if (editNum.getText().toString().length() == 0) {
            score = amount;
        } else {
            score = editNum.getText().toString();
        }
        map.put("score", score);
        map.put("extr_type", 1);
        map.put("extr_name", editMoney.getText().toString());
        map.put("extr_username", editAccount.getText().toString());
        map.put("extr_notes", "支付宝直接转账给我就好，转好之后通知下 谢谢");
        startLoading();
        HaoConnect.loadContent("extraction/add", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                if (result.isResultsOK()) {
                    showToast("提现中，请点击右上角查看提现状态");
                }
                stopLoading();
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
                stopLoading();
            }
        }, this);

    }

    @OnClick(R.id.btn_withdrew_right)
    public void onViewClicked() {
        Intent intent = new Intent(this, WithdrewAccountListActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (100 == requestCode && resultCode == RESULT_OK) {
            editMoney.setText(data.getStringExtra("name"));
            editAccount.setText(data.getStringExtra("account"));
        }
    }
}
