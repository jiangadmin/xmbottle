package com.wt.piaoliuping.activity;

import android.text.TextUtils;
import android.view.View;
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
 * Created by wangtao on 2017/11/12.
 */

public class WarningActivity extends BaseTitleActivity {
    @BindView(R.id.text_id)
    TextView textId;
    @BindView(R.id.text_input)
    EditText textInput;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    String userId;

    @Override
    public void initView() {
        setTitle("投诉");
        userId = getIntent().getStringExtra("userId");
        textId.setText("ID：" + userId);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_warning;
    }

    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (TextUtils.isEmpty(textInput.getText().toString())) {
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("user_id", userId);
                map.put("content", textInput.getText().toString());
                HaoConnect.loadContent("complain/add", map, "post", new HaoResultHttpResponseHandler() {
                    @Override
                    public void onSuccess(HaoResult result) {
                        showToast("投诉成功");
                        finish();
                    }

                    @Override
                    public void onFail(HaoResult result) {
                        showToast(result.errorStr);
                    }
                }, this);
                break;
        }
    }
}
