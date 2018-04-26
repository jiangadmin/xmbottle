package com.wt.piaoliuping.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.App;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.activity.FeedbackActivity;
import com.wt.piaoliuping.activity.FollowListActivity;
import com.wt.piaoliuping.activity.GoodsListActivity;
import com.wt.piaoliuping.activity.LoginActivity;
import com.wt.piaoliuping.activity.MinePrizeListActivity;
import com.wt.piaoliuping.activity.PrizeInfoActivity;
import com.wt.piaoliuping.activity.PrizeListActivity;
import com.wt.piaoliuping.activity.RechargeListActivity;
import com.wt.piaoliuping.activity.RevokeListActivity;
import com.wt.piaoliuping.activity.SettingTitleActivity;
import com.wt.piaoliuping.activity.ShareInfoActivity;
import com.wt.piaoliuping.activity.UserInfoActivity;
import com.wt.piaoliuping.activity.VipActivity;
import com.wt.piaoliuping.base.AppManager;
import com.wt.piaoliuping.base.PageFragment;
import com.wt.piaoliuping.manager.UserManager;
import com.wt.piaoliuping.utils.DateUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/25.
 */

public class MineFragment extends PageFragment {
    private static final String TAG = "MineFragment";

    @BindView(R.id.image_head)
    ImageView imageHead;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_no)
    TextView textNo;
    @BindView(R.id.text_expire_time)
    TextView textExpireTime;
    @BindView(R.id.layout_1)
    LinearLayout layout1;
    @BindView(R.id.layout_2)
    LinearLayout layout2;
    @BindView(R.id.layout_3)
    LinearLayout layout3;
    @BindView(R.id.layout_4)
    LinearLayout layout4;
    @BindView(R.id.layout_5)
    LinearLayout layout5;
    @BindView(R.id.layout_6)
    LinearLayout layout6;
    @BindView(R.id.layout_7)
    LinearLayout layout7;

    @BindView(R.id.layout_21)
    LinearLayout layout21;
    @BindView(R.id.layout_22)
    LinearLayout layout22;
    @BindView(R.id.btn_right_title)
    ImageButton btnRightTitle;

    @BindView(R.id.mine_info)
    RelativeLayout mine_info;

    private String userId = "";

    private boolean inited = false;

    @Override
    public void initView(View view) {
        super.initView(view);
        setTitle("我的");

        try {
            ApplicationInfo appInfo = getActivity().getPackageManager()
                    .getApplicationInfo(getActivity().getPackageName(),
                            PackageManager.GET_META_DATA);
            String channel = appInfo.metaData.getString("CHANNEL");

            if ("dev".equals(channel)) {
                boolean isProd = false;
                if (null == HaoConnect.getString("sec") || HaoConnect.getString("sec").equals("prod")) {
                    isProd = true;
                }
                layout22.setVisibility(View.VISIBLE);
                final boolean finalIsProd = isProd;
                layout22.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("提示")
                                .setMessage(finalIsProd ? "是否需要切换测试环境" : "是否需要切换正式环境")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (finalIsProd) {
                                            HaoConnect.putString("sec", "dev");
                                        } else {
                                            HaoConnect.putString("sec", "prod");
                                        }
                                        EMClient.getInstance().logout(true);
                                        UserManager.getInstance().logout();
                                        AppManager.getInstance().finishAllActivity();
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create()
                                .show();
                    }
                });
            } else {
                layout22.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_mine;
    }

    @OnClick({R.id.layout_1, R.id.layout_2, R.id.layout_3, R.id.layout_4, R.id.layout_5, R.id.layout_6, R.id.layout_7, R.id.layout_8, R.id.btn_right_title,
            R.id.layout_21, R.id.layout_31, R.id.mine_info,R.id.mine_help,R.id.mine_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //个人资料
            case R.id.mine_info:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            //关注
            case R.id.layout_1:
                startActivity(new Intent(getActivity(), FollowListActivity.class));
                break;
            //黑名单
            case R.id.layout_2:
                startActivity(new Intent(getActivity(), RevokeListActivity.class));
                break;
            //充值
            case R.id.layout_3:
                startActivity(new Intent(getActivity(), RechargeListActivity.class));
                break;
            //礼物记录
            case R.id.layout_4:
                startActivity(new Intent(getActivity(), MinePrizeListActivity.class));
                break;
            //礼物列表
            case R.id.layout_5:
                startActivity(new Intent(getActivity(), PrizeListActivity.class));
                break;
            //礼品商场
            case R.id.layout_6:
                startActivity(new Intent(getActivity(), GoodsListActivity.class));
                break;
            //邀请奖励
            case R.id.layout_7: {
//                loadShare();
                Intent intent = new Intent(getActivity(), ShareInfoActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
            break;
            //设置中心
            case R.id.mine_setting:
            case R.id.btn_right_title:
            case R.id.layout_8:
                startActivity(new Intent(getActivity(), SettingTitleActivity.class));
                break;
            //联系客服
            case R.id.mine_help:
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;

            //查看奖励
            case R.id.mine_reward: {
                startActivity(new Intent(getActivity(), PrizeInfoActivity.class));
                break;
            }
            //充值
            case R.id.layout_21: {
                startActivity(new Intent(getActivity(), RechargeListActivity.class));
                break;
            }
            //会员中心
            case R.id.layout_31: {
//                showToast("正在升级中");
                startActivity(new Intent(getActivity(), VipActivity.class));
                break;
            }
        }
    }

    @Override
    protected void fetchData() {
        super.fetchData();
        loadUser();
    }

    private void loadUser() {
        userId = App.app.userInfo.findAsString("id");
        ImageLoader.getInstance().displayImage(App.app.userInfo.findAsString("avatarPreView"), imageHead, App.app.getImageCircleOptions());
        textName.setText(App.app.userInfo.findAsString("nickname"));
        textNo.setText("ID：" + App.app.userInfo.findAsString("id"));
        if (App.app.userInfo.findAsInt("vipLevel") == 0) {
            textExpireTime.setVisibility(View.GONE);
//                    layout5.setVisibility(View.GONE);
//                    layout4.setVisibility(View.VISIBLE);
        } else {
            textExpireTime.setVisibility(View.VISIBLE);
//                    layout4.setVisibility(View.GONE);
//                    layout5.setVisibility(View.VISIBLE);
        }
        long time = DateUtils.getTime(App.app.userInfo.findAsString("vipEndTime")) - System.currentTimeMillis() / 1000;
        long day = time / 24 / 3600;
        if (day > 0) {
            textExpireTime.setText("会员剩余：" + day + "天");
        }
    }
}
