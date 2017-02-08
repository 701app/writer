package com.example.writer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.fragment.*;
import com.example.tool.Main_bottom_change;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

public class Main extends AppCompatActivity implements View.OnClickListener , ViewPager.OnPageChangeListener {

    public RecentFragment recentfragment;
    private BookcaseFragment bookcasefragment;
    private SettingFragment settingfragment;
    private List<Fragment> fragment_list = new ArrayList<Fragment>();
    private FragmentPagerAdapter fragmentpager_adapter;
    private ViewPager fragment_viewpager;
    private List<Main_bottom_change> mTabIndicator = new ArrayList<Main_bottom_change>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);

        Bmob.initialize(this, "280022b948e8d4bd93d20a5618e99f49");//初始化Bmob数据库

        initDatas();

        fragment_viewpager.setAdapter(fragmentpager_adapter);
        fragment_viewpager.setOnPageChangeListener(this);



    }

    private void initDatas() {
        recentfragment = new RecentFragment();
        fragment_list.add(recentfragment);

        bookcasefragment = new BookcaseFragment();
        fragment_list.add(bookcasefragment);

        settingfragment = new SettingFragment();
        fragment_list.add(settingfragment);

        fragmentpager_adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragment_list.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragment_list.get(arg0);
            }
        };

        initView();

    }

    private void initView() {
        // TODO Auto-generated method stub
        fragment_viewpager = (ViewPager) findViewById(R.id.fragment_viewpager);

        Main_bottom_change one = (Main_bottom_change) findViewById(R.id.recent);
        Main_bottom_change two = (Main_bottom_change) findViewById(R.id.bookcase);
        Main_bottom_change three = (Main_bottom_change) findViewById(R.id.setting);

        mTabIndicator.add(one);
        mTabIndicator.add(two);
        mTabIndicator.add(three);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);

        one.setIconAlpha(1.0f);
    }

    @Override
    public void onClick(View v) {

        resetOtherTabs();

        switch (v.getId()) {
            case R.id.recent:
                mTabIndicator.get(0).setIconAlpha(1.0f);
                fragment_viewpager.setCurrentItem(0, false);
                break;
            case R.id.bookcase:
                mTabIndicator.get(1).setIconAlpha(1.0f);
                fragment_viewpager.setCurrentItem(1, false);
                break;
            case R.id.setting:
                mTabIndicator.get(2).setIconAlpha(1.0f);
                fragment_viewpager.setCurrentItem(2, false);
                break;

        }

    }

    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicator.size(); i++) {
            mTabIndicator.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (positionOffset > 0) {
            Main_bottom_change left = mTabIndicator.get(position);
            Main_bottom_change right = mTabIndicator.get(position + 1);

            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);

        }

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
