package com.wt.piaoliuping;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.stetho.Stetho;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.utils.Log;
import com.wt.piaoliuping.activity.LoginActivity;
import com.wt.piaoliuping.activity.SettingTitleActivity;
import com.wt.piaoliuping.base.AppManager;
import com.wt.piaoliuping.database.DataBaseHelper;
import com.wt.piaoliuping.db.DaoMaster;
import com.wt.piaoliuping.db.DaoSession;
import com.wt.piaoliuping.db.UserDao;
import com.wt.piaoliuping.db.UserDaoDao;
import com.wt.piaoliuping.manager.UserManager;

import org.greenrobot.greendao.database.Database;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangtao on 2017/10/20.
 */

public class App extends Application {

    public static App app;
    public String userName;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    public double latitude;
    public double longitude;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        HaoConnect.init(this);
        initImageLoader();
        initDataBase();
        initHX();
        initUMeng();
        Stetho.initializeWithDefaults(this);
        initBDMap();
    }

    private void initBDMap() {
        SDKInitializer.initialize(this);
        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
    }

    private void initUMeng() {
        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wx4b57ed12eb6b3040", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
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
                .showImageOnLoading(R.drawable.image_default) // 默认图片
                .showImageForEmptyUri(R.drawable.image_default) //
//                        // url爲空會显示该图片，自己放在drawable里面的
                .showImageOnFail(R.drawable.image_default)// 加载失败显示的图片
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


    private DaoSession daoSession;

    private void initDataBase() {
        try {
            DataBaseHelper.copyDataBase(this, "zipArea.db");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "user_db");
        Database db = devOpenHelper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
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

        EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });
    }

    private EaseUser getUserInfo(String username) {
        loadUser(username);
        UserDaoDao userDao = getDaoSession().getUserDaoDao();
        UserDao userDao1 = userDao.load(username);
        if (userDao1 == null) {
            return null;
        }
        EaseUser user = new EaseUser(username);
        user.setAvatar(userDao1.getAvatar());
        user.setNickname(userDao1.getNick());
        return user;
    }

    private void loadUser(String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        HaoConnect.loadContent("user/detail", map, "get", new HaoResultHttpResponseHandler() {
            @Override
            public void onSuccess(HaoResult result) {

                UserDaoDao userDao = getDaoSession().getUserDaoDao();
                UserDao userDao1 = new UserDao();
                userDao1.setAvatar(result.findAsString("avatarPreView"));
                userDao1.setUserName(result.findAsString("id"));
                userDao1.setNick(result.findAsString("nickname"));
                userDao.insertOrReplace(userDao1);
            }

            @Override
            public void onFail(HaoResult result) {
            }
        }, this);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            latitude = location.getLatitude();    //获取纬度信息
            longitude = location.getLongitude();    //获取经度信息
        }
    }
}
