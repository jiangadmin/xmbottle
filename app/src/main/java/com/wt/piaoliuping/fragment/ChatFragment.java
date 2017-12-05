package com.wt.piaoliuping.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.App;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.activity.PointActivity;
import com.wt.piaoliuping.activity.ShowBottleActivity;
import com.wt.piaoliuping.activity.ShowUserActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangtao on 2017/12/2.
 */

public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {

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
        if (EMMessage.Type.TXT == message.getType()) {
            map.put("message_type", 1);
            EMTextMessageBody body = (EMTextMessageBody) message.getBody();
            map.put("message", body.getMessage());
        } else if (EMMessage.Type.IMAGE == message.getType()) {
            map.put("message_type", 2);
        } else if (EMMessage.Type.VIDEO == message.getType()) {
            map.put("message_type", 3);
        }
        HaoConnect.loadContent("user_messages/add", map, "post", new HaoResultHttpResponseHandler() {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChatFragmentListener(this);

    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {

    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {

        Intent intent = new Intent(getActivity(), ShowUserActivity.class);

        intent.putExtra("userId", username);
        intent.putExtra("userName", EaseUI.getInstance().getUserProfileProvider().getUser(username).getNick());
        startActivity(intent);
    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }
}
