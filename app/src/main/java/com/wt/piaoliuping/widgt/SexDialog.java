package com.wt.piaoliuping.widgt;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.wt.piaoliuping.R;


/**
 * Created by wangtao on 2017/5/6.
 */

public class SexDialog extends Dialog {
    public SexDialog(@NonNull Context context) {
        super(context);
    }

    public SexDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected SexDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static SexDialog create(Context context, final View.OnClickListener onClickListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final SexDialog dialog = new SexDialog(context, R.style.dialog);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setWindowAnimations(R.style.introduction_pop);

        View layout = inflater.inflate(R.layout.dialog_sex, null);
        dialog.addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        layout.findViewById(R.id.layout_male).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
        layout.findViewById(R.id.layout_female).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
        layout.findViewById(R.id.layout_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
        return dialog;
    }
}
