package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/28.
 */

public class ShowBottleTitleActivity extends BaseTitleActivity {
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

    private String msgId;

    @Override
    public void initView() {
        setTitle("来自***的瓶子");

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

                textContent.setText(result.findAsString("message"));
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
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
                Intent intent = new Intent(ShowBottleTitleActivity.this, ShowUserTitleActivity.class);
                intent.putExtra("userId", result.findAsString("throwUserLocal>id"));
                startActivity(intent);
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }
}
