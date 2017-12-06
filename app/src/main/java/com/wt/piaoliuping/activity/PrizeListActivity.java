package com.wt.piaoliuping.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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

    private String userId;
    @Override
    public void initView() {
        setTitle("礼物列表");
        adapter = new PrizeAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setItemClickListener(this);
        userId = getIntent().getStringExtra("userId");
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
            if (TextUtils.isEmpty(userId)) {
                Intent intent = new Intent(this, FriendListActivity.class);
                intent.putExtra("choose", true);
                intent.putExtra("prizeId", result.findAsString("id"));
                startActivityForResult(intent, 100);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定将礼物送给该好友吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("user_goods_item_id", result.findAsString("id"));
                                map.put("give_user_id", userId);
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
                                }, PrizeListActivity.this);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
            }
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
