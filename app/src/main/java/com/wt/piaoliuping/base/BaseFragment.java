package com.wt.piaoliuping.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.widgt.CustomProgressDialog;

import butterknife.ButterKnife;

/**
 * Created by wangtao on 2017/10/25.
 */

public abstract class BaseFragment extends Fragment {

    private TextView titleText;
    private ImageButton backBtn;

    ImageView emptyImage;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(getFragmentLayoutId(), null);
        ButterKnife.bind(this, view);
        customProgressDialog = new CustomProgressDialog(getActivity());
        initView(view);
        try {
            backBtn = view.findViewById(R.id.btn_back);
            backBtn.setVisibility(View.GONE);
        } catch (Exception e) {

        }

        if (emptyImage == null) {
            emptyImage =  view.findViewById(R.id.no_data);
        }
        if (emptyImage != null) {
            emptyImage.setVisibility(View.GONE);
        }
        rootView = view;
        return view;
    }

    public void showNoData() {
        if (emptyImage != null) {
            emptyImage.setVisibility(View.VISIBLE);
        }
    }

    public void hideNoData() {
        if (emptyImage != null) {
            emptyImage.setVisibility(View.GONE);
        }
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
            if (TextUtils.isEmpty(notice)) {
                return;
            }
            Toast.makeText(getActivity(), notice, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public CustomProgressDialog customProgressDialog;

    public void startLoading() {
        customProgressDialog.startProgressDialog();
    }

    public void stopLoading() {
        customProgressDialog.stopProgressDialog();
    }
}
