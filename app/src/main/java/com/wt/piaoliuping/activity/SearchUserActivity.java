package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.adapter.PersonAdapter;
import com.wt.piaoliuping.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/11/3.
 */

public class SearchUserActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.text_search)
    EditText textSearch;
    @BindView(R.id.btn_search)
    TextView btnSearch;
    @BindView(R.id.list_view)
    PullToRefreshListView listView;

    PersonAdapter personAdapter;

    @Override
    public void initView() {
        setTitle("查找用户");

        personAdapter = new PersonAdapter(this);
        listView.setAdapter(personAdapter);

        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        listView.setOnItemClickListener(this);
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_search_user;
    }

    @OnClick(R.id.btn_search)
    public void onViewClicked() {
        searchUser(textSearch.getText().toString());
    }

    private void searchUser(String keyword) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        map.put("size", "999");
        map.put("event", "search_user");
        if (!TextUtils.isEmpty(keyword)) {
            map.put("search_keyword", keyword);
        }
        HaoConnect.loadContent("user/list", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ArrayList<Object> lists = result.findAsList("results");
                personAdapter.setData(lists);
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
        Intent intent  = new Intent(this, ShowUserActivity.class);
        intent.putExtra("userId", result.findAsString("id"));
        startActivity(intent);
    }
}
