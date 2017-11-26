package com.wt.piaoliuping.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
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
 * Created by wangtao on 2017/10/26.
 */

public class PointActivity extends BaseTitleActivity {
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @BindView(R.id.text_mine_star)
    TextView textMineStar;
    @BindView(R.id.layout_1)
    LinearLayout layout1;
    @BindView(R.id.text_mine_point)
    TextView textMinePoint;
    @BindView(R.id.layout_2)
    LinearLayout layout2;
    @BindView(R.id.edit_money)
    EditText editMoney;
    @BindView(R.id.layout_3)
    LinearLayout layout3;
    @BindView(R.id.text_point)
    TextView textPoint;
    @BindView(R.id.layout_4)
    LinearLayout layout4;
    @BindView(R.id.btn_charge)
    Button btnCharge;
    @BindView(R.id.btn_withdrew)
    Button btnWithdrew;

    @Override
    public void initView() {
        setTitle("我的积分");

        loadUser();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_point;
    }

    @OnClick({R.id.btn_charge, R.id.btn_withdrew})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_charge: {
                Map<String, Object> map = new HashMap<>();
                map.put("pay_type", "alipay");
                map.put("price", editMoney.getText().toString());
                startLoading();
                HaoConnect.loadContent("pay_order/buy_star", map, "post", new HaoResultHttpResponseHandler() {
                    @Override
                    public void onSuccess(HaoResult result) {
                        stopLoading();
                        String payInfo = result.findAsString("results>");
                        final String orderInfo = payInfo;   // 订单信息
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(PointActivity.this);
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
                break;
            }
            case R.id.btn_withdrew:
                startActivity(new Intent(this, WithdrewActivity.class));
                break;
        }
    }

    private void loadUser() {
        startLoading();
        HaoConnect.loadContent("user/my_detail", null, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
//                ImageLoader.getInstance().displayImage(result.findAsString("avatarPreView"), imageHead);
                textMineStar.setText("我的星星：" + result.findAsString("amount"));
                textPoint.setText("系统积分：" + result.findAsString("score"));
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
                        Toast.makeText(PointActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PointActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
//                case SDK_AUTH_FLAG: {
//                    @SuppressWarnings("unchecked")
//                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
//                    String resultStatus = authResult.getResultStatus();
//
//                    // 判断resultStatus 为“9000”且result_code
//                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
//                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
//                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
//                        // 传入，则支付账户为该授权账户
//                        Toast.makeText(PayDemoActivity.this,
//                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
//                                .show();
//                    } else {
//                        // 其他状态值则为授权失败
//                        Toast.makeText(PayDemoActivity.this,
//                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
//
//                    }
//                    break;
//                }
                default:
                    break;
            }
        }

        ;
    };
}
