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
 * Created by wangtao on 2017/10/26.
 */

public class WithdrawAccountAdapter extends BaseItemAdapter {

    public WithdrawAccountAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_withdrew_account, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HaoResult haoResult = (HaoResult) dataList.get(position);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.text_account)
        TextView textAccount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
