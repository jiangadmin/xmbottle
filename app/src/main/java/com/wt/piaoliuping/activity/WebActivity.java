package com.wt.piaoliuping.activity;

import android.text.TextUtils;
import android.webkit.WebView;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/10/25.
 */

public class WebActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    public void initView() {

        String content = getIntent().getStringExtra("content");
        String title = getIntent().getStringExtra("title");

        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        if (!TextUtils.isEmpty(content)) {
            webView.loadData(content, "text/html; charset=UTF-8", null);
        }
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_web;
    }
}
