package com.wt.piaoliuping.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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

    ImageView emptyImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customProgressDialog = new CustomProgressDialog(this);
        setContentView(getContentViewID());
        ButterKnife.bind(this);
        initView();
        AppManager.getInstance().addActivity(this);

        emptyImage = new ImageView(this);
        emptyImage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        emptyImage.setImageResource(R.drawable.no_data);
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
}
