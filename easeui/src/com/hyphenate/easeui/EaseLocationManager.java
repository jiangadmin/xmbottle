package com.hyphenate.easeui;

/**
 * Created by wangtao on 2018/1/5.
 */

public class EaseLocationManager {

    public double latitude;
    public double longitude;

    private static EaseLocationManager manager;

    public static EaseLocationManager getInstance() {
        if (manager == null) {
            manager = new EaseLocationManager();
        }
        return manager;
    }
}
