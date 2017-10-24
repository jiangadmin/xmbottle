package com.wt.piaoliuping.fragment;

import android.view.View;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.PageFragment;

/**
 * Created by wangtao on 2017/10/25.
 */

public class SettingFragment extends PageFragment {
    @Override
    public void initView(View view) {
        super.initView(view);
        setTitle("设置");
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_setting;
    }
}
