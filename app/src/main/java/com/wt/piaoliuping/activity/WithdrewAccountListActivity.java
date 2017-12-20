package com.wt.piaoliuping.activity;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

/**
 * Created by wangtao on 2017/12/20.
 */

public class WithdrewAccountListActivity extends BaseTitleActivity {
    @Override
    public void initView() {
        setTitle("提现账号");
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_withdrew_account_list;
    }
}
