package com.wt.piaoliuping;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.stetho.Stetho;
import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoResult;
import com.haoxitech.HaoConnect.HaoResultHttpResponseHandler;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseNotifier;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.wt.piaoliuping.database.DataBaseHelper;
import com.wt.piaoliuping.db.DaoMaster;
import com.wt.piaoliuping.db.DaoSession;
import com.wt.piaoliuping.db.UserDao;
import com.wt.piaoliuping.db.UserDaoDao;
import com.wt.piaoliuping.utils.CallReceiver;
import com.wt.piaoliuping.utils.PreferenceManager;

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
    private CallReceiver callReceiver;
    private DemoModel demoModel = null;

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
//        Config.DEBUG = true;
        UMConfigure.init(this, "5a37da7df29d981083000031", "xmbottle", UMConfigure.DEVICE_TYPE_PHONE, "aeitmoora8rqgevfsaik32cdjjxfxsdg");
        //        UMConfigure.setLogEnabled(true);
        UMShareAPI.get(this);
        PlatformConfig.setWeixin("wx4b57ed12eb6b3040", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
//        PushAgent mPushAgent = PushAgent.getInstance(this);
//        //注册推送服务，每次调用register方法都会回调该接口
//        mPushAgent.register(new IUmengRegisterCallback() {
//
//            @Override
//            public void onSuccess(String deviceToken) {
//                //注册成功会返回device token
//                Log.e("wt", deviceToken);
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//
//                Log.e("wt", s + " " + s1);
//            }
//        });
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

    public DisplayImageOptions getImageCircleOptions() {
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
                .displayer(new Displayer(0))
                .build();
    }

    class Displayer extends RoundedBitmapDisplayer {
        public Displayer(int cornerRadiusPixels) {
            super(cornerRadiusPixels);
        }

        // 显示位图
        @Override
        public void display(Bitmap bitmap, ImageAware imageAware,
                            LoadedFrom loadedFrom) {
            imageAware.setImageDrawable(new CircleDrawable(bitmap, margin));
        }

        public class CircleDrawable extends Drawable {
            private final int margin;
            private final RectF mRect = new RectF();
            private final BitmapShader bitmapShader;
            private final Paint paint;
            private RectF mBitmapRect;

            public CircleDrawable(Bitmap bitmap, int margin) {
                this.margin = 0;
                // 创建着色器
                bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                        Shader.TileMode.CLAMP);
                mBitmapRect = new RectF(margin, margin, bitmap.getWidth() - margin,
                        bitmap.getHeight() - margin);
                // 设置画笔
                paint = new Paint();
                paint.setAntiAlias(true);
                paint.setShader(bitmapShader);
            }

            // 画圆，覆盖原来的位图
            @Override
            protected void onBoundsChange(Rect bounds) {
                super.onBoundsChange(bounds);
                mRect.set(margin, margin, bounds.width() - margin, bounds.height()
                        - margin);

                // 调整位图，设置该矩阵，转换映射源矩形和目的矩形
                Matrix shaderMatrix = new Matrix();
                shaderMatrix.setRectToRect(mBitmapRect, mRect,
                        Matrix.ScaleToFit.FILL);
                // 设置着色器矩阵
                bitmapShader.setLocalMatrix(shaderMatrix);
            }

            // 画出其边界（通过设置的setBounds）
            @Override
            public void draw(Canvas canvas) {
                canvas.drawRoundRect(mRect, mRect.width() / 2, mRect.height() / 2,
                        paint);
            }

            /**
             * 返回此绘制对象的不透明度/透明度 ，返回的值是抽象的格式常数的PixelFormat之一：未知，半透明，透明或不透明
             */
            @Override
            public int getOpacity() {
                // 半透明
                return PixelFormat.TRANSLUCENT;
            }

            // 设置透明度
            @Override
            public void setAlpha(int alpha) {
                paint.setAlpha(alpha);
            }

            // 彩色滤光片（通过设置setColorFilter）
            @Override
            public void setColorFilter(ColorFilter cf) {
                paint.setColorFilter(cf);
            }
        }
    }

    private DaoSession daoSession;

    private void initDataBase() {
        try {
            DataBaseHelper.copyDataBase(this, "zipArea.db");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "user_db");
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

        PreferenceManager.init(this);

        // min video kbps
        int minBitRate = PreferenceManager.getInstance().getCallMinVideoKbps();
        if (minBitRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setMinVideoKbps(minBitRate);
        }

        // max video kbps
        int maxBitRate = PreferenceManager.getInstance().getCallMaxVideoKbps();
        if (maxBitRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setMaxVideoKbps(maxBitRate);
        }

        // max frame rate
        int maxFrameRate = PreferenceManager.getInstance().getCallMaxFrameRate();
        if (maxFrameRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setMaxVideoFrameRate(maxFrameRate);
        }

        // audio sample rate
        int audioSampleRate = PreferenceManager.getInstance().getCallAudioSampleRate();
        if (audioSampleRate != -1) {
            EMClient.getInstance().callManager().getCallOptions().setAudioSampleRate(audioSampleRate);
        }

        /**
         * This function is only meaningful when your app need recording
         * If not, remove it.
         * This function need be called before the video stream started, so we set it in onCreate function.
         * This method will set the preferred video record encoding codec.
         * Using default encoding format, recorded file may not be played by mobile player.
         */
        //EMClient.getInstance().callManager().getVideoCallHelper().setPreferMovFormatEnable(true);

        // resolution
        String resolution = PreferenceManager.getInstance().getCallBackCameraResolution();
        if (resolution.equals("")) {
            resolution = PreferenceManager.getInstance().getCallFrontCameraResolution();
        }
        String[] wh = resolution.split("x");
        if (wh.length == 2) {
            try {
                EMClient.getInstance().callManager().getCallOptions().setVideoResolution(new Integer(wh[0]).intValue(), new Integer(wh[1]).intValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // enabled fixed sample rate
        boolean enableFixSampleRate = PreferenceManager.getInstance().isCallFixedVideoResolution();
        EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(enableFixSampleRate);

        // Offline call push
        EMClient.getInstance().callManager().getCallOptions().setIsSendPushIfOffline(true);

        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }

        //register incoming call receiver
        registerReceiver(callReceiver, callFilter);
        registerMessageListener();
        demoModel = new DemoModel(this);
        EaseUI.getInstance().setSettingsProvider(new EaseUI.EaseSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {
                return demoModel.getSettingMsgSpeaker();
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return demoModel.getSettingMsgVibrate();
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return demoModel.getSettingMsgSound();
            }

            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                if (message == null) {
                    return demoModel.getSettingMsgNotification();
                }
                if (!demoModel.getSettingMsgNotification()) {
                    return false;
                } else {
                    String chatUsename = null;
                    List<String> notNotifyIds = null;
                    // get user or group id which was blocked to show message notifications
//                    if (message.getChatType() == EMMessage.ChatType.Chat) {
//                        chatUsename = message.getFrom();
//                        notNotifyIds = demoModel.getDisabledIds();
//                    } else {
//                        chatUsename = message.getTo();
//                        notNotifyIds = demoModel.getDisabledGroups();
//                    }
//
//                    if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
                    return true;
//                    } else {
//                        return false;
//                    }
                }
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

    public EaseNotifier getNotifier() {
        return EaseUI.getInstance().getNotifier();
    }


    protected EMMessageListener messageListener = null;

    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    // in background, do not refresh UI, notify it in notification bar
                    if (!EaseUI.getInstance().hasForegroundActivies()) {
                        getNotifier().onNewMsg(message);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    //get message body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action
                    //red packet code : 处理红包回执透传消息
                    if (!EaseUI.getInstance().hasForegroundActivies()) {
//                        if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
//                            RedPacketUtil.receiveRedPacketAckMessage(message);
//                            broadcastManager.sendBroadcast(new Intent(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION));
//                        }
                    }

                    if (action.equals("__Call_ReqP2P_ConferencePattern")) {
                        String title = message.getStringAttribute("em_apns_ext", "conference call");
                        Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
                    }
                    //end of red packet code
                    //获取扩展属性 此处省略
                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
            }

            //            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
//                for (EMMessage msg : messages) {
//                    if(msg.getChatType() == EMMessage.ChatType.GroupChat && EaseAtMessageHelper.get().isAtMeMsg(msg)){
//                        EaseAtMessageHelper.get().removeAtMeGroup(msg.getTo());
//                    }
//                    EMMessage msgNotification = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
//                    EMTextMessageBody txtBody = new EMTextMessageBody(String.format(getApplicationContext().getString(R.string.msg_recall_by_user), msg.getFrom()));
//                    msgNotification.addBody(txtBody);
//                    msgNotification.setFrom(msg.getFrom());
//                    msgNotification.setTo(msg.getTo());
//                    msgNotification.setUnread(false);
//                    msgNotification.setMsgTime(msg.getMsgTime());
//                    msgNotification.setLocalTime(msg.getMsgTime());
//                    msgNotification.setChatType(msg.getChatType());
//                    msgNotification.setAttribute(MESSAGE_TYPE_RECALL, true);
//                    msgNotification.setStatus(EMMessage.Status.SUCCESS);
//                    EMClient.getInstance().chatManager().saveMessage(msgNotification);
//                }
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
//                EMLog.d(TAG, "change:");
//                EMLog.d(TAG, "change:" + change);
            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }
}
