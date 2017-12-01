package com.wt.piaoliuping.base;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wt.piaoliuping.R;

/**
 * Created by wangtao on 2017/10/17.
 */

public abstract class BaseTitleActivity extends BaseActivity {

    private TextView titleText;
    private ImageButton backBtn;

    public void setTitle(final String title) {
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                titleText.setText(title);
            }
        });
    }

    public void hideBackBtn() {
        if (backBtn != null) {
            backBtn.setVisibility(View.GONE);
        }
    }
}
