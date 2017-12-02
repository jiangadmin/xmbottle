package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.adapter.PrizeAdapter;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/11/14.
 */

public class PrizeListActivity extends BaseTitleActivity implements PrizeAdapter.ItemClickListener {
    @BindView(R.id.grid_view)
    PullToRefreshGridView gridView;

    PrizeAdapter adapter;
    @BindView(R.id.text_point)
    TextView textPoint;

    @Override
    public void initView() {
        setTitle("礼物列表");
        adapter = new PrizeAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setItemClickListener(this);
        loadUser();
        loadData();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_prize_list;
    }

    @Override
    public void click(View v, int position) {
        if (v.getId() == R.id.btn_submit) {
            final HaoResult result = (HaoResult) adapter.dataList.get(position);
            Intent intent = new Intent(this, FriendListActivity.class);
            intent.putExtra("choose", true);
            intent.putExtra("prizeId", result.findAsString("id"));
            startActivityForResult(intent, 100);
        }
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        map.put("size", "999");
        HaoConnect.loadContent("user_goods_item/list", map, "get", new HaoResultHttpResponseHandler() {
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


    private void loadUser() {
        HaoConnect.loadContent("user/my_detail", null, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                textPoint.setText("我的积分：" + result.findAsString("score") + "星星");
            }
        }, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadData();
        }
    }
}
