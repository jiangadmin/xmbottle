package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.chat.EMMessage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.App;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;
import com.wt.piaoliuping.db.UserDao;
import com.wt.piaoliuping.db.UserDaoDao;
import com.wt.piaoliuping.manager.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/28.
 */

public class ShowUserActivity extends BaseTitleActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    //    @BindView(R.id.image_head)
//    ImageView imageHead;
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
    @BindView(R.id.text_num)
    TextView textNum;
    @BindView(R.id.text_phone)
    TextView textPhone;
    @BindView(R.id.layout_photo)
    LinearLayout layoutPhoto;
    private String userId;
    private String userName;

    boolean isFriend;
    private List<String> images = new ArrayList<>();

    @Override
    public void initView() {

        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");

        if (TextUtils.isEmpty(userId)) {
            showToast("用户数据错误");
            finish();
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            userName = "";
        }
        setTitle(userName);
        loadUserInfo();
        textRightTitle.setVisibility(View.VISIBLE);
        textRightTitle.setText("投诉");
        loadData();
        viewPager.addOnPageChangeListener(this);
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
                textPhone.setText(result.findAsString("username"));
//                ImageLoader.getInstance().displayImage(result.findAsString("avatarPreView"), imageHead);
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
                images.add(result.findAsString("avatarPreView"));
                pagerAdapter = new ImagePagerAdapter(images);
                viewPager.setAdapter(pagerAdapter);
                textNum.setText(1 + "/" + pagerAdapter.getCount());

                UserDaoDao userDao = App.app.getDaoSession().getUserDaoDao();
                UserDao userDao1 = new UserDao();
                userDao1.setAvatar(result.findAsString("avatarPreView"));
                userDao1.setUserName(result.findAsString("id"));
                userDao1.setNick(result.findAsString("nickname"));
                userDao.insertOrReplace(userDao1);
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    @OnClick({R.id.text_add_friend, R.id.text_msg, R.id.text_right_title, R.id.layout_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_add_friend:
                if (isFriend) {
                    deleteFriend();
                } else {
                    addFriend();
                }
                break;
            case R.id.text_msg: {

                // 跳转到聊天界面，开始聊天
                Intent intent = new Intent(this, ChatActivity.class);
                // EaseUI封装的聊天界面需要这两个参数，聊天者的username，以及聊天类型，单聊还是群聊
                intent.putExtra("userId", userId);
                intent.putExtra("chatType", EMMessage.ChatType.Chat);
                startActivity(intent);
            }
            break;
            case R.id.text_right_title: {
                Intent intent1 = new Intent(this, WarningActivity.class);
                intent1.putExtra("userId", userId);
                startActivity(intent1);
            }
            break;
            case R.id.layout_photo: {
                Intent intent = new Intent(this, PhotosActivity.class);
                intent.putExtra("show", true);
                intent.putExtra("userId", userId);
                startActivity(intent);
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
                    loadData();
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
                    loadData();
                }

                @Override
                public void onFail(HaoResult result) {
                    showToast(result.errorStr);
                    stopLoading();
                }
            }, this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        textNum.setText(position + 1 + "/" + pagerAdapter.getCount());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class ImagePagerAdapter extends PagerAdapter {

        private List<String> imageList = new ArrayList<>();

        private List<ImageView> viewList = new ArrayList<>();

        public ImagePagerAdapter(List<String> imageList) {
            this.imageList = imageList;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public int getCount() {

            return imageList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(ShowUserActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(imageList.get(position), imageView);
            container.addView(imageView);
            viewList.add(imageView);
            return imageView;
        }
    }

    PagerAdapter pagerAdapter;

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        map.put("size", "999");
        if (!TextUtils.isEmpty(userId)) {
            map.put("user_id", userId);
        }
        HaoConnect.loadContent("user_photos/list", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ArrayList<Object> lists = result.findAsList("results>");
                for (Object object : lists) {
                    images.add(((HaoResult) object).findAsString("photoPreview"));
                }
                pagerAdapter = new ImagePagerAdapter(images);
                viewPager.setAdapter(pagerAdapter);
                textNum.setText(1 + "/" + pagerAdapter.getCount());
            }

            @Override
            public void onFail(HaoResult result) {
//                showToast(result.errorStr);
            }
        }, this);
    }

}
