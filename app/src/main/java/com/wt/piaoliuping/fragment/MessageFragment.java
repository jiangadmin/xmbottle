package com.wt.piaoliuping.fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.PageFragment;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/10/25.
 */

public class MessageFragment extends PageFragment {
    @BindView(R.id.fragment)
    FrameLayout fragment;
    MessageListFragment messageListFragment;

    private boolean inited = false;

    @Override
    public void initView(View view) {

        super.initView(view);
        setTitle("消息");
        if (inited == false) {
            inited = true;
            messageListFragment = new MessageListFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.fragment, messageListFragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_message;
    }

    public void refresh() {
        if (messageListFragment != null) {
            messageListFragment.refresh();
        }
    }
}
