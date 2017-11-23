package com.wt.piaoliuping;

import android.app.Application;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.wt.piaoliuping.database.DataBaseHelper;
import com.wt.piaoliuping.manager.UserManager;
import com.wt.piaoliuping.utils.DateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangtao on 2017/10/20.
 */

public class App extends Application {

    public static App app;
    public String userName;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        HaoConnect.init(this);
        initImageLoader();
        initDataBase();
        initHX();
        sign();
    }

    /**
     * 初始化图片加载类
     */
    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
                .cacheOnDisk(true)
//                .cacheInMemory(true)
                // 如Bitmap.Config.ARGB_8888
//                .showImageOnLoading(R.drawable.image_default) // 默认图片
//                .showImageForEmptyUri(R.drawable.image_default) //
//                        // url爲空會显示该图片，自己放在drawable里面的
//                .showImageOnFail(R.drawable.image_default)// 加载失败显示的图片
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).memoryCacheExtraOptions(480, 800)
                // 缓存在内存的图片的宽和高度
//                .discCacheExtraOptions(480, 800, CompressFormat.PNG, 70, null)
                // CompressFormat.PNG类型，70质量（0-100）
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(2 * 1024 * 1024) // 缓存到内存的最大数据
                .discCacheSize(50 * 1024 * 1024) // 缓存到文件的最大数据
                .discCacheFileCount(1000) // 文件数量
                .defaultDisplayImageOptions(options) // 上面的options对象，一些属性配置
                .build();
        ImageLoader.getInstance().init(config); // 初始化
    }


    private void initDataBase() {
        try {
            DataBaseHelper.copyDataBase(this, "zipArea.db");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void initHX() {
        EMOptions options = new EMOptions();
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
//        options.setRequireServerAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);

        EaseUI.getInstance().init(this, options);
    }

    private void sign() {
        if (!TextUtils.isEmpty(UserManager.getInstance().getUserId())) {
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", UserManager.getInstance().getUserId());
            map.put("sign_date", DateUtils.getStringDateAndTimeFromDate(new Date()));
            HaoConnect.loadContent("sign_in/add", map, "get", new HaoResultHttpResponseHandler() {
                @Override
                public void onSuccess(HaoResult result) {
                }

                @Override
                public void onFail(HaoResult result) {
                }
            }, this);
        }
    }

}
