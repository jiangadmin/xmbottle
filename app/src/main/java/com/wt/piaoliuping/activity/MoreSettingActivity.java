package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/26.
 */

public class MoreSettingActivity extends BaseTitleActivity {

    @BindView(R.id.checkbox_msg)
    CheckBox checkboxMsg;
    @BindView(R.id.layout_1)
    LinearLayout layout1;
    @BindView(R.id.checkbox_shark)
    CheckBox checkboxShark;
    @BindView(R.id.layout_2)
    LinearLayout layout2;
    @BindView(R.id.layout_3)
    LinearLayout layout3;
    @BindView(R.id.layout_4)
    LinearLayout layout4;

    @Override
    public void initView() {
        setTitle("更多设置");
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_more_setting;
    }

    @OnClick(R.id.layout_3)
    public void onLayout3Clicked() {
        startActivity(new Intent(this, FeedbackActivity.class));
    }

    @OnClick(R.id.layout_4)
    public void onLayout4Clicked() {
        startActivity(new Intent(this, ApplyVipActivity.class));
    }
}
