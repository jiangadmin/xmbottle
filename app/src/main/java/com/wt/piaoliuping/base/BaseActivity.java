package com.wt.piaoliuping.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.widgt.CustomProgressDialog;

import butterknife.ButterKnife;

/**
 * Created by wangtao on 2017/10/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public CustomProgressDialog customProgressDialog;

    ImageView emptyImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customProgressDialog = new CustomProgressDialog(this);
        setContentView(getContentViewID());
        ButterKnife.bind(this);
        initView();

        if (emptyImage == null) {
            emptyImage = (ImageView) findViewById(R.id.no_data);
        }
        if (emptyImage != null) {
            emptyImage.setVisibility(View.GONE);
        }
        AppManager.getInstance().addActivity(this);
    }

    public void showNoData() {
        if (emptyImage != null) {
            emptyImage.setVisibility(View.VISIBLE);
        }
    }

    public void hideNoData() {
        if (emptyImage != null) {
            emptyImage.setVisibility(View.GONE);
        }
    }

    public void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
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

    @Override
    protected void onResume() {
        super.onResume();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
