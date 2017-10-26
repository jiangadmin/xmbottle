package com.wt.piaoliuping.manager;

import android.text.TextUtils;

import com.haoxitech.HaoConnect.HaoConnect;
import com.haoxitech.HaoConnect.HaoUtility;

/**
 * Created by wangtao on 2017/10/26.
 */

public class UserManager {

    private static UserManager userManager;

    private String userId;

    public static UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        HaoConnect.putString("userId", userId);
    }

    public String getUserId() {
        if (TextUtils.isEmpty(userId)) {
            userId = HaoConnect.getString("userId");
        }
        return userId;
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(HaoConnect.getUserid());
    }

    public void logout() {
        setUserId("");
    }
}
