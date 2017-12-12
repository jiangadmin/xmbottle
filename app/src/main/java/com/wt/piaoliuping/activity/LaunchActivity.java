package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.wt.piaoliuping.App;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;
import com.wt.piaoliuping.manager.HXManager;
import com.wt.piaoliuping.manager.UserManager;

/**
 * Created by wangtao on 2017/10/18.
 */

public class LaunchActivity extends BaseTitleActivity implements Runnable {

    private Handler mHandler;

    @Override
    public void initView() {
        mHandler = new Handler();
        mHandler.postDelayed(this, 2000);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_launch;
    }

    @Override
    public void run() {
        if (UserManager.getInstance().isLogin()) {
            if (TextUtils.isEmpty(UserManager.getInstance().getUserId())) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LaunchActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        stopLoading();
                    }
                });

            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
