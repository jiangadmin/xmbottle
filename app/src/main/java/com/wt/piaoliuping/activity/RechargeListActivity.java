package com.wt.piaoliuping.activity;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.adapter.RechargeAdapter;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wangtao on 2018/2/4.
 */

public class RechargeListActivity extends BaseTitleActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.grid_view)
    PullToRefreshGridView gridView;
    @BindView(R.id.text_desc)
    TextView testDesc;

    RechargeAdapter adapter;
    @BindView(R.id.layout_pay)
    TabLayout layoutPay;

    private static final int SDK_PAY_FLAG = 1;

    @Override
    public void initView() {
        setTitle("充值");
        adapter = new RechargeAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setMode(PullToRefreshBase.Mode.DISABLED);
        gridView.setOnItemClickListener(this);

        loadData();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_recharge_list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HaoResult result = (HaoResult) adapter.getItem(position);

        if (layoutPay.getSelectedTabPosition() == 0) {
            alipay(result.findAsString("id"));
        }
    }

    private void alipay(String itemId) {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_amount_id", itemId);
        map.put("pay_type", "alipay");
        startLoading();
        HaoConnect.loadContent("pay_order/buy_goods_amount", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                stopLoading();
                String payInfo = result.findAsString("results>");
                final String orderInfo = payInfo;   // 订单信息
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(RechargeListActivity.this);
                        Map<String, String> result = alipay.payV2(orderInfo, true);
                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
                stopLoading();
            }
        }, this);
    }
    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        map.put("size", "999");
        HaoConnect.loadContent("goods_amount/list", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ArrayList<Object> lists = result.findAsList("results>");
                adapter.setData(lists);
                if (adapter.dataList.isEmpty()) {
                    showNoData();
                } else {
                    hideNoData();
                }
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(RechargeListActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(RechargeListActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
}
