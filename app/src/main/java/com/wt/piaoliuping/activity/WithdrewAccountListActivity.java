package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.adapter.WithdrawAccountAdapter;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/12/20.
 */

public class WithdrewAccountListActivity extends BaseTitleActivity implements AdapterView.OnItemClickListener {

    WithdrawAccountAdapter adapter;
    @BindView(R.id.list_view)
    PullToRefreshListView listView;

    @Override
    public void initView() {
        setTitle("提现账号");

        adapter = new WithdrawAccountAdapter(this);
        listView.setAdapter(adapter);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        listView.setOnItemClickListener(this);
        loadData();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_withdrew_account_list;
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        map.put("size", "999");
        HaoConnect.loadContent("goods_item/list", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ArrayList<Object> lists = result.findAsList("results>");
                adapter.setData(lists);
                if (adapter.dataList.isEmpty()) {
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        HaoResult haoResult = (HaoResult) adapter.dataList.get(i);
        intent.putExtra("name", haoResult.findAsString("name"));
        intent.putExtra("account", haoResult.findAsString("account"));
        setResult(RESULT_OK, intent);
        finish();
    }
}
