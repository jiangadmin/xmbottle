package com.wt.piaoliuping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.App;
import com.wt.piaoliuping.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangtao on 2017/10/26.
 */

public class NearbyAdapter extends BaseItemAdapter {

    public NearbyAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_nearby, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HaoResult result = (HaoResult) dataList.get(position);
        ImageLoader.getInstance().displayImage(result.findAsString("userIDLocal>avatarPreView"), holder.itemImage, App.app.getImageCircleOptions());
        holder.textName.setText(result.findAsString("userIDLocal>nickname"));
        if (result.findAsString("userIDLocal>declaration").isEmpty()) {
            holder.textDesc.setVisibility(View.GONE);
        } else {
            holder.textDesc.setVisibility(View.VISIBLE);
            holder.textDesc.setText(result.findAsString("userIDLocal>declaration"));
        }
//        holder.textTime.setText(result.findAsString("distanceLabel"));
        int sex = result.findAsInt("userIDLocal>sex");
        if (sex == 1) {
            holder.textSex.setBackgroundResource(R.drawable.icon_boy);
        } else {
            holder.textSex.setBackgroundResource(R.drawable.icon_girl);
        }
//        holder.textSex.setText(result.findAsString("userIDLocal>sexLabel"));
        try {
            holder.textArea.setText(result.findAsString("userIDLocal>areaLabel").split("-")[2]);
        } catch (Exception e) {

        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_image)
        ImageView itemImage;
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.text_sex)
        TextView textSex;
        @BindView(R.id.text_time)
        TextView textTime;
        @BindView(R.id.text_area)
        TextView textArea;
        @BindView(R.id.text_desc)
        TextView textDesc;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
