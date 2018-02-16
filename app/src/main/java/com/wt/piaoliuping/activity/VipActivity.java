package com.wt.piaoliuping.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wt.piaoliuping.Constant;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.adapter.VipAdapter;
import com.wt.piaoliuping.base.AppManager;
import com.wt.piaoliuping.base.BaseTitleActivity;
import com.wt.piaoliuping.manager.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by wangtao on 07/02/2018.
 */

public class VipActivity extends BaseTitleActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.top_image_view)
    ImageView topImageView;

    @BindView(R.id.list_view)
    PullToRefreshListView listView;

    @BindView(R.id.text_desc)
    TextView textDesc;


    private static final int SDK_PAY_FLAG = 1;

    VipAdapter vipAdapter;

    private IWXAPI api;

    @Override
    public void initView() {
        setTitle("会员中心");

        api = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_KEY);
        vipAdapter = new VipAdapter(this);
        listView.setAdapter(vipAdapter);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        loadData();
        listView.setOnItemClickListener(this);
        loadTips();
        loadTopImage();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_vip;
    }

    private void loadData() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", "1");
        map.put("size", "999");
        HaoConnect.loadContent("goods_vip/list", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ArrayList<Object> lists = result.findAsList("results>");
                vipAdapter.setData(lists);
            }

            @Override
            public void onFail(HaoResult result) {
//                showToast(result.errorStr);
            }
        }, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void loadTips() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "goodsVipLabel");
        HaoConnect.loadContent("sys_config/detail", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                textDesc.setText(result.findAsString("results>value"));
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }
    private void loadTopImage() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "goodsVipImg");
        HaoConnect.loadContent("sys_config/detail", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ImageLoader.getInstance().displayImage(result.findAsString("results>value"), topImageView);
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    private void alipay(String itemId) {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_vip_id", itemId);
        map.put("pay_type", "alipay");
        startLoading();
        HaoConnect.loadContent("pay_order/buy_goods_vip", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                stopLoading();
                String payInfo = result.findAsString("results>");
                final String orderInfo = payInfo;   // 订单信息
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(VipActivity.this);
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
                        Toast.makeText(VipActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(VipActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private void weixinPay(String itemId) {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_vip_id", itemId);
        map.put("pay_type", "wechat");
        startLoading();
        HaoConnect.loadContent("pay_order/buy_goods_vip", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(final HaoResult result) {
                stopLoading();
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayReq request = new PayReq();

                        request.appId = result.findAsString("results>appid");

                        request.partnerId = result.findAsString("results>partnerid");

                        request.prepayId= result.findAsString("results>prepayid");

                        request.packageValue = result.findAsString("package");

                        request.nonceStr= result.findAsString("noncestr");

                        request.timeStamp= result.findAsString("timestamp");

                        request.sign= result.findAsString("sign");

                        api.sendReq(request);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HaoResult haoResult = (HaoResult) parent.getAdapter().getItem(position);
        final String itemId = haoResult.findAsString("id");
//        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_pay, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("支付")
                .setMessage("请选择支付方式")
                .setPositiveButton("支付宝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alipay(itemId);
                    }
                })
                .setNegativeButton("微信", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        showToast("微信支付正在申请中，下个版本正常使用");
                        weixinPay(itemId);
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
//        alertDialog.setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        alertDialog.show();
//
//        contentView.findViewById(R.id.text_alipay).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alipay(itemId);
//            }
//        });
//
//        contentView.findViewById(R.id.text_weichat).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                weixinPay(itemId);
//            }
//        });

    }
}
