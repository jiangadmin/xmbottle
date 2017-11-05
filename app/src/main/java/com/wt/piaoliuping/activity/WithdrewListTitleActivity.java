package com.wt.piaoliuping.activity;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.adapter.WithdrawAdapter;
import com.wt.piaoliuping.base.BaseTitleActivity;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/10/26.
 */

public class WithdrewListTitleActivity extends BaseTitleActivity {
    @BindView(R.id.list_view)
    PullToRefreshListView listView;

    WithdrawAdapter withdrawAdapter;

    @Override
    public void initView() {
        setTitle("提现记录");
        withdrawAdapter = new WithdrawAdapter(this);
        listView.setAdapter(withdrawAdapter);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_withdrew_list;
    }
}
