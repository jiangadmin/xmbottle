package com.wt.piaoliuping.manager;

import android.text.TextUtils;

import com.haoxitech.HaoConnect.HaoUtility;

/**
 * Created by wangtao on 2017/11/12.
 */

public class HXManager {
    public static HXManager instance;

    public static HXManager getInstance() {
        if (instance == null) {
            instance = new HXManager();
        }
        return instance;
    }

    public String formatPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return "";
        }
        String temp = HaoUtility.encodeMD5String(password);
        String md5Temp = HaoUtility.encodeMD5String(temp + "@_2017_daTing" + temp.substring(2, 7));
        return md5Temp;
    }
}
