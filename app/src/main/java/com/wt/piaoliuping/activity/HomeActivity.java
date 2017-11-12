package com.wt.piaoliuping.activity;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.wt.piaoliuping.R;
import com.wt.piaoliuping.base.BaseTitleActivity;
import com.wt.piaoliuping.fragment.BottleFragment;
import com.wt.piaoliuping.fragment.MessageFragment;
import com.wt.piaoliuping.fragment.NearbyFragment;
import com.wt.piaoliuping.fragment.MineFragment;
import com.wt.piaoliuping.widgt.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wangtao on 2017/10/20.
 */

public class HomeActivity extends BaseTitleActivity {

    @BindView(R.id.message_btn)
    RadioButton messageBtn;
    @BindView(R.id.home_btn)
    RadioButton homeBtn;
    @BindView(R.id.activity_btn)
    RadioButton activityBtn;
    @BindView(R.id.mine_btn)
    RadioButton mineBtn;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.view_pager)
    CustomViewPager viewPager;

    @Override
    public void initView() {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.message_btn:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.home_btn:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.activity_btn:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.mine_btn:
                        viewPager.setCurrentItem(3);
                        break;
                }
            }
        });
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_home;
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(new MessageFragment());
            fragments.add(new BottleFragment());
            fragments.add(new NearbyFragment());
            fragments.add(new MineFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
                exitTime = System.currentTimeMillis();
            } else {
                exitTime = 0;
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
