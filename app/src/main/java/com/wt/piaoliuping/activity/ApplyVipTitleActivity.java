package com.wt.piaoliuping.activity;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

/**
 * Created by wangtao on 2017/10/26.
 */

public class ApplyVipTitleActivity extends BaseTitleActivity {

    @Override
    public void initView() {
        setTitle("申请成为高级会员");
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_apply_vip;
    }
}
