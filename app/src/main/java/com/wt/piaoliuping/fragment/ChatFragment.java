package com.wt.piaoliuping.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.util.PathUtil;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.activity.GoodsListActivity;
import com.wt.piaoliuping.activity.RechargeListActivity;
import com.wt.piaoliuping.activity.ShowUserActivity;
import com.wt.piaoliuping.activity.VideoCallActivity;
import com.wt.piaoliuping.activity.VoiceCallActivity;
import com.wt.piaoliuping.view.TabToast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangtao on 2017/12/2.
 */

public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {

    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;
    private static final int ITEM_VIDEO_PRIZE = 15;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;

    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    private static final int MESSAGE_TYPE_RECALL = 9;
    //red packet code : 红包功能使用的常量
    private static final int MESSAGE_TYPE_RECV_RED_PACKET = 5;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET = 6;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET_ACK = 7;
    private static final int MESSAGE_TYPE_RECV_RED_PACKET_ACK = 8;
    private static final int MESSAGE_TYPE_RECV_RANDOM = 11;
    private static final int MESSAGE_TYPE_SEND_RANDOM = 12;
    private static final int ITEM_RED_PACKET = 16;

    /* @Override
     protected void sendVoiceMessage(final String filePath, final int length) {

 //        if (App.app.userInfo.findAsInt("sex") == 1) {
             Map<String, Object> map = new HashMap<>();
             map.put("to_user_id", toChatUsername);
             map.put("message_type", 2);
             HaoConnect.loadContent("user_messages/add", map, "post", new HaoResultHttpResponseHandler() {
                 @Override
                 public void onSuccess(HaoResult result) {
                     if (result.findAsInt("chatCount") <= 0) {
                         showCharge();
                     } else {
                         ChatFragment.super.sendVoiceMessage(filePath, length);
                     }
                 }

                 @Override
                 public void onFail(HaoResult result) {
                     showCharge();
                 }
             }, getActivity());
 //        } else {
 //            super.sendVoiceMessage(filePath, length);
 //        }

         *//*if (App.app.userInfo.findAsInt("vipLevel") == 0) {
            showCharge();
        } else {
            ChatFragment.super.sendVoiceMessage(filePath, length);
        }*//*
    }
*/
    @Override
    protected void sendMessage(final EMMessage message) {

        /*
        if (EMMessage.Type.TXT == message.getType()) {

            ChatFragment.super.sendMessage(message);
        } else {
            if (App.app.userInfo.findAsInt("vipLevel") == 0) {
                showCharge();
            } else {
                ChatFragment.super.sendMessage(message);
            }
        }*/

//        if (App.app.userInfo.findAsInt("sex") == 1) {
        Map<String, Object> map = new HashMap<>();
        map.put("to_user_id", toChatUsername);
        if (EMMessage.Type.TXT == message.getType()) {
            map.put("message_type", 1);
            EMTextMessageBody body = (EMTextMessageBody) message.getBody();
            map.put("message", body.getMessage());
        } else if (EMMessage.Type.IMAGE == message.getType()) {
            map.put("message_type", 4);
        } else if (EMMessage.Type.VIDEO == message.getType()) {
            map.put("message_type", 6);
        } else if (EMMessage.Type.VOICE == message.getType()) {
            map.put("message_type", 2);
        } else if (EMMessage.Type.LOCATION == message.getType()) {
            map.put("message_type", 5);
        }
        HaoConnect.loadContent("user_messages/add", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                //消息体发送
                ChatFragment.super.sendMessage(message);
//                if (result.findAsInt("chatCount") <= 0) {
//
//                    showCharge();
//                } else {
//
//                }
            }

            @Override
            public void onFail(HaoResult result) {
//                    showCharge();
                showError(result.errorStr);
//                Toast.makeText(getActivity(), result.errorStr, Toast.LENGTH_LONG);
            }
        }, getActivity());
//        } else {
//            super.sendMessage(message);
//        }
    }

    private void showError(final String msg) {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                TabToast.makeText(msg);
            }
        });
    }

    private void showCharge() {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("您当日签到赠送星星已消耗完，请充值星星继续使用")
                        .setNeutralButton("详情", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TabToast.makeText("跳转一个链接");
                            }
                        })
                        .setPositiveButton("去看看", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getActivity(), RechargeListActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("明天聊", null)
                        .create()
                        .show();
            }
        });
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
        inputAtUsername(username);
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
        switch (itemId) {
            case ITEM_VIDEO:
                break;
            case ITEM_FILE: //file
                break;
            case ITEM_VOICE_CALL: {
//                if (App.app.userInfo.findAsInt("sex") == 1) {
                Map<String, Object> map = new HashMap<>();
                map.put("to_user_id", toChatUsername);
                map.put("message_type", 7);
                HaoConnect.loadContent("user_messages/add", map, "post", new HaoResultHttpResponseHandler() {
                    @Override
                    public void onSuccess(HaoResult result) {
                        if (result.findAsInt("chatCount") <= 0) {
                            showCharge();
                        } else {
                            startVoiceCall();
                        }
                    }

                    @Override
                    public void onFail(HaoResult result) {
//                        Toast.makeText(getActivity(), result.errorStr, Toast.LENGTH_LONG);
                        showError(result.errorStr);
                    }
                }, getActivity());
//                } else {
//                    startVoiceCall();
//                }
                /*if (App.app.userInfo.findAsInt("vipLevel") == 0) {
                    showCharge();
                } else {
                    startVoiceCall();
                }*/
            }
            break;
            case ITEM_VIDEO_CALL: {
//                if (App.app.userInfo.findAsInt("sex") == 1) {
                Map<String, Object> map = new HashMap<>();
                map.put("to_user_id", toChatUsername);
                map.put("message_type", 6);
                HaoConnect.loadContent("user_messages/add", map, "post", new HaoResultHttpResponseHandler() {
                    @Override
                    public void onSuccess(HaoResult result) {
                        if (result.findAsInt("chatCount") <= 0) {
                            showCharge();
                        } else {
                            startVideoCall();
                        }
                    }

                    @Override
                    public void onFail(HaoResult result) {
//                        Toast.makeText(getActivity(), result.errorStr, Toast.LENGTH_LONG);
                        showError(result.errorStr);
                    }
                }, getActivity());
//                } else {
//                    startVideoCall();
//                }

                /*if (App.app.userInfo.findAsInt("vipLevel") == 0) {
                    showCharge();
                } else {
                    startVideoCall();
                }*/

                break;
            }
            case ITEM_VIDEO_PRIZE: {
                startPrize();
                break;
            }
            default:
                break;
        }
        return false;
    }

    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // voiceCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            inputMenu.hideExtendMenuContainer();
        }
    }

    private void startPrize() {
        Intent intent = new Intent(getActivity(), GoodsListActivity.class);
        intent.putExtra("userId", toChatUsername);
        intent.putExtra("send", true);
        startActivity(intent);
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }

    @Override
    protected void registerExtendMenuItem() {
        super.registerExtendMenuItem();
//        inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
//        inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.prize, R.drawable.em_chat_prize_call_selector, ITEM_VIDEO_PRIZE, extendMenuItemClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_AT_USER:
                    if (data != null) {
                        String username = data.getStringExtra("username");
                        inputAtUsername(username, false);
                    }
                    break;
                default:
                    break;
            }
        }

    }

}
