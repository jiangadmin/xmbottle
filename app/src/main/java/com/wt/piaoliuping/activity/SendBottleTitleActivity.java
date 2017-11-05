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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/28.
 */

public class SendBottleTitleActivity extends BaseTitleActivity {
    @BindView(R.id.text_content)
    EditText textContent;
    @BindView(R.id.text_rand)
    TextView textRand;
    @BindView(R.id.btn_send)
    Button btnSend;

    private String randId;

    @Override
    public void initView() {
        setTitle("扔一个");
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_send_bottle;
    }

    @OnClick(R.id.text_rand)
    public void onTextRandClicked() {
        Map<String, Object> map = new HashMap<>();
        map.put("limit", "1");
        HaoConnect.loadContent("bottle_message/bottle_text", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                textContent.setText(result.findAsString("0>message"));
                randId = result.findAsString("id");
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    @OnClick(R.id.btn_send)
    public void onBtnSendClicked() {
        if (TextUtils.isEmpty(textContent.getText())) {
            showToast("内容不能为空");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("message", textContent.getText());
        if (!TextUtils.isEmpty(randId)) {
            map.put("bottle_text_id", randId);
        }
        HaoConnect.loadContent("bottle_message/add", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                finish();
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }
}
