package com.wt.piaoliuping.activity;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.socialize.UMShareAPI;
import com.wt.piaoliuping.App;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.AppManager;
import com.wt.piaoliuping.base.BaseTitleActivity;
import com.wt.piaoliuping.fragment.BottleFragment;
import com.wt.piaoliuping.fragment.MessageFragment;
import com.wt.piaoliuping.fragment.MineFragment;
import com.wt.piaoliuping.fragment.NearbyFragment;
import com.wt.piaoliuping.manager.UserManager;
import com.wt.piaoliuping.utils.DateUtils;
import com.wt.piaoliuping.widgt.CustomViewPager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.functions.Action1;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

/**
 * Created by wangtao on 2017/10/20.
 */

public class HomeActivity extends BaseTitleActivity {

    @BindView(R.id.message_btn)
    RadioButton messageBtn;
    @BindView(R.id.home_btn)
    RadioButton homeBtn;
    @BindView(R.id.activity_btn)
    RadioButton activityBtn;
    @BindView(R.id.mine_btn)
    RadioButton mineBtn;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.view_pager)
    CustomViewPager viewPager;

    RxPermissions rxPermissions;
    @Override
    public void initView() {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.message_btn:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.home_btn:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.activity_btn:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.mine_btn:
                        viewPager.setCurrentItem(3);
                        break;
                }
            }
        });

        sign();

        update();

        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
            }

            @Override
            public void onDisconnected(int errorCode) {
                if (errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    UserManager.getInstance().logout();
                    AppManager.getInstance().finishAllActivity();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            }
        });

        rxPermissions = new RxPermissions(this);
        handleLocation();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_home;
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(new MessageFragment());
            fragments.add(new BottleFragment());
            fragments.add(new NearbyFragment());
            fragments.add(new MineFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
                exitTime = System.currentTimeMillis();
            } else {
                exitTime = 0;
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void sign() {
        if (!TextUtils.isEmpty(UserManager.getInstance().getUserId())) {
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", UserManager.getInstance().getUserId());
            map.put("sign_date", DateUtils.getStringDateAndTimeFromDate(new Date()));
            HaoConnect.loadContent("sign_in/add", map, "post", new HaoResultHttpResponseHandler() {
                @Override
                public void onSuccess(HaoResult result) {
                    showToast(result.findAsString("results>"));
                }

                @Override
                public void onFail(HaoResult result) {
                }
            }, this);
        }
    }


    private void update() {
        if (!TextUtils.isEmpty(UserManager.getInstance().getUserId())) {
            Map<String, Object> map = new HashMap<>();
            map.put("lat", App.app.latitude);
            map.put("lng", App.app.longitude);
            HaoConnect.loadContent("user_site/update", map, "post", new HaoResultHttpResponseHandler() {
                @Override
                public void onSuccess(HaoResult result) {
                }

                @Override
                public void onFail(HaoResult result) {
                }
            }, this);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private void handleLocation() {
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION ,
                ACCESS_COARSE_LOCATION)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                        } else {
                            showToast("已禁止定位权限，您可以在系统设置中打开");
                        }
                    }
                });

    }


}
