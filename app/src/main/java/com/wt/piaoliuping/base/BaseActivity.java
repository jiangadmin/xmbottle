package com.wt.piaoliuping.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.widgt.CustomProgressDialog;

import butterknife.ButterKnife;

/**
 * Created by wangtao on 2017/10/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customProgressDialog = new CustomProgressDialog(this);
        setContentView(getContentViewID());
        ButterKnife.bind(this);
        initView();
        AppManager.getInstance().addActivity(this);
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public abstract void initView();

    public abstract int getContentViewID();

    public void startLoading() {
        customProgressDialog.startProgressDialog();
    }

    public void stopLoading() {
        customProgressDialog.stopProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().finishActivity(this);
    }
}
