package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/26.
 */

public class PointActivity extends BaseActivity {
    @BindView(R.id.text_mine_star)
    TextView textMineStar;
    @BindView(R.id.layout_1)
    LinearLayout layout1;
    @BindView(R.id.text_mine_point)
    TextView textMinePoint;
    @BindView(R.id.layout_2)
    LinearLayout layout2;
    @BindView(R.id.edit_money)
    EditText editMoney;
    @BindView(R.id.layout_3)
    LinearLayout layout3;
    @BindView(R.id.text_point)
    TextView textPoint;
    @BindView(R.id.layout_4)
    LinearLayout layout4;
    @BindView(R.id.btn_charge)
    Button btnCharge;
    @BindView(R.id.btn_withdrew)
    Button btnWithdrew;

    @Override
    public void initView() {
        setTitle("我的积分");
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_point;
    }

    @OnClick({R.id.btn_charge, R.id.btn_withdrew})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_charge:
                break;
            case R.id.btn_withdrew:
                startActivity(new Intent(this, WithdrewActivity.class));
                break;
        }
    }
}
