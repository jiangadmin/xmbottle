package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.wt.piaoliuping.DemoModel;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.AppManager;
import com.wt.piaoliuping.base.BaseTitleActivity;
import com.wt.piaoliuping.manager.UserManager;
import com.wt.piaoliuping.utils.PreferenceManager;

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
    @BindView(R.id.layout_21)
    LinearLayout layout21;
    @BindView(R.id.layout_22)
    LinearLayout layout22;
    @BindView(R.id.layout_3)
    LinearLayout layout3;
    @BindView(R.id.layout_4)
    LinearLayout layout4;
    @BindView(R.id.layout_5)
    LinearLayout layout5;
    @BindView(R.id.layout_6)
    LinearLayout layout6;
    @BindView(R.id.checkbox_msg)
    CheckBox checkBoxMsg;
    @BindView(R.id.checkbox_shark)
    CheckBox checkBoxShark;

    @Override
    public void initView() {
        setTitle("设置中心");
        model = new DemoModel(getApplicationContext());
        checkBoxMsg.setChecked(getSettingMsgSound());
        checkBoxShark.setChecked(getSettingMsgVibrate());
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

    @OnClick({R.id.layout_1, R.id.layout_2,R.id.layout_21, R.id.layout_22, R.id.layout_3, R.id.layout_4, R.id.layout_5, R.id.layout_6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_1:
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            case R.id.layout_2:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.layout_21:
                startActivity(new Intent(this, FollowListActivity.class));
                break;
            case R.id.layout_22:
                startActivity(new Intent(this, RevokeListActivity.class));
                break;
            case R.id.layout_3:
//                startActivity(new Intent(this, MoreSettingActivity.class));
                break;
            case R.id.layout_4:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            case R.id.layout_5:
                checkBoxMsg.setChecked(!checkBoxMsg.isChecked());
                setSettingMsgSound(checkBoxMsg.isChecked());
                break;
            case R.id.layout_6:
                checkBoxShark.setChecked(!checkBoxShark.isChecked());
                setSettingMsgVibrate(checkBoxShark.isChecked());
                break;
        }
    }

    private DemoModel model;

    public void setSettingMsgSound(boolean paramBoolean) {
        model.setSettingMsgSound(paramBoolean);
    }

    public void setSettingMsgVibrate(boolean paramBoolean) {
        model.setSettingMsgVibrate(paramBoolean);
    }

    public boolean getSettingMsgSound() {
        return model.getSettingMsgSound();
    }

    public boolean getSettingMsgVibrate() {
        return model.getSettingMsgVibrate();
    }

}
