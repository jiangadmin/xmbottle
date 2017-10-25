package com.wt.piaoliuping.activity;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.adapter.NameAdapter;
import com.wt.piaoliuping.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/10/26.
 */

public class NameListActivity extends BaseActivity {
    @BindView(R.id.list_view)
    PullToRefreshListView listView;

    NameAdapter nameAdapter;

    @Override
    public void initView() {
        setTitle("黑名单");
        nameAdapter = new NameAdapter(this);
        listView.setAdapter(nameAdapter);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_name_list;
    }
}
