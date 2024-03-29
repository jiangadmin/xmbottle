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
import com.wt.piaoliuping.adapter.NameAdapter;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/10/26.
 */

public class RevokeListActivity extends BaseTitleActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.list_view)
    PullToRefreshListView listView;

    NameAdapter nameAdapter;

    @Override
    public void initView() {
        setTitle("黑名单");
        nameAdapter = new NameAdapter(this);
        listView.setAdapter(nameAdapter);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        listView.setOnItemClickListener(this);
        loadData();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_name_list;
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        map.put("size", "999");
        map.put("type", "7");
        HaoConnect.loadContent("user_friends/list", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ArrayList<Object> lists = result.findAsList("results>");
                nameAdapter.setData(lists);
                if (nameAdapter.dataList.isEmpty()) {
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HaoResult result = (HaoResult) parent.getAdapter().getItem(position);
        Intent intent = new Intent(this, ShowUserActivity.class);
        intent.putExtra("userId", result.findAsString("toUserLocal>id"));
        intent.putExtra("userName", result.findAsString("toUserLocal>nickname"));
        startActivity(intent);
    }
}
