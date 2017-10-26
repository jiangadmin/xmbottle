package com.wt.piaoliuping.activity;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseActivity;

/**
 * Created by wangtao on 2017/10/26.
 */

public class UserInfoActivity extends BaseActivity {
    @Override
    public void initView() {
        setTitle("个人资料");
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_user_info;
    }
}
