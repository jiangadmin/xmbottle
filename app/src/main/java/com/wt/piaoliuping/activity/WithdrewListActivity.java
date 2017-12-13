package com.wt.piaoliuping.activity;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.adapter.WithdrawAdapter;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/10/26.
 */

public class WithdrewListActivity extends BaseTitleActivity {
    @BindView(R.id.list_view)
    PullToRefreshListView listView;

    WithdrawAdapter withdrawAdapter;

    @Override
    public void initView() {
        setTitle("提现记录");
        withdrawAdapter = new WithdrawAdapter(this);
        listView.setAdapter(withdrawAdapter);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        loadData();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_withdrew_list;
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        map.put("size", "999");
        HaoConnect.loadContent("extraction/list", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ArrayList<Object> lists = result.findAsList("results>");
                withdrawAdapter.setData(lists);
                if (withdrawAdapter.dataList.isEmpty()) {
                    showNoData();
                } else {
                    hideNoData();
                }
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }
}
