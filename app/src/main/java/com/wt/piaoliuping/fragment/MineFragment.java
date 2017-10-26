package com.wt.piaoliuping.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.activity.NameListActivity;
import com.wt.piaoliuping.activity.PointActivity;
import com.wt.piaoliuping.activity.SettingActivity;
import com.wt.piaoliuping.activity.WithdrewActivity;
import com.wt.piaoliuping.base.PageFragment;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
                startActivity(new Intent(getActivity(), PointActivity.class));
                break;
            case R.id.layout_2:
                startActivity(new Intent(getActivity(), NameListActivity.class));
                break;
            case R.id.layout_3:
                startActivity(new Intent(getActivity(), WithdrewActivity.class));
                break;
            case R.id.layout_4:
                break;
            case R.id.layout_5:
                break;
            case R.id.layout_6:
                break;
            case R.id.layout_7:
                break;
            case R.id.layout_8:
                startActivity(new Intent(getActivity(), SettingActivity.class));
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
                textName.setText("昵称：" + (TextUtils.isEmpty(result.findAsString("nickname")) ? result.findAsString("username") : result.findAsString("nickname")));
                textNo.setText("ID：" + result.findAsString("id"));
            }
        }, getActivity());
    }
}