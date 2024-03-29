package com.haoxitech.HaoConnect;

import android.text.TextUtils;

/**
 * HaoConfig配置
 */
public class HaoConfig {

    private static String Clientversion = "1.0";

    public static String getClientInfo() {
        return "android";
    }

    public static void setClientVersion(String clientVersion) {
        Clientversion = clientVersion;
    }

    public static String getClientVersion() {
        return Clientversion;
    }

    public static String getSecretHax() {
        return "secret=floating_@^_^O(∩_∩)O~3WaCaLeiGEDJ";
    }

    public static String getApiHost() {
        if (TextUtils.isEmpty(HaoConnect.getString("sec")) || HaoConnect.getString("sec").equals("prod")) {
            return "api.xhplpz.com";
        } else {
            return "floating.api.yemaoka.com";
        }
    }

}