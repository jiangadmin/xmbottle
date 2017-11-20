package com.wt.piaoliuping.activity;

import android.text.TextUtils;
import android.webkit.WebView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/10/25.
 */

public class WebActivity extends BaseTitleActivity {
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

        loadData();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_web;
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        map.put("size", "1");
        map.put("key", "userRegister");
        HaoConnect.loadContent("web_view/list", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ArrayList<Object> lists = result.findAsList("results>");
                if (lists.size() > 0) {
                    HaoResult result1 = (HaoResult) lists.get(0);
                    webView.loadData(result1.findAsString("content"), "text/html; charset=UTF-8", null);
                }
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }
}
