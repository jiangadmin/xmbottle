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

public class RechargeAdapter extends BaseItemAdapter {

    public RechargeAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_recharge, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HaoResult result = (HaoResult) dataList.get(position);
        holder.textMoney.setText("￥" + result.findAsString("goodsMoney"));
        holder.textMoneyDesc.setText(result.findAsString("goodsName"));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.text_money)
        TextView textMoney;
        @BindView(R.id.text_money_desc)
        TextView textMoneyDesc;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
