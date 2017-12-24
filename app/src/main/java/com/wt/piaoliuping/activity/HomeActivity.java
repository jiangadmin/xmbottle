package com.wt.piaoliuping.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
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
import com.wt.piaoliuping.manager.HXManager;
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
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.MODIFY_AUDIO_SETTINGS;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.hyphenate.easeui.EaseConstant.ACTION_GROUP_CHANAGED;

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
    private MessageFragment messageFragment = new MessageFragment();

    @Override
    public void initView() {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager(), messageFragment);
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
        registerBroadcastReceiver();
        update();

        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
            }

            @Override
            public void onDisconnected(int errorCode) {
                if (errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(HomeActivity.this)
                                    .setTitle("提示")
                                    .setMessage("您的账号已在其他设备登陆，如非本人操作请修改密码")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            messageFragment.refresh();
                                            UserManager.getInstance().logout();
                                            AppManager.getInstance().finishAllActivity();
                                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(intent);
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();
                        }
                    });
                }
            }
        });


        new Thread(new Runnable() {
            public void run() {
                EMClient.getInstance().login(UserManager.getInstance().getUserId(), HXManager.getInstance().formatPassword(UserManager.getInstance().getUserId()), new EMCallBack() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(int code, final String error) {
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
            }
        }).start();

        rxPermissions = new RxPermissions(this);
        handleLocation();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_home;
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, MessageFragment messageFragment) {
            super(fm);
            fragments.add(messageFragment);
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
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION, RECORD_AUDIO, CAMERA, WRITE_EXTERNAL_STORAGE, MODIFY_AUDIO_SETTINGS)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                        } else {
                            showToast("已禁止相关权限，您可以在系统设置中打开");
                        }
                    }
                });
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                App.app.getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
//                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                    RedPacketUtil.receiveRedPacketAckMessage(message);
//                }
            }
            //end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };


    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
//                updateUnreadLabel();
                if (radioGroup.getCheckedRadioButtonId() == R.id.message_btn) {
                    // refresh conversation list
                    if (messageFragment != null) {
                        messageFragment.refresh();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        super.onStop();
    }


    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EaseConstant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(ACTION_GROUP_CHANAGED);
//        intentFilter.addAction(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
//                updateUnreadLabel();
//                updateUnreadAddressLable();
                if (radioGroup.getCheckedRadioButtonId() == R.id.message_btn) {
                    // refresh conversation list
                    if (messageFragment != null) {
                        messageFragment.refresh();
                    }
                }
                String action = intent.getAction();
//                if (action.equals(ACTION_GROUP_CHANAGED)) {
//                    if (EaseCommonUtils.getTopActivity(HomeActivity.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
//                }
                //red packet code : 处理红包回执透传消息
//                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
//                }
                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
    }
}
