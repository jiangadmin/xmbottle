package com.wt.piaoliuping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoResult;
import com.wt.piaoliuping.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangtao on 2017/11/14.
 */

public class VipAdapter extends BaseItemAdapter {

    public VipAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_vip, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HaoResult result = (HaoResult) dataList.get(position);
        holder.textDuration.setText(result.findAsString("goodsName"));
        holder.textPrice.setText(result.findAsString("goodsContent"));
        holder.textTotalPrice.setText(result.findAsString("goodsMoney"));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.text_duration)
        TextView textDuration;
        @BindView(R.id.text_price)
        TextView textPrice;
        @BindView(R.id.text_total_price)
        TextView textTotalPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
