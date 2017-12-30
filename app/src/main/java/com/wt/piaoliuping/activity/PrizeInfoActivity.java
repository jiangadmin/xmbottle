package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/12/30.
 */

public class PrizeInfoActivity extends BaseTitleActivity {
    @BindView(R.id.text_star)
    TextView textStar;
    @BindView(R.id.text_share)
    TextView textShare;
    @BindView(R.id.text_star_1)
    TextView textStar1;
    @BindView(R.id.text_star_2)
    TextView textStar2;
    @BindView(R.id.btn_withdrew)
    Button btnWithdrew;
    @BindView(R.id.text_desc)
    TextView textDesc;

    @Override
    public void initView() {
        setTitle("查看奖励");
        loadDetail();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_prize_info;
    }

    @OnClick(R.id.btn_withdrew)
    public void onViewClicked() {
        startActivity(new Intent(this, WithdrewActivity.class));
    }

    private void loadDetail() {
        Map<String, Object> map = new HashMap<>();
        HaoConnect.loadContent("user_invite/invite_info", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                textShare.setText(result.findAsString("myInviteUserCount") + "人");
                textStar.setText(result.findAsString("myTotalAmount") + "人");
                textStar1.setText(result.findAsString("userAmount"));
                textStar2.setText(result.findAsString("unrevdAmount"));
                textDesc.setText(result.findAsString("balanceRule"));
            }

            @Override
            public void onFail(HaoResult result) {
            }
        }, this);
    }
}
