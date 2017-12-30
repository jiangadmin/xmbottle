package com.wt.piaoliuping.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wangtao on 2017/12/30.
 */

public class ShareInfoActivity extends BaseTitleActivity {
    @BindView(R.id.btn_right_title)
    ImageButton btnRightTitle;
    @BindView(R.id.text_code)
    TextView textCode;
    @BindView(R.id.btn_bind)
    TextView btnBind;
    @BindView(R.id.btn_share)
    TextView btnShare;
    @BindView(R.id.text_desc)
    TextView textDesc;
    @BindView(R.id.image_code)
    ImageView imageCode;
    Bitmap mBitmap;
    @Override
    public void initView() {
        setTitle("邀请奖励");
        btnRightTitle.setVisibility(View.VISIBLE);
        btnRightTitle.setBackgroundResource(R.drawable.icon_scan);
        textCode.setText(getIntent().getStringExtra("userId"));
        btnRightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShareInfoActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 1000);
            }
        });

        loadDetail();

        mBitmap = CodeUtils.createImage(getIntent().getStringExtra("userId"), 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        imageCode.setImageBitmap(mBitmap);
        imageCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(ShareInfoActivity.this)
                        .setTitle("提示")
                        .setMessage("将该图片保存在本地吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                save();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                return true;
            }
        });
    }

    private void save() {
//        StorageUtils.getIndividualCacheDirectory(this, "photos").getPath();
        File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() , System.currentTimeMillis() + ".jpg");
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(imageFile);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            showToast("保存成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_share_info;
    }

    @OnClick(R.id.btn_bind)
    public void onBtnBindClicked() {
        startActivity(new Intent(this, RecommendActivity.class));
    }

    @OnClick(R.id.btn_share)
    public void onBtnShareClicked() {
        loadShare();
    }

    private void loadShare() {
        HaoConnect.loadContent("sys_config/share_info", null, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                UMImage umImage = new UMImage(ShareInfoActivity.this, R.mipmap.ic_launcher);
                UMWeb web = new UMWeb(result.findAsString("shareUrl"));
                web.setTitle(result.findAsString("shareTitle"));//标题
                web.setThumb(umImage);  //缩略图
                web.setDescription(result.findAsString("shareContent"));//描述

                new ShareAction(ShareInfoActivity.this)
                        .withMedia(web)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onStart(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                if (share_media == SHARE_MEDIA.WEIXIN) {
                                    upload("weixin");
                                } else {
                                    upload("weixincircle");
                                }

                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {

                            }
                        })
                        .open();
            }
        }, this);
    }


    private void upload(String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("channel", type);
        HaoConnect.loadContent("sys_config/share_ok", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {

            }
        }, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    submit(result);
//                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(ShareInfoActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void submit(String code) {
        Map<String, Object> map = new HashMap<>();
        map.put("invite_code", code);
        HaoConnect.loadContent("user_invite/invite_user", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                showToast("绑定成功");
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }


    private void loadDetail() {
        Map<String, Object> map = new HashMap<>();
        HaoConnect.loadContent("user_invite/invite_code", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                textDesc.setText(result.findAsString("inviteUserRule"));
            }

            @Override
            public void onFail(HaoResult result) {
            }
        }, this);
    }
}
