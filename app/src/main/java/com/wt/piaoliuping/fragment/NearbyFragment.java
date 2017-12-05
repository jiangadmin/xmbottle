package com.wt.piaoliuping.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.activity.NearbyActivity;
import com.wt.piaoliuping.activity.SearchUserActivity;
import com.wt.piaoliuping.activity.ShowUserActivity;
import com.wt.piaoliuping.adapter.FriendAdapter;
import com.wt.piaoliuping.base.PageFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/25.
 */

public class NearbyFragment extends PageFragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.layout_1)
    LinearLayout layout1;
    @BindView(R.id.layout_2)
    LinearLayout layout2;
//    @BindView(R.id.layout_3)
//    LinearLayout layout3;
    @BindView(R.id.list_view)
    PullToRefreshListView listView;

    FriendAdapter friendAdapter;

    @Override
    public void initView(View view) {
        super.initView(view);

        setTitle("发现");
        friendAdapter = new FriendAdapter(getActivity());
        listView.setAdapter(friendAdapter);

        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        listView.setOnItemClickListener(this);
        loadData();
        hideNoData();
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_nearby;
    }

    @OnClick({R.id.layout_1, R.id.layout_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_1:
                startActivity(new Intent(getActivity(), NearbyActivity.class));
                break;
            case R.id.layout_2:
                startActivity(new Intent(getActivity(), SearchUserActivity.class));
                break;
//            case R.id.layout_3:
//                startActivity(new Intent(getActivity(), FriendListActivity.class));
//                break;
        }
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        map.put("size", "999");
        map.put("type", "5");
        HaoConnect.loadContent("user_friends/list", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ArrayList<Object> lists = result.findAsList("results>");
                friendAdapter.setData(lists);
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, getActivity());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final HaoResult result = (HaoResult) parent.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), ShowUserActivity.class);
        intent.putExtra("userId", result.findAsString("toUserLocal>id"));
        intent.putExtra("userName", result.findAsString("toUserLocal>nickname"));
        startActivity(intent);
    }
}
