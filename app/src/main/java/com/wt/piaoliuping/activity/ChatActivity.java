package com.wt.piaoliuping.activity;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseActivity;
import com.wt.piaoliuping.fragment.ChatFragment;

/**
 * Created by wangtao on 2017/11/5.
 */

public class ChatActivity extends BaseActivity {
    @Override
    public void initView() {

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_content, fragment)
                .commit();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_chat;
    }

}
