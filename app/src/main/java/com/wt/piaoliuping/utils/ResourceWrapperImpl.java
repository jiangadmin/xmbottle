package com.wt.piaoliuping.utils;

import android.app.Application;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;

/**
 * ProductName:DFYJ
 * PackageName:com.dfzq.dfyj.basis.app
 * Dage:2016/9/7
 * Author:Fredric
 * Coding is an art not science
 */
public class ResourceWrapperImpl implements ResourceWrapper {
    private Application application;
    private Resources resources;

    public ResourceWrapperImpl(Application application) {
        this.application = application;
        this.resources = application.getResources();
    }

    @Override
    public Drawable getDrawable(int id) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = resources.getDrawable(id, application.getTheme());
        } else {
            drawable = resources.getDrawable(id);
        }
        return drawable;
    }

    @Override
    public String getString(int id) {
        return resources.getString(id);
    }

    @Override
    public String getString(@StringRes int id, Object... formatArgs) {
        return resources.getString(id, formatArgs);
    }

    @Override
    public String[] getStringArray(@ArrayRes int id) {
        return resources.getStringArray(id);
    }

    @Override
    public int getColor(int id) {
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = resources.getColor(id, application.getTheme());
        } else {
            color = resources.getColor(id);
        }
        return color;
    }

    @Override
    public String getArrayString(@ArrayRes int id, String key){
        String[] values = resources.getStringArray(id);
//        if (values.length % 2 != 0) {
////            throw new Exception("biz dict init error:key value not match");
//            return "";
//        }
        int index = -1;
        int length = values.length / 2;
        for (int i = 0; i < length; i++) {
            if (values[i * 2].equals(key)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            return values[index * 2 + 1];
        } else {
            return null;
        }
    }

    @Override
    public String getArrayString(@ArrayRes int id, int index) {
        String[] values = resources.getStringArray(id);
        if (index < values.length) {
            return values[index];
        } else {
            return null;
        }
    }
}
