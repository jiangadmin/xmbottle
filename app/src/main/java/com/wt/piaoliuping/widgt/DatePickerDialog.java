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
import android.widget.TextView;

import com.aigestudio.wheelpicker.widgets.WheelDatePicker;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.utils.DateUtils;

import java.util.Date;

/**
 * Created by wangtao on 2017/5/1.
 */

public class DatePickerDialog extends Dialog {
    private Date chooseDate;

    public DatePickerDialog(@NonNull Context context) {
        super(context);
    }

    public DatePickerDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected DatePickerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static DatePickerDialog createDialog(Context context, final DatePickerResultCallBack callBack) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final DatePickerDialog dialog = new DatePickerDialog(context, R.style.dialog);

        View layout = inflater.inflate(R.layout.dialog_date_picker, null);
        dialog.addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        final WheelDatePicker picker = layout.findViewById(R.id.picker);
//        picker.setData(data);

        picker.setVisibleItemCount(5);
//        picker.setSelectedItemPosition(position);
        picker.setSelectedItemTextColor(context.getResources().getColor(R.color.redColor));
        picker.setCurtain(false);
//        picker.setIndicator(true);
//        picker.setIndicatorColor(context.getResources().getColor(R.color.lightWhite));
        picker.setAtmospheric(true);
        picker.setCurved(true);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setWindowAnimations(R.style.introduction_pop);

        TextView done = (TextView) layout.findViewById(R.id.btn_sure);
        TextView cancel = (TextView) layout.findViewById(R.id.btn_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if (callBack != null && dialog.chooseDate != null) {
//                    String time = "";
//                    time += picker.getYear() + "-";
//                    time += picker.getMonth() > 10 ? picker.getMonth() + "" : "0" + picker.getMonth();
//                    time += "-";
//                    time += picker.getSelectedDay() > 10 ? picker.getSelectedDay() + "" : "0" + picker.getSelectedDay();
                    callBack.choose(DateUtils.getStringDateFromDate(dialog.chooseDate));
                }
            }
        });
        picker.setOnDateSelectedListener(new WheelDatePicker.OnDateSelectedListener() {
            @Override
            public void onDateSelected(WheelDatePicker picker, Date date) {
                dialog.chooseDate = date;
            }
        });
        dialog.chooseDate = picker.getCurrentDate();
        return dialog;
    }


    public interface DatePickerResultCallBack {
        void choose(String time);
    }
}
