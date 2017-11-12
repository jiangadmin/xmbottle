package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.os.Handler;

import com.haoxitech.HaoConnect.HaoConnect;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;
import com.wt.piaoliuping.manager.UserManager;

/**
 * Created by wangtao on 2017/10/18.
 */

public class LaunchTitleActivity extends BaseTitleActivity implements Runnable {

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
            Intent intent = new Intent(this, HomeTitleActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginTitleActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();
    }
}
