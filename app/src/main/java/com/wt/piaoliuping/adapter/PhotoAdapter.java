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

    ItemLongClickListener onLongClickListener;

    public ItemLongClickListener getOnLongClickListener() {
        return onLongClickListener;
    }

    public void setOnLongClickListener(ItemLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public PhotoAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onLongClickListener != null) {
                    onLongClickListener.onLongClick(view, position);
                }
                return true;
            }
        });
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_image)
        ImageView itemImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface ItemLongClickListener {
        public void onLongClick(View view, int position);
    }
}
