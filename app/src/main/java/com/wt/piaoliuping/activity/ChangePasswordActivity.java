package com.wt.piaoliuping.activity;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.haoxitech.HaoConnect.HaoUtility;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/26.
 */

public class ChangePasswordActivity extends BaseTitleActivity {
    @BindView(R.id.old_password)
    EditText oldPassword;
    @BindView(R.id.current_password)
    EditText currentPassword;
    @BindView(R.id.btn_withdrew)
    Button btnWithdrew;

    @Override
    public void initView() {
        setTitle("修改密码");
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_change_password;
    }

    @OnClick(R.id.btn_withdrew)
    public void onViewClicked() {
        if (TextUtils.isEmpty(oldPassword.getText().toString())) {
            showToast("旧密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(currentPassword.getText().toString())) {
            showToast("新密码不能为空");
            return;
        }
        startLoading();
        Map<String, Object> map = new HashMap<>();
        map.put("old_password", HaoUtility.encodeMD5String(oldPassword.getText().toString()));
        map.put("password", HaoUtility.encodeMD5String(currentPassword.getText().toString()));
        HaoConnect.loadContent("user/update_password", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                stopLoading();
                showToast("修改成功");
                finish();
            }

            @Override
            public void onFail(HaoResult result) {
                stopLoading();
                showToast(result.errorStr);
            }
        }, this);
    }
}
