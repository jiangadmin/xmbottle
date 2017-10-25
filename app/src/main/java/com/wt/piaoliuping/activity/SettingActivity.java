package com.wt.piaoliuping.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/26.
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.layout_1)
    LinearLayout layout1;
    @BindView(R.id.layout_2)
    LinearLayout layout2;
    @BindView(R.id.layout_3)
    LinearLayout layout3;
    @BindView(R.id.btn_logout)
    Button btnLogout;

    @Override
    public void initView() {
        setTitle("设置中心");
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_setting;
    }

    @OnClick(R.id.btn_logout)
    public void onViewClicked() {
    }
}
