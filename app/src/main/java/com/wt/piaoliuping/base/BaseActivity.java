package com.wt.piaoliuping.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wt.piaoliuping.R;

import butterknife.ButterKnife;

/**
 * Created by wangtao on 2017/10/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private TextView titleText;
    private ImageButton backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewID());
        ButterKnife.bind(this);
        if (titleText == null) {
            try {
                titleText = (TextView) findViewById(R.id.text_title);
                backBtn = (ImageButton) findViewById(R.id.btn_back);
                backBtn.setVisibility(View.VISIBLE);
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            } catch (Exception e) {

            }
        }
        initView();
    }

    public void setTitle(final String title) {
        if (titleText != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    titleText.setText(title);
                }
            });
        }
    }

    public void hideBackBtn() {
        if (backBtn != null) {
            backBtn.setVisibility(View.GONE);
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public abstract void initView();

    public abstract int getContentViewID();
}