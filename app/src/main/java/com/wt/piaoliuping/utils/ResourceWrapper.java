package com.wt.piaoliuping.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * ProductName:DFYJ
 * PackageName:com.dfzq.dfyj.basis.app
 * Dage:2016/9/7
 * Author:Fredric
 * Coding is an art not science
 */
public interface ResourceWrapper {
    Drawable getDrawable(@DrawableRes int id);

    String getString(@StringRes int id);

    String getString(@StringRes int id, Object... formatArgs);

    int getColor(@ColorRes int id);

    String[] getStringArray(@ArrayRes int id);

    /**
     * 根据id及key，从string array中去值
     *
     * @param id
     * @param key
     * @return
     */
    String getArrayString(@ArrayRes int id, String key);

    String getArrayString(@ArrayRes int id, int index);
}
