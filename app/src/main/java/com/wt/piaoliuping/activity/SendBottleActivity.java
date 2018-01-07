package com.wt.piaoliuping.activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.hyphenate.util.PathUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;
import com.wt.piaoliuping.widgt.CameraDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by wangtao on 2017/10/28.
 */

public class SendBottleActivity extends BaseTitleActivity {
    @BindView(R.id.text_content)
    EditText textContent;
    @BindView(R.id.text_rand)
    TextView textRand;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.text_pic)
    TextView textPic;
    @BindView(R.id.text_voice)
    TextView textVoice;
    @BindView(R.id.voice_recorder)
    EaseVoiceRecorderView voiceRecorder;

    private String randId;
    RxPermissions rxPermissions;

    @Override
    public void initView() {
        setTitle("扔一个");
        rxPermissions = new RxPermissions(this);

        textVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                voiceRecorder.onPressToSpeakBtnTouch(view, motionEvent, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
                return false;
            }
        });
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_send_bottle;
    }

    @OnClick(R.id.text_rand)
    public void onTextRandClicked() {
        Map<String, Object> map = new HashMap<>();
        map.put("limit", "1");
        HaoConnect.loadContent("bottle_message/bottle_text", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                textContent.setText(result.findAsString("0>message"));
                randId = result.findAsString("id");
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    @OnClick(R.id.btn_send)
    public void onBtnSendClicked() {
        if (TextUtils.isEmpty(textContent.getText())) {
            showToast("内容不能为空");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("message", textContent.getText());
        if (!TextUtils.isEmpty(randId)) {
            map.put("bottle_text_id", randId);
        }
        startLoading();
        HaoConnect.loadContent("bottle_message/add", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                showToast("已发送");
                finish();
                stopLoading();
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
                stopLoading();
            }
        }, this);
    }



    @OnClick({R.id.text_pic, R.id.text_voice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_pic:
                final CameraDialog dialog = CameraDialog.create(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.layout_camera) {
                            handleCameraPermission();
                        } else if (v.getId() == R.id.layout_lib) {
                            handleAlbumPermission();
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.text_voice:

                break;
        }
    }

    private void sendVoiceMessage(String voiceFilePath, int voiceTimeLength) {
        File fileTemp = new File(voiceFilePath);
        updateImage(fileTemp, 3);
    }

    private void updateImage(File file, final int type) {
        HaoConnect.loadImageContent("axapi/up_file", file, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                Log.e("wt", "onSuccess" + " result" + result);
                String url = result.findAsString("filePath");
//                updateUserInfo("avatar", url);
                Map<String, Object> map = new HashMap<>();
                map.put("message", url);
                map.put("message_type", type);
                startLoading();
                HaoConnect.loadContent("bottle_message/add", map, "post", new HaoResultHttpResponseHandler() {
                    @Override
                    public void onSuccess(HaoResult result) {
                        showToast("已发送");
                        finish();
                        stopLoading();
                    }

                    @Override
                    public void onFail(HaoResult result) {
                        showToast(result.errorStr);
                        stopLoading();
                    }
                }, SendBottleActivity.this);
            }

            @Override
            public void onFail(HaoResult result) {
                super.onFail(result);
                showToast(result.errorStr);
            }
        }, this);
    }

    private void handleCameraPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            rxPermissions.request(Manifest.permission.CAMERA)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                takeCamera();
                            } else {
                                showToast("已禁止相机权限，您可以在系统设置中打开");
                            }
                        }
                    });
        } else {
            takeCamera();
        }
    }

    private void handleAlbumPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (aBoolean) {
                                handleSelectPicture();
                            } else {
                                showToast("已禁止文件读写权限，您可以在系统设置中打开");
                            }
                        }
                    });
        } else {
            handleSelectPicture();
        }
    }

    private Uri photoUri;

    private void takeCamera() {
        File directory = StorageUtils.getIndividualCacheDirectory(this, "photos");
        if (!directory.exists()) {
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            File imgFile = new File(directory, System.currentTimeMillis() + "");
            imgFile.createNewFile();
            photoUri = Uri.fromFile(imgFile);
            Uri photoUri;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                photoUri = this.photoUri;
            } else {
                photoUri = FileProvider.getUriForFile(this.getApplicationContext(), "com.xm.bottle.provider", imgFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleSelectPicture() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
        }
        startActivityForResult(Intent.createChooser(intent, "选择照片"), 2);
    }

    String currentImagePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
//            ImageLoader.getInstance().displayImage("file://" + data.getStringExtra("imagePath"), imageHead);
        }
        if (requestCode == 2) {

//            startPhotoZoom(data.getData());
            onPhotoBack(data.getData());
        } else if (requestCode == 3) {
            onPhotoBack(photoUri);
//            startPhotoZoom(photoUri);
//        } else if (requestCode == 4) {
//            onPhotoBack(photoUri);
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 4);
    }


    public void onPhotoBack(Uri originalUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        // 相册
        Cursor cursor = getContentResolver().query(
                originalUri, proj, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            currentImagePath = cursor.getString(column_index);
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                //cursor = null;
            }
        } else {//Android 4.4
            currentImagePath = originalUri.toString().replace("file://", "");
        }
        File fileTemp = new File(currentImagePath);

        Log.e("size", fileTemp.length() + "");
        updateImage(compressedImage(fileTemp), 2);
//        ImageLoader.getInstance().displayImage("file://" + currentImagePath, imageHead);
    }


    public File compressedImage(File file) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 300f;//这里设置高度为800f
        float ww = 300f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = 3;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);
        File compressedFile = new File(getTempPath(file.getName()));
//        tempFiles.add(compressedFile);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(compressedFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return compressedFile;

    }

    public String getTempPath(String name) {

        return PathUtil.getInstance().getImagePath() + name + "_tmp.jpg";
//        String path = StorageUtils.getIndividualCacheDirectory(this, "photos").getPath();
//        return path + name + "_tmp.jpg";

//        return basePath + name + "_tmp.jpg";
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }
}
