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
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.hyphenate.easeui.R;

import java.util.ArrayList;
import java.util.List;

public class EaseBaiduMapActivity extends EaseBaseActivity implements BaiduMap.OnMapStatusChangeListener, AdapterView.OnItemClickListener {

    private final static String TAG = "map";
    static MapView mMapView = null;
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    Button sendButton = null;
    static BDLocation lastLocation = null;
    ProgressDialog progressDialog;
    private BaiduMap mBaiduMap;
    private boolean showLocation;
    private AddressAdapter addressAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize SDK with context, should call this before setContentView
        setContentView(R.layout.ease_activity_baidumap);
        mMapView = findViewById(R.id.bmapView);

        sendButton = findViewById(R.id.btn_location_send);
        ListView listView = findViewById(R.id.list_view);
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        mMapView.setLongClickable(true);
        addressAdapter = new AddressAdapter(this);
        listView.setAdapter(addressAdapter);
        listView.setOnItemClickListener(this);

        if (latitude == 0) {
            showMapWithLocationClient();
        } else {
            double longitude = intent.getDoubleExtra("longitude", 0);
            String address = intent.getStringExtra("address");
            showMap(latitude, longitude, address);
            listView.setVisibility(View.GONE);
        }
    }

    private void showMap(double latitude, double longtitude, String address) {
        sendButton.setVisibility(View.GONE);
        LatLng llA = new LatLng(latitude, longtitude);
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
                finish();
            }
        });

        progressDialog.show();

        mBaiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mBaiduMap.setOnMapStatusChangeListener(this);
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
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (addressAdapter.data.size() == 0) {
            return;
        }
        lastPoint = addressAdapter.data.get(position);
        addressAdapter.setSelectIndex(position);
    }

    /**
     * format new location to string and show on screen
     */
    private class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null || mMapView == null) {
                return;
            }

            if (lastLocation != null) {
                if (lastLocation.getLatitude() == location.getLatitude() && lastLocation.getLongitude() == location.getLongitude()) {
                    Log.d("map", "same location, skip refresh");
                    // mMapView.refresh(); //need this refresh?
                    return;
                }
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (!showLocation) {
                showLocation = true;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                /*List<PoiInfo> list = new ArrayList<>();
                PoiInfo poiInfo = new PoiInfo();
                poiInfo.location = ll;
                poiInfo.address = lastAddress;
                poiInfo.name = lastLocation.getAddress().street;
                list.add(poiInfo);
                addressAdapter.setData(list);*/
                recodeAddress(ll);
            }
            sendButton.setEnabled(true);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    public void back(View v) {
        finish();
    }

    private PoiInfo lastPoint;

    public void sendLocation(View view) {
        if (lastPoint == null) {
            Toast.makeText(this, "请选择一个位置", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = this.getIntent();
        intent.putExtra("latitude", lastPoint.location.latitude);
        intent.putExtra("longitude", lastPoint.location.longitude);
        intent.putExtra("address", lastPoint.address);
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
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        Log.e("wt", "onMapStatusChangeFinish");
        if (showLocation) {
            recodeAddress(mapStatus.target);
        }
    }

    private GeoCoder geoCoder;

    private void recodeAddress(LatLng mapStatus) {
        if (geoCoder == null) {
            geoCoder = GeoCoder.newInstance();
        }
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(mapStatus));
        geoCoder.setOnGetGeoCodeResultListener(listener);
    }

    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {

        public void onGetGeoCodeResult(GeoCodeResult result) {

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
                return;
            }
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                return;
            }
            addressAdapter.setData(result.getPoiList());
        }
    };

    private class AddressAdapter extends BaseAdapter {

        private Context context;

        private int selectIndex = 0;

        public void setSelectIndex(int selectIndex) {
            this.selectIndex = selectIndex;
            notifyDataSetChanged();
        }

        AddressAdapter(Context context) {
            this.context = context;
        }

        public List<PoiInfo> data = new ArrayList<>();

        public void setData(List<PoiInfo> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_address, null);
                holder.name = convertView.findViewById(R.id.text_name);
                holder.address = convertView.findViewById(R.id.text_address);
                holder.linearLayout = convertView.findViewById(R.id.layout_content);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PoiInfo info = data.get(position);
            if (position == 0) {
                lastPoint = info;
            }
            holder.name.setText(info.name);
            holder.address.setText(info.address);
            if (selectIndex == position) {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#eeeeee"));
            } else {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            return convertView;
        }

        class ViewHolder {
            TextView name;
            TextView address;
            LinearLayout linearLayout;
        }
    }
}
