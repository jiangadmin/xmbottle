package com.wt.piaoliuping.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
 * Created by wangtao on 2017/12/7.
 */

public class RecommendActivity extends BaseTitleActivity {
    @BindView(R.id.text_edit)
    EditText textEdit;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @Override
    public void initView() {
        setTitle("推荐人");
        loadDetail();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_recommend;
    }


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (TextUtils.isEmpty(textEdit.getText().toString())) {
            showToast("推荐人邀请码不能为空");
            return;
        }
        submit();
    }


    private void submit() {
        Map<String, Object> map = new HashMap<>();
        map.put("invite_code", textEdit.getText().toString());
        HaoConnect.loadContent("user_invite/invite_user", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                showToast("绑定成功");
                finish();
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    private void loadDetail() {
        Map<String, Object> map = new HashMap<>();
        HaoConnect.loadContent("user_invite/detail", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                if (!TextUtils.isEmpty(result.findAsString("userLocal>id"))) {
                    textEdit.setText("推荐人ID：" + result.findAsString("userLocal>id"));
                    textEdit.setEnabled(false);
                    btnSubmit.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFail(HaoResult result) {
            }
        }, this);
    }
}
