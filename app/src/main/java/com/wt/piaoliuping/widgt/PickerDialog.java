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

import com.aigestudio.wheelpicker.WheelPicker;
import com.wt.piaoliuping.R;
import com.wt.piaoliuping.database.AreaDBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangtao on 2017/5/1.
 */

public class PickerDialog extends Dialog {
    public PickerDialog(@NonNull Context context) {
        super(context);
    }

    public PickerDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected PickerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static PickerDialog create(Context context, final PickerResultCallBack callBack, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final PickerDialog dialog = new PickerDialog(context, R.style.dialog);

        View layout = inflater.inflate(R.layout.dialog_picker, null);
        dialog.addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        final List<Map<String, Object>> dataList = AreaDBHelper.chooseAreaInfo("0");
        final List<Map<String, Object>> dataList2 = new ArrayList<>();
        final List<Map<String, Object>> dataList3 = new ArrayList<>();

        final WheelPicker picker = layout.findViewById(R.id.picker1);
        final WheelPicker picker2 = layout.findViewById(R.id.picker2);
        final WheelPicker picker3 = layout.findViewById(R.id.picker3);
        buildPicker(picker, context);
        buildPicker(picker2, context);
        buildPicker(picker3, context);

        List<String> pick1data = buildData(dataList);
        picker.setData(pick1data);
        picker.setSelectedItemPosition(position);
        picker2.setData(new ArrayList());
        picker3.setData(new ArrayList());

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setWindowAnimations(R.style.introduction_pop);

        TextView done = layout.findViewById(R.id.btn_sure);
        TextView cancel = layout.findViewById(R.id.btn_cancel);

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
                if (callBack != null) {
                    callBack.choose(dataList3.get(picker.getCurrentItemPosition()).get("areaName").toString(), dataList3.get(picker.getCurrentItemPosition()).get("id").toString());
                }
            }
        });
        picker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {

                List<Map<String, Object>> data2 = new ArrayList<>();
                data2 = AreaDBHelper.chooseAreaInfo(dataList.get(picker.getCurrentItemPosition()).get("id").toString());
                dataList2.clear();
                dataList2.addAll(data2);
                picker2.setData(buildData(data2));
                picker2.setSelectedItemPosition(0);
            }
        });
        picker2.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                List<Map<String, Object>> data3 = new ArrayList<>();
                data3 = AreaDBHelper.chooseAreaInfo(dataList2.get(picker2.getCurrentItemPosition()).get("id").toString());
                dataList3.clear();
                dataList3.addAll(data3);
                picker3.setData(buildData(data3));
                picker3 .setSelectedItemPosition(0);
            }
        });
        return dialog;
    }

    private static List<String> buildData(List<Map<String, Object>> dataList ) {
        List<String> result = new ArrayList<>();
        for (Map<String, Object> map : dataList) {
            result.add(map.get("areaName").toString());
        }
        return result;
    }

    private static WheelPicker buildPicker(WheelPicker picker, Context context) {
        picker.setVisibleItemCount(5);
        picker.setSelectedItemTextColor(context.getResources().getColor(R.color.redColor));
        picker.setCurtain(false);
        picker.setAtmospheric(true);
        picker.setCurved(true);
        return picker;
    }

    public interface PickerResultCallBack {
        void choose(String result, String resultId);
    }
}
