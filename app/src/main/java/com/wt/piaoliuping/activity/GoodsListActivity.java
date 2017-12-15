package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.adapter.GoodsAdapter;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/11/14.
 */

public class GoodsListActivity extends BaseTitleActivity implements GoodsAdapter.ItemClickListener {
    @BindView(R.id.grid_view)
    PullToRefreshGridView gridView;

    GoodsAdapter adapter;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @BindView(R.id.text_point)
    TextView textPoint;
    @BindView(R.id.text_recharge)
    TextView textRecharge;
    @BindView(R.id.layout_5)
    LinearLayout layout5;

    private boolean send;
    private String userId;
    private String userName;

    @Override
    public void initView() {
        setTitle("礼品商城");
        adapter = new GoodsAdapter(this);
        gridView.setAdapter(adapter);
        adapter.setItemClickListener(this);
        send = getIntent().getBooleanExtra("send", false);
        userId = getIntent().getStringExtra("userId");
        loadData();
        gridView.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUser();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_goods_list;
    }

    @Override
    public void click(View v, int position) {
        if (v.getId() == R.id.btn_submit) {

            /*HaoResult result = (HaoResult) adapter.dataList.get(position);

            Map<String, Object> map = new HashMap<>();
            map.put("pay_type", "alipay");
            map.put("goods_item_id", result.findAsString("id"));
            startLoading();
            HaoConnect.loadContent("pay_order/buy_goods_item", map, "post", new HaoResultHttpResponseHandler() {
                @Override
                public void onSuccess(HaoResult result) {
                    stopLoading();
                    String payInfo = result.findAsString("results>");
                    final String orderInfo = payInfo;   // 订单信息
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(GoodsListActivity.this);
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
            }, this);*/

            HaoResult result = (HaoResult) adapter.dataList.get(position);

            Map<String, Object> map = new HashMap<>();
            map.put("id", result.findAsString("id"));
            startLoading();
            HaoConnect.loadContent("goods_item/exchange", map, "post", new HaoResultHttpResponseHandler() {
                @Override
                public void onSuccess(HaoResult result) {
                    stopLoading();
                    if (send) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("user_goods_item_id", result.findAsString("id"));
                        map.put("give_user_id", userId);
                        HaoConnect.loadContent("user_goods_item/give_user", map, "post", new HaoResultHttpResponseHandler() {
                            @Override
                            public void onSuccess(HaoResult result) {
                                showToast("赠送成功");
                                String file = ImageLoader.getInstance().getDiskCache().get(result.findAsString("goodsImgView")).getAbsolutePath();
                                EMMessage imageMessage = EMMessage.createImageSendMessage(file, false, userId);
                                EMClient.getInstance().chatManager().sendMessage(imageMessage);

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                String desc = "【" + userName + "】"+"送您礼物 {" + result.findAsString("goodsName") + "}";
                                EMMessage message = EMMessage.createTxtSendMessage(desc, userId);
                                EMClient.getInstance().chatManager().sendMessage(message);
                                setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onFail(HaoResult result) {
                                showToast(result.errorStr);
                            }
                        }, GoodsListActivity.this);
                    } else {
                        showToast("购买成功");
                    }
                }

                @Override
                public void onFail(HaoResult result) {
                    showToast(result.errorStr);
                    stopLoading();
                }
            }, this);
        }
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        map.put("size", "999");
        HaoConnect.loadContent("goods_item/list", map, "get", new HaoResultHttpResponseHandler() {
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
                        Toast.makeText(GoodsListActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(GoodsListActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @OnClick(R.id.text_recharge)
    public void onViewClicked() {
        startActivity(new Intent(this, PointActivity.class));
    }


    private void loadUser() {
        HaoConnect.loadContent("user/my_detail", null, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                textPoint.setText("我的星星：" + result.findAsString("amountAdnScore") + "星星");
                userName = result.findAsString("nickname");
            }
        }, this);
    }

    @OnClick(R.id.layout_5)
    public void onLayout5licked() {
        startActivity(new Intent(this, PrizeListActivity.class));
    }
}
