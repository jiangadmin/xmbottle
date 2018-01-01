package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
//        window = getWindow();
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
//        window.setAttributes(params);
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
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
