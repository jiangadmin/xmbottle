package com.wt.piaoliuping.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
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
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.activity.FollowListActivity;
import com.wt.piaoliuping.activity.GoodsListActivity;
import com.wt.piaoliuping.activity.PointActivity;
import com.wt.piaoliuping.activity.PrizeListActivity;
import com.wt.piaoliuping.activity.RevokeListActivity;
import com.wt.piaoliuping.activity.SettingTitleActivity;
import com.wt.piaoliuping.base.PageFragment;

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

    @Override
    public void initView(View view) {
        super.initView(view);
        setTitle("设置");
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_setting;
    }

    @OnClick({R.id.image_head, R.id.layout_1, R.id.layout_2, R.id.layout_3, R.id.layout_4, R.id.layout_5, R.id.layout_6, R.id.layout_7, R.id.layout_8})
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
                startActivity(new Intent(getActivity(), PrizeListActivity.class));
                break;
            case R.id.layout_5:
                startActivity(new Intent(getActivity(), PrizeListActivity.class));
                break;
            case R.id.layout_6:
                startActivity(new Intent(getActivity(), GoodsListActivity.class));
                break;
            case R.id.layout_7: {
                new ShareAction(getActivity())
                        .withText("星梦漂流")
                        .setDisplayList(SHARE_MEDIA.WEIXIN)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onStart(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onResult(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .open();

            }
            break;
            case R.id.layout_8:
                startActivity(new Intent(getActivity(), SettingTitleActivity.class));
                break;
        }
    }

    @Override
    protected void fetchData() {
        super.fetchData();
        loadUser();
    }

    private void loadUser() {
        HaoConnect.loadContent("user/my_detail", null, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ImageLoader.getInstance().displayImage(result.findAsString("avatarPreView"), imageHead);
                textName.setText("昵称：" + result.findAsString("nickname"));
                textNo.setText("ID：" + result.findAsString("id"));
                /*if (result.findAsInt("vipLevel") == 0) {
                    layout5.setVisibility(View.GONE);
                    layout4.setVisibility(View.VISIBLE);
                } else {
                    layout4.setVisibility(View.GONE);
                    layout5.setVisibility(View.VISIBLE);
                }*/
            }
        }, getActivity());
    }

}
