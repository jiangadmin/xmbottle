package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.AppManager;
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
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {

                runOnUiThread(new Runnable() {
                                  public void run() {

                                      Map<String, Object> map = new HashMap<>();
                                      HaoConnect.loadContent("user/logout", map, "get", new HaoResultHttpResponseHandler() {
                                          @Override
                                          public void onSuccess(HaoResult result) {
                                              UserManager.getInstance().logout();
                                              AppManager.getInstance().finishAllActivity();
                                              Intent intent = new Intent(SettingTitleActivity.this, LoginActivity.class);
                                              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                              startActivity(intent);
                                              finish();
                                          }

                                          @Override
                                          public void onFail(HaoResult result) {
                                              showToast(result.errorStr);
                                          }
                                      }, SettingTitleActivity.this);
                                  }
                              }
                    );
            }

            @Override
            public void onError(int code, String error) {

            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    @OnClick({R.id.layout_1, R.id.layout_2, R.id.layout_3, R.id.layout_4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_1:
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            case R.id.layout_2:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.layout_3:
                startActivity(new Intent(this, MoreSettingActivity.class));
                break;
            case R.id.layout_4:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
        }
    }
}
