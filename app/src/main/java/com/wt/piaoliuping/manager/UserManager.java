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
    private String password;

    public static UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        HaoConnect.putString("userID", userId);
    }

    public String getUserId() {
        if (TextUtils.isEmpty(userId)) {
            userId = HaoConnect.getString("userID");
        }
        return userId;
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(HaoConnect.getUserid());
    }

    public void logout() {
        setUserId("");
    }

    public String getPassword() {
        if (TextUtils.isEmpty(password)) {
            password = HaoConnect.getString("password");
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        HaoConnect.putString("password", userId);
    }
}
