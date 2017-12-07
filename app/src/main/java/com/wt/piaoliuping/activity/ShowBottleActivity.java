package com.wt.piaoliuping.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/28.
 */

public class ShowBottleActivity extends BaseActivity {
    @BindView(R.id.image_head)
    ImageView imageHead;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_sex)
    TextView textSex;
    @BindView(R.id.text_time)
    TextView textTime;
    @BindView(R.id.text_content)
    TextView textContent;
    @BindView(R.id.btn_left)
    Button btnLeft;
    @BindView(R.id.btn_msg)
    Button btnMsg;
    @BindView(R.id.voice_content)
    ImageButton voiceContent;
    @BindView(R.id.image_content)
    ImageView imageContent;

    private String msgId;

    @Override
    public void initView() {
//        setTitle("来自***的瓶子");
        msgId = getIntent().getStringExtra("msgId");
        loadDetail();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_show_bottle;
    }

    @OnClick({R.id.btn_left, R.id.btn_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                left();
                break;
            case R.id.btn_msg:
                msg();
                break;
        }
    }

    private void loadDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", msgId);
        startLoading();
        HaoConnect.loadContent("user_get_bottle/detail", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ImageLoader.getInstance().displayImage(result.findAsString("throwUserLocal>avatarPreView"), imageHead);
                String[] area = result.findAsString("throwUserLocal>areaLabel").split("-");
                String city = "";
                if (area.length > 0) {
                    city = area[0];
                }
                textName.setText("[" + city + "]" + result.findAsString("throwUserLocal>nickname"));
                textSex.setText(result.findAsString("throwUserLocal>sexLabel") + " " + result.find("throwUserLocal>age") + result.find("throwUserLocal>constellation"));
                textTime.setText(result.findAsString("throwUserLocal>createTime"));
                setTitle("来自" + city + "的瓶子");

                textContent.setText(result.findAsString("message"));
                stopLoading();
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
                stopLoading();
            }
        }, this);
    }

    private void left() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", msgId);
        HaoConnect.loadContent("bottle_message/throw", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                finish();
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    private void msg() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", msgId);
        HaoConnect.loadContent("bottle_message/view", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                Intent intent = new Intent(ShowBottleActivity.this, ShowUserActivity.class);
                intent.putExtra("userId", result.findAsString("throwUserLocal>id"));
                intent.putExtra("userName", result.findAsString("throwUserLocal>nickname"));
                startActivity(intent);
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    @OnClick(R.id.voice_content)
    public void onViewClicked() {
    }

    MediaPlayer mediaPlayer = null;

    public void playVoice(String filePath) {
        if (!(new File(filePath).exists())) {
            return;
        }
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer = new MediaPlayer();
        if (EaseUI.getInstance().getSettingsProvider().isSpeakerOpened()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopPlayVoice(); // stop animation
                }

            });
            mediaPlayer.start();

        } catch (Exception e) {
            System.out.println();
        }
    }


    public void stopPlayVoice() {
        // stop play voice
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
