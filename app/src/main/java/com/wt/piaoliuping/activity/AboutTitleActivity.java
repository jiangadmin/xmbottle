package com.wt.piaoliuping.activity;

import android.webkit.WebView;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/10/26.
 */

public class AboutTitleActivity extends BaseTitleActivity {
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    public void initView() {
        setTitle("关于我们");
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_web;
    }
}
