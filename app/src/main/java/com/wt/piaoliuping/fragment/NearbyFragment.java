package com.wt.piaoliuping.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.activity.FriendListTitleActivity;
import com.wt.piaoliuping.activity.NearbyTitleActivity;
import com.wt.piaoliuping.activity.SearchUserTitleActivity;
import com.wt.piaoliuping.base.PageFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/25.
 */

public class NearbyFragment extends PageFragment {
    @BindView(R.id.layout_1)
    LinearLayout layout1;
    @BindView(R.id.layout_2)
    LinearLayout layout2;
    @BindView(R.id.layout_3)
    LinearLayout layout3;

    @Override
    public void initView(View view) {
        super.initView(view);

        setTitle("发现");
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_nearby;
    }

    @OnClick({R.id.layout_1, R.id.layout_2, R.id.layout_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_1:
                startActivity(new Intent(getActivity(), NearbyTitleActivity.class));
                break;
            case R.id.layout_2:
                startActivity(new Intent(getActivity(), SearchUserTitleActivity.class));
                break;
            case R.id.layout_3:
                startActivity(new Intent(getActivity(), FriendListTitleActivity.class));
                break;
        }
    }
}
