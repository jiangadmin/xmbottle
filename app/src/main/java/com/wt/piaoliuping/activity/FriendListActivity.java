package com.wt.piaoliuping.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.adapter.FriendAdapter;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/11/3.
 */

public class FriendListActivity extends BaseTitleActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.list_view)
    PullToRefreshListView listView;

    FriendAdapter friendAdapter;

    private Boolean choose;
    private String prizeId;

    @Override
    public void initView() {

        setTitle("好友列表");
        friendAdapter = new FriendAdapter(this);
        listView.setAdapter(friendAdapter);

        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        listView.setOnItemClickListener(this);
        choose = getIntent().getBooleanExtra("choose", false);
        prizeId = getIntent().getStringExtra("prizeId");
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
        }, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final HaoResult result = (HaoResult) parent.getAdapter().getItem(position);
        if (choose) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("确定将礼物送给该好友吗")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String friendId = result.findAsString("toUserLocal>id");
                            Map<String, Object> map = new HashMap<>();
                            map.put("user_goods_item_id", prizeId);
                            map.put("give_user_id", friendId);
                            HaoConnect.loadContent("user_goods_item/give_user", map, "post", new HaoResultHttpResponseHandler() {
                                @Override
                                public void onSuccess(HaoResult result) {
                                    showToast("增送成功");
                                    setResult(RESULT_OK);
                                    finish();
                                }

                                @Override
                                public void onFail(HaoResult result) {
                                    showToast(result.errorStr);
                                }
                            }, FriendListActivity.this);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();

        } else {
            Intent intent = new Intent(this, ShowUserActivity.class);
            intent.putExtra("userId", result.findAsString("toUserLocal>id"));
            intent.putExtra("userName", result.findAsString("toUserLocal>nickname"));
            startActivity(intent);
        }
    }
}
