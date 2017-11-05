package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;
import com.wt.piaoliuping.manager.UserManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/26.
 */

public class SettingTitleActivity extends BaseTitleActivity {
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.layout_1)
    LinearLayout layout1;
    @BindView(R.id.layout_2)
    LinearLayout layout2;
    @BindView(R.id.layout_3)
    LinearLayout layout3;
    @BindView(R.id.layout_4)
    LinearLayout layout4;

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
        Map<String, Object> map = new HashMap<>();
        HaoConnect.loadContent("user/logout", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                UserManager.getInstance().logout();
                Intent intent = new Intent(SettingTitleActivity.this, LoginTitleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    @OnClick({R.id.layout_1, R.id.layout_2, R.id.layout_3, R.id.layout_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_1:
                startActivity(new Intent(this, UserInfoTitleActivity.class));
                break;
            case R.id.layout_2:
                startActivity(new Intent(this, AboutTitleActivity.class));
                break;
            case R.id.layout_3:
                startActivity(new Intent(this, MoreSettingTitleActivity.class));
                break;
            case R.id.layout_4:
                startActivity(new Intent(this, ChangePasswordTitleActivity.class));
                break;
        }
    }
}
