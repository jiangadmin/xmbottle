package com.wt.piaoliuping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.utils.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangtao on 2017/11/14.
 */

public class MinePrizeAdapter extends BaseItemAdapter {

    ItemClickListener itemClickListener;

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MinePrizeAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_mine_prize, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HaoResult result = (HaoResult) dataList.get(position);
        String temp;
        temp = "在" + result.findAsString("buyTime") + " ";
        if (result.findAsInt("sourceType ") == 1) {
            temp += "由" + result.findAsString("userLocal>nickname") + " ";
        } else {
            temp += "由" + result.findAsString("sourceUserLocal>nickname") + " ";
        }
        temp += result.findAsString("sourceTypeLabel") + " ";
        temp += result.findAsString("goodsName") + "一个";
        holder.textName.setText(temp);
        return convertView;
    }

    public interface ItemClickListener {
        void click(View v, int position);
    }

    static class ViewHolder {
        @BindView(R.id.text_name)
        TextView textName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
