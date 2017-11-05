package com.wt.piaoliuping.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wt.piaoliuping.R;

import butterknife.ButterKnife;

/**
 * Created by wangtao on 2017/10/25.
 */

public abstract class BaseFragment extends Fragment {

    private TextView titleText;
    private ImageButton backBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(getFragmentLayoutId(), null);
        ButterKnife.bind(this, view);
        initView(view);
        try {
            backBtn = view.findViewById(R.id.btn_back);
            backBtn.setVisibility(View.GONE);
        } catch (Exception e) {

        }
        return view;
    }

    public void initView(View view) {
        titleText = view.findViewById(R.id.text_title);
    }

    public void setTitle(final String title) {
        if (titleText != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    titleText.setText(title);
                }
            });
        }
    }

    public void hideBackBtn() {
        if (backBtn != null) {
            backBtn.setVisibility(View.GONE);
        }
    }

    public abstract int getFragmentLayoutId();

    public void showToast(String notice) {
        try {
            Toast.makeText(getActivity(), notice, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
