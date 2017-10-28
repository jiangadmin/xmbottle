package com.wt.piaoliuping.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/28.
 */

public class ShowUserActivity extends BaseActivity {

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
    private String userId;

    boolean isFriend;

    @Override
    public void initView() {

        userId = getIntent().getStringExtra("userId");
        loadUserInfo();
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
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    @OnClick({R.id.text_add_friend, R.id.text_msg})
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
                break;
        }
    }

    private void addFriend() {
        Map<String, Object> map = new HashMap<>();
        map.put("to_user_id", userId);
        HaoConnect.loadContent("user_friends/add", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                if (result.isResultsOK()) {
                    showToast("关注成功");
                    isFriend = true;
                    textAddFriend.setText("已关注");
                }
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    private void deleteFriend() {
        Map<String, Object> map = new HashMap<>();
        map.put("to_user_id", userId);
        HaoConnect.loadContent("user_friends/remove", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                if (result.isResultsOK()) {
                    showToast("删除成功");
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
}
