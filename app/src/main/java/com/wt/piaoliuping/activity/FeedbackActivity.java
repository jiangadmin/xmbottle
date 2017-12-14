package com.wt.piaoliuping.activity;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/26.
 */

public class FeedbackActivity extends BaseTitleActivity {
    @BindView(R.id.text_input)
    EditText textInput;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.text_tips)
    TextView textTips;

    @Override
    public void initView() {
        setTitle("联系客服");
        loadData();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_feedback;
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (TextUtils.isEmpty(textInput.getText().toString())) {
            return;
        }
        startLoading();
        Map<String, Object> map = new HashMap<>();
        map.put("content", textInput.getText().toString());
        HaoConnect.request("feedback/add", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                stopLoading();
                showToast("反馈成功");
                finish();
            }
            @Override
            public void onFail(HaoResult result) {
                stopLoading();
                showToast(result.errorStr);
            }
        }, this);
    }


    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "contactWe");
        HaoConnect.loadContent("sys_config/detail", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                textTips.setText(result.findAsString("results>value"));
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }
}
