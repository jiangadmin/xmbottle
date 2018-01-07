/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.platform.comapi.util.CoordTrans;
import com.hyphenate.easeui.R;

//import com.baidu.mapapi.utils.CoordinateConverter;

public class EaseBaiduMapActivity extends EaseBaseActivity implements BaiduMap.OnMapStatusChangeListener {

    private final static String TAG = "map";
    static MapView mMapView = null;
    FrameLayout mMapViewContainer = null;
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    Button sendButton = null;

    EditText indexText = null;
    int index = 0;
    // LocationData locData = null;
    static BDLocation lastLocation = null;
    public static EaseBaiduMapActivity instance = null;
    ProgressDialog progressDialog;
    private BaiduMap mBaiduMap;
    private double lastLat;
    private double lastLong;
    private String lastAddress;
    private boolean showLocation;


    public class BaiduSDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            String st1 = getResources().getString(R.string.Network_error);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {

                String st2 = getResources().getString(R.string.please_check);
                Toast.makeText(instance, st2, Toast.LENGTH_SHORT).show();
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Toast.makeText(instance, st1, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private BaiduSDKReceiver mBaiduReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        //initialize SDK with context, should call this before setContentView
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.ease_activity_baidumap);
        mMapView = (MapView) findViewById(R.id.bmapView);
        sendButton = (Button) findViewById(R.id.btn_location_send);
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0);
        LocationMode mCurrentMode = LocationMode.NORMAL;
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        initMapView();
        if (latitude == 0) {
            mMapView = new MapView(this, new BaiduMapOptions());
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    mCurrentMode, true, null));
            showMapWithLocationClient();
        } else {
            double longtitude = intent.getDoubleExtra("longitude", 0);
            String address = intent.getStringExtra("address");
            LatLng p = new LatLng(latitude, longtitude);
            mMapView = new MapView(this,
                    new BaiduMapOptions().mapStatus(new MapStatus.Builder()
                            .target(p).build()));
            showMap(latitude, longtitude, address);
        }
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mBaiduReceiver = new BaiduSDKReceiver();
        registerReceiver(mBaiduReceiver, iFilter);
        /*mMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!inited) {
                        inited = true;
                        mBaiduMap.setOnMapStatusChangeListener(EaseBaiduMapActivity.this);
                    }
                }
                return true;
            }
        });*/
    }

    private void showMap(double latitude, double longtitude, String address) {
        sendButton.setVisibility(View.GONE);
        LatLng llA = new LatLng(latitude, longtitude);
//		CoordinateConverter converter= new CoordinateConverter();
//		converter.coord(llA);
//		converter.from(CoordinateConverter.CoordType.COMMON);
//		LatLng convertLatLng = converter.convert();
//        LatLng convertLatLng = CoordTrans.gcjToBaidu(llA);
//        OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory
//                .fromResource(R.drawable.ease_icon_marka))
//                .zIndex(4).draggable(true);
//        mBaiduMap.addOverlay(ooA);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(llA, 18.0f);
        mBaiduMap.animateMapStatus(u);
    }

    private void showMapWithLocationClient() {
        String str1 = getResources().getString(R.string.Making_sure_your_location);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(str1);

        progressDialog.setOnCancelListener(new OnCancelListener() {

            public void onCancel(DialogInterface arg0) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.d("map", "cancel retrieve location");
                finish();
            }
        });

        progressDialog.show();

        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// open gps
        // option.setCoorType("bd09ll");
        // Johnson change to use gcj02 coordination. chinese national standard
        // so need to conver to bd09 everytime when draw on baidu map
        option.setCoorType("gcj02");
        option.setScanSpan(30000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        if (mLocClient != null) {
            mLocClient.stop();
        }
        super.onPause();
        lastLocation = null;
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        if (mLocClient != null) {
            mLocClient.start();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mLocClient != null)
            mLocClient.stop();
        if (geoCoder != null) {
            geoCoder.destroy();
        }
        mMapView.onDestroy();
        unregisterReceiver(mBaiduReceiver);
        super.onDestroy();
    }

    private void initMapView() {
        mMapView.setLongClickable(true);
    }

    /**
     * format new location to string and show on screen
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }

            Log.e("wt", "onReceiveLocation");
            Log.d("map", "On location change received:" + location);
            Log.d("map", "addr:" + location.getAddrStr());
            sendButton.setEnabled(true);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            if (lastLocation != null) {
                if (lastLocation.getLatitude() == location.getLatitude() && lastLocation.getLongitude() == location.getLongitude()) {
                    Log.d("map", "same location, skip refresh");
                    // mMapView.refresh(); //need this refresh?
                    return;
                }
            }
            lastLocation = location;
            mBaiduMap.clear();
            LatLng llA = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//			CoordinateConverter converter= new CoordinateConverter();
//			converter.coord(llA);
//			converter.from(CoordinateConverter.CoordType.COMMON);
//			LatLng convertLatLng = converter.convert();
            LatLng convertLatLng = CoordTrans.gcjToBaidu(llA);
//            OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory
//                    .fromResource(R.drawable.ease_icon_marka))
//                    .zIndex(4).draggable(true);
//            mBaiduMap.addOverlay(ooA);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
            mBaiduMap.animateMapStatus(u);
            mLocClient.unRegisterLocationListener(this);
            lastLat = lastLocation.getLatitude();
            lastLong = lastLocation.getLongitude();
            lastAddress = lastLocation.getAddrStr();
            showLocation = true;
            if (!inited) {
                inited = true;
                mBaiduMap.setOnMapStatusChangeListener(EaseBaiduMapActivity.this);
            }
//            mBaiduMap.setOnMapStatusChangeListener(EaseBaiduMapActivity.this);

        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }

    private boolean inited = false;

    public void back(View v) {
        finish();
    }

    public void sendLocation(View view) {
        Intent intent = this.getIntent();
        intent.putExtra("latitude", lastLat);
        intent.putExtra("longitude", lastLong);
        intent.putExtra("address", lastAddress);
        this.setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
//        mBaiduMap.clear();
    }

    private boolean isGecoder = false;

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        Log.e("wt", "onMapStatusChangeFinish");
        if (showLocation) {

            lastLong = mapStatus.target.longitude;
            lastLat = mapStatus.target.latitude;
//            lastAddress = "地点";
            latlngToAddress(mapStatus);
        }
    }

    private GeoCoder geoCoder;

    private void latlngToAddress(MapStatus mapStatus) {
        if (isGecoder) {
            return;
        }
        isGecoder = true;
        if (geoCoder == null) {
            geoCoder = GeoCoder.newInstance();
        }
        // 设置反地理经纬度坐标,请求位置时,需要一个经纬度
        if (showLocation) {
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(mapStatus.target));
            geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                    isGecoder = false;
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                    isGecoder = false;
                    //获取地理编码结果
                    lastAddress = reverseGeoCodeResult.getAddress();
                    lastLong = reverseGeoCodeResult.getLocation().longitude;
                    lastLat = reverseGeoCodeResult.getLocation().latitude;
                }
            });

//            LatLng convertLatLng = CoordTrans.gcjToBaidu(llA);
//            geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
//                    .location(llA));
//            geoCoder.searchNearby(new PoiNearbySearchOption()
//                    .keyword("路")
//                    .sortType(PoiSortType.distance_from_near_to_far)
//                    .location(mapStatus.target)
//                    .radius(100)
//                    .pageCapacity(20)
//                    .pageNum(10));
//            geoCoder.setOnGetPoiSearchResultListener(listener);
        }
    }

    OnGetPoiSearchResultListener listener = new OnGetPoiSearchResultListener() {

        public void onGetPoiResult(PoiResult result) {
            //获取POI检索结果

            isGecoder = false;

            if (result != null && result.getAllPoi() != null && !result.getAllPoi().isEmpty()) {

                PoiInfo info = result.getAllPoi().get(0);
                //获取地理编码结果
                lastAddress = info.address;
                lastLong = info.location.longitude;
                lastLat = info.location.latitude;
            }
        }

        public void onGetPoiDetailResult(PoiDetailResult result) {
            //获取Place详情页检索结果
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };
    /*
    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {

        public void onGetGeoCodeResult(GeoCodeResult result) {

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
            }
            isGecoder = false;

            //获取地理编码结果
            lastAddress = result.getAddress();
            lastLong = result.getLocation().longitude;
            lastLat = result.getLocation().latitude;
        }

        @Override

        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

            isGecoder = false;
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
            }

            //获取反向地理编码结果
        }
    };*/

}
