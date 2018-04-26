package com.wt.piaoliuping.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.utils.LogUtil;

/**
 * @author: jiangyao
 * @date: 2017/11/30.
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 弹框
 */

public class Base_Dialog extends Dialog {
    private static final String TAG = "Base_Dialog";

    TextView dialog_title, dialog_message;
    Button dialog_esc, dialog_ok;
    LinearLayout bottom, dismis;
    ImageView dialog_icon;

    public Base_Dialog(@NonNull Context context) {
        super(context, R.style.myDialogTheme);
        try {
            show();
        } catch (Exception e) {

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //把状态栏设置为透明

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //透明状态栏
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.dialog_base);

        initview();

    }


    private void initview() {
        dialog_icon = findViewById(R.id.dialog_icon);
        dialog_title = findViewById(R.id.dialog_title);
        dialog_message = findViewById(R.id.dialog_message);
        dialog_esc = findViewById(R.id.dialog_esc);
        dialog_ok = findViewById(R.id.dialog_ok);
        bottom = findViewById(R.id.dialog_bottom);
        dismis = findViewById(R.id.dialog_dismis);

        dismis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
    }

    public void setIcon(int i) {
        dialog_icon.setVisibility(View.VISIBLE);
        dialog_icon.setImageResource(i);
    }

    public void setTitle(String t) {

        dialog_title.setVisibility(View.VISIBLE);
        dialog_title.setText(t);
    }

    public void setMessage(String m) {

        dialog_message.setVisibility(View.VISIBLE);
        dialog_message.setText(m);
    }

    public void setEsc(String e, final View.OnClickListener listener) {
        bottom.setVisibility(View.VISIBLE);
        dialog_esc.setVisibility(View.VISIBLE);
        dialog_esc.setText(e);
        dialog_esc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dismiss();
            }
        });
    }

    public void setOk(String e, final View.OnClickListener listener) {

        bottom.setVisibility(View.VISIBLE);
        dialog_ok.setVisibility(View.VISIBLE);
        dialog_ok.setText(e);
        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dismiss();
            }
        });
    }

    @Override
    public void setCancelable(boolean flag) {
        LogUtil.e(TAG, "初始化这里了");
        dismis.setEnabled(flag);
        super.setCancelable(flag);

    }

}
