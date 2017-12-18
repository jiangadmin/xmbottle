package com.wt.piaoliuping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoxitech.HaoConnect.HaoResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wt.piaoliuping.App;
import com.wt.piaoliuping.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangtao on 2017/11/14.
 */

public class GoodsAdapter extends BaseItemAdapter {

    ItemClickListener itemClickListener;

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public GoodsAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_goods, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.click(holder.btnSubmit, position);
                }
            }
        });
        HaoResult result = (HaoResult) dataList.get(position);
        holder.textPoint.setText(result.findAsString("goodsPrice") + "星星");
        ImageLoader.getInstance().displayImage(result.findAsString("goodsImgView"), holder.imagePrize);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.image_prize)
        ImageView imagePrize;
        @BindView(R.id.text_point)
        TextView textPoint;
        @BindView(R.id.btn_submit)
        Button btnSubmit;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface ItemClickListener {
        void click(View v, int position);
    }
}
