package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.chat.EMMessage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/28.
 */

public class ShowUserActivity extends BaseTitleActivity {

    @BindView(R.id.image_head)
    ImageView imageHead;
    @BindView(R.id.text_add_friend)
    TextView textAddFriend;
    @BindView(R.id.text_msg)
    TextView textMsg;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_id)
    TextView textId;
    @BindView(R.id.text_sex)
    TextView textSex;
    @BindView(R.id.text_city)
    TextView textCity;
    @BindView(R.id.text_age)
    TextView textAge;
    @BindView(R.id.text_star)
    TextView textStar;
    @BindView(R.id.text_sign)
    TextView textSign;
    @BindView(R.id.text_black)
    TextView textBlack;
    @BindView(R.id.text_right_title)
    TextView textRightTitle;
    private String userId;
    private String userName;

    boolean isFriend;

    @Override
    public void initView() {

        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        setTitle(userName);
        loadUserInfo();
        textRightTitle.setVisibility(View.VISIBLE);
        textRightTitle.setText("投诉");
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_show_user;
    }

    private void loadUserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        HaoConnect.loadContent("user/detail", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                textName.setText(result.findAsString("nickname"));
                textId.setText(result.findAsString("id"));
                textSex.setText(result.findAsString("sexLabel"));
                String[] area = result.findAsString("areaLabel").split("-");
                String city = "";
                if (area.length > 0) {
                    city = area[0];
                }
                textCity.setText(city);
                textAge.setText(result.findAsString("age"));
                textStar.setText(result.findAsString("constellation"));
                textSign.setText(result.findAsString("declaration"));
                ImageLoader.getInstance().displayImage(result.findAsString("avatarPreView"), imageHead);
                int userFriendType = result.findAsInt("userFriendType");
                textBlack.setVisibility(View.GONE);
                if (userFriendType == 0) {
                    isFriend = false;
                    textAddFriend.setText("关注");
                } else if (userFriendType == 1) {
                    isFriend = true;
                    textAddFriend.setText("已关注");
                } else if (userFriendType == 5) {
                    isFriend = true;
                    textAddFriend.setText("相互关注");
                    textBlack.setText("加入黑名单");
                    textBlack.setVisibility(View.VISIBLE);
                } else if (userFriendType == 7) {
                    isFriend = true;
                    textAddFriend.setText("已拉黑");
                    textBlack.setVisibility(View.VISIBLE);
                    textBlack.setText("移除黑名单");
                } else {
                    isFriend = false;
                    textAddFriend.setText("关注");
                }
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    @OnClick({R.id.text_add_friend, R.id.text_msg, R.id.text_right_title})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_add_friend:
                if (isFriend) {
                    deleteFriend();
                } else {
                    addFriend();
                }
                break;
            case R.id.text_msg:

                // 跳转到聊天界面，开始聊天
                Intent intent = new Intent(this, ChatActivity.class);
                // EaseUI封装的聊天界面需要这两个参数，聊天者的username，以及聊天类型，单聊还是群聊
                intent.putExtra("userId", userId);
                intent.putExtra("chatType", EMMessage.ChatType.Chat);
                startActivity(intent);
                break;
            case R.id.text_right_title: {
                Intent intent1 = new Intent(this, WarningActivity.class);
                intent1.putExtra("userId", userId);
                startActivity(intent1);
            }
            break;
        }
    }

    private void addFriend() {
        Map<String, Object> map = new HashMap<>();
        map.put("to_user_id", userId);
        startLoading();
        HaoConnect.loadContent("user_friends/add", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                if (result.isResultsOK()) {
                    showToast("关注成功");
                    isFriend = true;
                    textAddFriend.setText("已关注");
                }
                stopLoading();
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
                stopLoading();
            }
        }, this);
    }

    private void deleteFriend() {
        Map<String, Object> map = new HashMap<>();
        map.put("to_user_id", userId);
        startLoading();
        HaoConnect.loadContent("user_friends/remove", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                if (result.isResultsOK()) {
                    showToast("删除成功");
                    isFriend = false;
                    textAddFriend.setText("关注");
                }
                stopLoading();
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
                stopLoading();
            }
        }, this);
    }

    @OnClick(R.id.text_black)
    public void onViewClicked() {
        if (textBlack.getText().equals("加入黑名单")) {
            Map<String, Object> map = new HashMap<>();
            map.put("to_user_id", userId);
            startLoading();
            HaoConnect.loadContent("user_friends/blacklist", map, "post", new HaoResultHttpResponseHandler() {
                @Override
                public void onSuccess(HaoResult result) {
                    if (result.isResultsOK()) {
                        showToast("加入成功");
                        textBlack.setText("移除黑名单");
                    }
                    stopLoading();
                }

                @Override
                public void onFail(HaoResult result) {
                    showToast(result.errorStr);
                    stopLoading();
                }
            }, this);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("to_user_id", userId);
            startLoading();
            HaoConnect.loadContent("user_friends/remove", map, "post", new HaoResultHttpResponseHandler() {
                @Override
                public void onSuccess(HaoResult result) {
                    if (result.isResultsOK()) {
                        showToast("移除成功");
                        textBlack.setText("加入黑名单");
                    }
                    stopLoading();
                }

                @Override
                public void onFail(HaoResult result) {
                    showToast(result.errorStr);
                    stopLoading();
                }
            }, this);
        }
    }

}
