package com.wt.piaoliuping.base;

import android.app.Activity;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.Stack;

/**
 * 栈堆管理
 */
public class AppManager {
    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }

        return instance;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack();
        }

        activityStack.add(activity);
    }

    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

    public Activity currentActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            return null;
        } else {
            Activity activity = (Activity) activityStack.lastElement();
            return activity;
        }
    }

    public void finishActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            Activity activity = (Activity) activityStack.lastElement();
            this.finishActivity(activity);
        }
    }

    public void finishActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activityStack.remove(activity);
                activity.finish();
                activity = null;
            }

        }
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack != null && activityStack.size() > 0)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }


    public void finishActivity(Class<?> cls) {
        if (activityStack != null && activityStack.size() > 0) {
            Iterator iterator = activityStack.iterator();

            while (iterator.hasNext()) {
                Activity activity = (Activity) iterator.next();
                if (activity != null && activity.getClass().equals(cls)) {
                    activity.finish();
                    activity = null;
                    iterator.remove();
                }
            }

        }
    }

    public void finishAllActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            Iterator iterator = activityStack.iterator();

            while (iterator.hasNext()) {
                Activity activity = (Activity) iterator.next();
                if (activity != null && !activity.isFinishing()) {
                    activity.finish();
                }
            }

            activityStack.clear();
        }
    }

    public void finishAllActivityExceptMe(Class cls) {
        if (activityStack != null && activityStack.size() > 0) {
            Iterator iterator = activityStack.iterator();

            while (iterator.hasNext()) {
                Activity activity = (Activity) iterator.next();
                if (activity != null && !activity.getClass().equals(cls)) {
                    activity.finish();
                    activity = null;
                    iterator.remove();
                }
            }

        }
    }

    public Activity getActivity(String activityName) {
        Iterator iterator = activityStack.iterator();

        Activity activity;
        do {
            if (!iterator.hasNext()) {
                return null;
            }

            activity = (Activity) iterator.next();
        }
        while (activity == null || !TextUtils.equals(activity.getClass().getName(), activityName));

        return activity;
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {

            finishAllActivity();
            // 杀死该应用进程
            Process.killProcess(Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }


}