package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/26.
 */

public class WithdrewActivity extends BaseTitleActivity {
    @BindView(R.id.text_right_title)
    TextView textRightTitle;
    @BindView(R.id.text_star)
    TextView textStar;
    @BindView(R.id.edit_money)
    EditText editMoney;
    @BindView(R.id.layout_1)
    LinearLayout layout1;
    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.layout_2)
    LinearLayout layout2;
    @BindView(R.id.btn_charge)
    Button btnCharge;

    @Override
    public void initView() {
        setTitle("提现");
        textRightTitle.setText("提现记录");
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
            }
        }, this);
    }
}
