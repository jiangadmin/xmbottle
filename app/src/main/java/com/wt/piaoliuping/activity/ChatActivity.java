package com.wt.piaoliuping.activity;

import android.support.annotation.NonNull;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseActivity;
import com.wt.piaoliuping.fragment.ChatFragment;
import com.wt.piaoliuping.runtimepermissions.PermissionsManager;

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
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_chat;
    }


//    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                                     @NonNull int[] grantResults) {
//        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
//    }
}
