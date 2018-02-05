package com.wt.piaoliuping.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.wt.piaoliuping.App;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.activity.FeedbackActivity;
import com.wt.piaoliuping.activity.FollowListActivity;
import com.wt.piaoliuping.activity.GoodsListActivity;
import com.wt.piaoliuping.activity.HomeActivity;
import com.wt.piaoliuping.activity.LoginActivity;
import com.wt.piaoliuping.activity.MinePrizeListActivity;
import com.wt.piaoliuping.activity.PointActivity;
import com.wt.piaoliuping.activity.PrizeInfoActivity;
import com.wt.piaoliuping.activity.PrizeListActivity;
import com.wt.piaoliuping.activity.RechargeListActivity;
import com.wt.piaoliuping.activity.RecommendActivity;
import com.wt.piaoliuping.activity.RevokeListActivity;
import com.wt.piaoliuping.activity.SettingTitleActivity;
import com.wt.piaoliuping.activity.ShareInfoActivity;
import com.wt.piaoliuping.base.AppManager;
import com.wt.piaoliuping.base.PageFragment;
import com.wt.piaoliuping.manager.UserManager;
import com.wt.piaoliuping.utils.DateUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/25.
 */

public class MineFragment extends PageFragment {
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
    @BindView(R.id.layout_9)
    LinearLayout layout9;
    @BindView(R.id.layout_10)
    LinearLayout layout10;
    @BindView(R.id.layout_21)
    LinearLayout layout21;
    @BindView(R.id.layout_22)
    LinearLayout layout22;
    @BindView(R.id.btn_right_title)
    ImageButton btnRightTitle;

    @BindView(R.id.layout_top)
    LinearLayout layoutTop;

    private String userId = "";

    private boolean inited = false;

    @Override
    public void initView(View view) {
        super.initView(view);
        setTitle("设置");
        btnRightTitle.setBackgroundResource(R.drawable.ic_setting);
        btnRightTitle.setVisibility(View.VISIBLE);

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
        return R.layout.fragment_setting;
    }

    @OnClick({R.id.image_head, R.id.layout_1, R.id.layout_2, R.id.layout_3, R.id.layout_4, R.id.layout_5, R.id.layout_6, R.id.layout_7, R.id.layout_8, R.id.layout_9, R.id.layout_10, R.id.btn_right_title,
            R.id.layout_21})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_head:
                break;
            case R.id.layout_1:
                startActivity(new Intent(getActivity(), FollowListActivity.class));
                break;
            case R.id.layout_2:
                startActivity(new Intent(getActivity(), RevokeListActivity.class));
                break;
            case R.id.layout_3:
                startActivity(new Intent(getActivity(), PointActivity.class));
                break;
            case R.id.layout_4:
                startActivity(new Intent(getActivity(), MinePrizeListActivity.class));
                break;
            case R.id.layout_5:
                startActivity(new Intent(getActivity(), PrizeListActivity.class));
                break;
            case R.id.layout_6:
                startActivity(new Intent(getActivity(), GoodsListActivity.class));
                break;
            case R.id.layout_7: {
//                loadShare();
                Intent intent = new Intent(getActivity(), ShareInfoActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
            break;
            case R.id.btn_right_title:
            case R.id.layout_8:
                startActivity(new Intent(getActivity(), SettingTitleActivity.class));
                break;
            case R.id.layout_9: {
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;

            }
            case R.id.layout_10: {
                startActivity(new Intent(getActivity(), PrizeInfoActivity.class));
                break;
            }
            case R.id.layout_21: {
                startActivity(new Intent(getActivity(), RechargeListActivity.class));
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
        textName.setText("昵称：" + App.app.userInfo.findAsString("nickname"));
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
