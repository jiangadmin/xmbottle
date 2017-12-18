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

public class WarningAdapter extends BaseItemAdapter {

    public WarningAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        if (dataList.size() > 4) {
            return 4;
        } else {
            return dataList.size() + 1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_warning, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == dataList.size()) {
            holder.textAdd.setVisibility(View.VISIBLE);
            holder.itemImage.setVisibility(View.GONE);
        } else {
            holder.textAdd.setVisibility(View.GONE);
            holder.itemImage.setVisibility(View.VISIBLE);

            String haoResult = dataList.get(position).toString();
            ImageLoader.getInstance().displayImage("http://floating.img.yemaoka.com/" + haoResult, holder.itemImage, App.app.getImageCircleOptions());
        }
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_image)
        ImageView itemImage;
        @BindView(R.id.text_add)
        TextView textAdd;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
