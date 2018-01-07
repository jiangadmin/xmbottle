package com.wt.piaoliuping.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.App;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.activity.SendBottleActivity;
import com.wt.piaoliuping.activity.ShowBottleActivity;
import com.wt.piaoliuping.base.PageFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wangtao on 2017/10/25.
 */

public class BottleFragment extends PageFragment {
    @BindView(R.id.image_head)
    ImageView imageHead;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_no)
    TextView textNo;
    @BindView(R.id.text_send)
    TextView textSend;
    @BindView(R.id.text_received)
    TextView textReceived;

    private boolean inited = false;

    @Override
    public void initView(View view) {
        super.initView(view);
        setTitle("瓶子");
    }

    @Override
    public void fetchData() {
        super.onStart();
        loadUser();
    }


    @Override
    public int getFragmentLayoutId() {
        return R.layout.fragment_bottle;
    }

    @OnClick(R.id.text_send)
    public void onTextSendClicked() {
        startActivityForResult(new Intent(getActivity(), SendBottleActivity.class), 1001);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void loadUser() {
        ImageLoader.getInstance().displayImage(App.app.userInfo.findAsString("avatarPreView"), imageHead, App.app.getImageCircleOptions());
        textName.setText(App.app.userInfo.findAsString("nickname"));
        textNo.setText("星星：" + App.app.userInfo.findAsString("amount"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            loadUser();
        }
    }
}
