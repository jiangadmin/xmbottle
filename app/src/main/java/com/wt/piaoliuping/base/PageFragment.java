package com.wt.piaoliuping.base;

import android.os.Bundle;
import android.support.annotation.Nullable;


/**
 * ProductName:DFYJ
 * PackageName:com.dfzq.dfyj.finance.view.fragment
 * Dage:2016/8/16
 * Author:Fredric
 * Coding is an art not science
 */
public abstract class PageFragment extends BaseFragment{
    // 界面是否可见
    private boolean isVisible;

    // 界面是否初始化
    private boolean isInitialized;

    // 界面是否加载
    private boolean isLoaded;

    protected void fetchData(){

    }

    protected void disappear() {

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isInitialized = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        prepareFetchData();
        prepareDisappear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInitialized = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isLoaded = false;
    }

    public boolean prepareFetchData() {
        return prepareFetchData(true);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisible && isInitialized && (!isLoaded || forceUpdate)) {
            isLoaded = true;
            fetchData();
            return true;
        }
        return false;
    }

    public boolean prepareDisappear() {
        if (!isVisible) {
            disappear();
            return true;
        }
        return false;
    }
}
