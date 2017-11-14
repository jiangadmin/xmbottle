package com.wt.piaoliuping.activity;

import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.adapter.GoodsAdapter;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/11/14.
 */

public class GoodsListActivity extends BaseTitleActivity implements GoodsAdapter.ItemClickListener {
    @BindView(R.id.grid_view)
    PullToRefreshGridView gridView;

    GoodsAdapter adapter;

    @Override
    public void initView() {
        setTitle("礼品商城");
        adapter = new GoodsAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setItemClickListener(this);
        loadData();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_goods_list;
    }

    @Override
    public void click(View v, int position) {
        if (v.getId() == R.id.btn_submit) {

        }
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
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }
}
