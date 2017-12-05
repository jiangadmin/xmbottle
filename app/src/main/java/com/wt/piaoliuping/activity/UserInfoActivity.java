package com.wt.piaoliuping.activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.AppManager;
import com.wt.piaoliuping.base.BaseTitleActivity;
import com.wt.piaoliuping.manager.UserManager;
import com.wt.piaoliuping.utils.DateUtils;
import com.wt.piaoliuping.widgt.CameraDialog;
import com.wt.piaoliuping.widgt.DatePickerDialog;
import com.wt.piaoliuping.widgt.PickerDialog;
import com.wt.piaoliuping.widgt.SexDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by wangtao on 2017/10/26.
 */

public class UserInfoActivity extends BaseTitleActivity {
    @BindView(R.id.image_head)
    ImageView imageHead;
    @BindView(R.id.layout_1)
    LinearLayout layout1;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.layout_2)
    LinearLayout layout2;
    @BindView(R.id.text_sex)
    TextView textSex;
    @BindView(R.id.layout_3)
    LinearLayout layout3;
    @BindView(R.id.text_city)
    TextView textCity;
    @BindView(R.id.layout_4)
    LinearLayout layout4;
    @BindView(R.id.text_birthday)
    TextView textBirthday;
    @BindView(R.id.layout_5)
    LinearLayout layout5;
    @BindView(R.id.text_star)
    TextView textStar;
    @BindView(R.id.layout_6)
    LinearLayout layout6;
    @BindView(R.id.text_sign)
    TextView textSign;
    @BindView(R.id.layout_7)
    LinearLayout layout7;
    @BindView(R.id.layout_8)
    LinearLayout layout8;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    RxPermissions rxPermissions;

    @Override
    public void initView() {
        setTitle("个人资料");
        rxPermissions = new RxPermissions(this);
        loadUser();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_user_info;
    }

    @OnClick({R.id.layout_1, R.id.layout_2, R.id.layout_3, R.id.layout_4, R.id.layout_5, R.id.layout_6, R.id.layout_7, R.id.layout_8})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_1: {
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
            }
            break;
            case R.id.layout_2: {
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("title", "昵称");
                intent.putExtra("hint", "请输入昵称");
                intent.putExtra("content", textName.getText());
                intent.putExtra("key", "nickname");
                startActivityForResult(intent, 100);
            }
            break;
            case R.id.layout_3: {
               /* final SexDialog dialog = SexDialog.create(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.layout_female) {
                            textSex.setText("女");
                            updateUserInfo("sex", "2");
                        } else if (v.getId() == R.id.layout_male) {
                            updateUserInfo("sex", "1");
                            textSex.setText("男");
                        }
                    }
                });
                dialog.show();*/
            }
            break;
            case R.id.layout_4: {
                PickerDialog dialog = PickerDialog.create(this, new PickerDialog.PickerResultCallBack() {
                    @Override
                    public void choose(String result, String resultId) {
                        textCity.setText(result);
                        updateUserInfo("area_id", resultId);
                    }
                }, 0);
                dialog.show();
            }
            break;
            case R.id.layout_5: {
                DatePickerDialog.createDialog(this, new DatePickerDialog.DatePickerResultCallBack() {
                    @Override
                    public void choose(String time) {
                        if (DateUtils.getTime(time) > DateUtils.getTime(new Date())) {
                            showToast("生日不能晚于今天");
                        } else {
                            textBirthday.setText(time);
                            updateUserInfo("birthday", time);
                        }
                    }
                }).show();
            }
            break;
            case R.id.layout_6: {
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("title", "星座");
                intent.putExtra("hint", "请输入星座");
                intent.putExtra("content", textStar.getText());
                intent.putExtra("key", "constellation");
                intent.putExtra("type", 1);
                startActivityForResult(intent, 100);
            }
            break;
            case R.id.layout_7: {

                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("title", "签名");
                intent.putExtra("hint", "请输入个性签名");
                intent.putExtra("content", textSign.getText());
                intent.putExtra("key", "declaration");
                intent.putExtra("type", 1);
                startActivityForResult(intent, 100);
            }
            break;
            case R.id.layout_8: {
                Intent intent = new Intent(this, PhotosActivity.class);
                intent.putExtra("userId", UserManager.getInstance().getUserId());
                startActivity(intent);
            }
            break;
        }
    }

    private void handleCameraPermission() {
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

    }

    private void handleAlbumPermission() {

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
            File imgFile = new File(directory, System.currentTimeMillis() + ".png");
            imgFile.createNewFile();
            photoUri = Uri.fromFile(imgFile);
            Uri photoUri;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                photoUri = this.photoUri;
            } else {
                photoUri = FileProvider.getUriForFile(this.getApplicationContext(), this.getPackageName() + ".provider", imgFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, 3);
        } catch (IOException e) {
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

    private void updateImage(File file) {
        HaoConnect.loadImageContent("axapi/up_file", compressedImage(file), "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                Log.e("wt", "onSuccess" + " result" + result);
                String url = result.findAsString("filePath");
                updateUserInfo("avatar", url);
            }

            @Override
            public void onFail(HaoResult result) {
                super.onFail(result);
                showToast(result.errorStr);
            }
        }, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            ImageLoader.getInstance().displayImage("file://" + data.getStringExtra("imagePath"), imageHead);
        }
        if (requestCode == 2) {
            onPhotoBack(data.getData());
        } else if (requestCode == 3) {
            onPhotoBack(photoUri);
        }
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String key = data.getStringExtra("key");
            String value = data.getStringExtra("value");
            updateUserInfo(key, value);
            switch (key) {
                case "nickname":
                    textName.setText(value);
                    break;

                case "constellation":
                    textStar.setText(value);
                    break;

                case "declaration":
                    textSign.setText(value);
                    break;
            }
        }
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
        updateImage(fileTemp);
        ImageLoader.getInstance().displayImage("file://" + currentImagePath, imageHead);
    }

    private void updateUserInfo(String key, String value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        HaoConnect.loadContent("user/update", map, "post", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                if (result.isResultsOK()) {
                    showToast("更新成功");
                }
            }

            @Override
            public void onFail(HaoResult result) {
                showToast(result.errorStr);
            }
        }, this);
    }

    public File compressedImage(File file) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);
        File compressedFile = new File(getTempPath(file.getName()));
//        tempFiles.add(compressedFile);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(compressedFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
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
        String path = StorageUtils.getIndividualCacheDirectory(this, "photos").getPath();
        return path + name + "_tmp.jpg";
//        return basePath + name + "_tmp.jpg";
    }

    private void loadUser() {
        HaoConnect.loadContent("user/my_detail", null, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {
                ImageLoader.getInstance().displayImage(result.findAsString("avatarPreView"), imageHead);
                textName.setText(result.findAsString("nickname"));
                textSex.setText(result.findAsString("sexLabel"));
                textBirthday.setText(result.findAsString("birthday"));
                textStar.setText(result.findAsString("constellation"));
                textSign.setText(result.findAsString("declaration"));
                try {
                    textCity.setText(result.findAsString("areaLabel").split("-")[1]);
                } catch (Exception e) {

                }
            }
        }, this);
    }
}

