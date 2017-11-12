package com.wt.piaoliuping.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.activity.SendBottleActivity;
import com.wt.piaoliuping.activity.ShowBottleActivity;
import com.wt.piaoliuping.base.PageFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/10/25.
 */

public class BottleFragment extends PageFragment {
    @BindView(R.id.text_send)
    TextView textSend;
    @BindView(R.id.text_received)
    TextView textReceived;

    @Override
    public void initView(View view) {
        super.initView(view);
        setTitle("瓶子");
    }

    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_bottle;
    }

    @OnClick(R.id.text_send)
    public void onTextSendClicked() {
        startActivity(new Intent(getActivity(), SendBottleActivity.class));
    }

    @OnClick(R.id.text_received)
    public void onTextReceivedClicked() {
        Map<String, Object> map = new HashMap<>();

        HaoConnect.loadContent("bottle_message/get", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                Intent intent = new Intent(getActivity(), ShowBottleActivity.class);
                intent.putExtra("msgId", result.findAsString("id"));
                startActivity(intent);
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, getActivity());
    }
}
