package com.wt.piaoliuping.activity;

import com.hyphenate.easeui.ui.EaseChatFragment;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseActivity;

/**
 * Created by wangtao on 2017/11/5.
 */

public class ChatActivity extends BaseActivity {
    @Override
    public void initView() {

        EaseChatFragment fragment = new EaseChatFragment();
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
