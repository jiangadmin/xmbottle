package com.wt.piaoliuping.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.activity.PointActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangtao on 2017/12/2.
 */

public class ChatFragment extends EaseChatFragment {

    boolean send = true;

    @Override
    protected void sendMessage(EMMessage message) {
        if (send) {
            super.sendMessage(message);
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage("您当前星星不足，请前往积分中心充值")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(getActivity(), PointActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("to_user_id", toChatUsername);
        HaoConnect.loadContent("user_messages/chat_count", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                if (result.findAsInt("chatCount") <= 0) {
                    send = false;
                } else {
                    send = true;
                }
            }

            @Override
            public void onFail(HaoResult result) {
            }
        }, getActivity());
    }
}
