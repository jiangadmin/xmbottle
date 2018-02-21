package com.wt.piaoliuping.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
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
                            final String friendId = result.findAsString("toUserLocal>id");
                            Map<String, Object> map = new HashMap<>();
                            map.put("user_goods_item_id", prizeId);
                            map.put("give_user_id", friendId);
                            startLoading();
                            HaoConnect.loadContent("user_goods_item/give_user", map, "post", new HaoResultHttpResponseHandler() {
                                @Override
                                public void onSuccess(final HaoResult result) {
                                    showToast("赠送成功");
                                    ImageLoader.getInstance().loadImage(result.findAsString("goodsImgView"), new ImageLoadingListener() {
                                        @Override
                                        public void onLoadingStarted(String imageUri, View view) {
                                            stopLoading();

                                        }

                                        @Override
                                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                            stopLoading();

                                        }

                                        @Override
                                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                                            String file = ImageLoader.getInstance().getDiskCache().get(result.findAsString("goodsImgView")).getAbsolutePath();
//                                    String file = Glide.getPhotoCacheDir(FriendListActivity.this, result.findAsString("goodsImgView")).getAbsolutePath();

                                            EMMessage imageMessage = EMMessage.createImageSendMessage(file, true, friendId);
                                            EMClient.getInstance().chatManager().sendMessage(imageMessage);

                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            String desc = "【" + friendId + "】" + "送您礼物 {" + result.findAsString("goodsName") + "}";
                                            EMMessage message = EMMessage.createTxtSendMessage(desc, friendId);
                                            EMClient.getInstance().chatManager().sendMessage(message);
                                            stopLoading();
                                            setResult(RESULT_OK);
                                            finish();
                                        }

                                        @Override
                                        public void onLoadingCancelled(String imageUri, View view) {
                                            stopLoading();

                                        }
                                    });
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
