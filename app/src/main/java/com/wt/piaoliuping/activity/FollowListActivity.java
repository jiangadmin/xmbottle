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
import com.wt.piaoliuping.adapter.FollowAdapter;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/11/3.
 */

public class FollowListActivity extends BaseTitleActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.list_view)
    PullToRefreshListView listView;

    FollowAdapter friendAdapter;

    @Override
    public void initView() {

        setTitle("关注");
        friendAdapter = new FollowAdapter(this);
        listView.setAdapter(friendAdapter);

        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        listView.setOnItemClickListener(this);
        loadData();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_friend_list;
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        map.put("size", "999");
        HaoConnect.loadContent("user_friends/list", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ArrayList<Object> lists = result.findAsList("results>");
                friendAdapter.setData(lists);
                if (friendAdapter.dataList.isEmpty()) {
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
