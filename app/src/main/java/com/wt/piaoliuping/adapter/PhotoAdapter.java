package com.wt.piaoliuping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.haoxitech.HaoConnect.HaoResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangtao on 2017/10/26.
 */

public class PhotoAdapter extends BaseItemAdapter {

    public PhotoAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_photo, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HaoResult haoResult = (HaoResult) dataList.get(position);
        ImageLoader.getInstance().displayImage(haoResult.findAsString("photoPreview"), holder.itemImage);
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_image)
        ImageView itemImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
