package com.wt.piaoliuping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wt.piaoliuping.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangtao on 2017/10/26.
 */

public class NameAdapter extends BaseItemAdapter {

    public NameAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null)  {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_name, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_image)
        ImageView itemImage;
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.text_desc)
        TextView textDesc;
        @BindView(R.id.text_time)
        TextView textTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
