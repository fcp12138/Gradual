package com.fcp.gradual;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.fcp.gradual.fragment.LazyFragment;
import com.fcp.gradual.fragment.impl.FirstFragment;
import com.fcp.gradual.fragment.impl.FouthFragment;
import com.fcp.gradual.fragment.impl.SecondFragment;
import com.fcp.gradual.fragment.impl.ThirdFragment;
import com.fcp.gradual.view.GradualChangeBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_app_main_gradual_change_bar_id)
    GradualChangeBar mGradualChangeBar;
    @BindView(R.id.activity_app_main_viewpager_id)
    ViewPager mViewPager;

    /**
     * 界面容器
     */
    private ArrayList<LazyFragment> pageList;
    private LazyFragment oneFragment, twoFragment, threeFragment,fourFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //
        mGradualChangeBar.setViewPager(mViewPager, new MySelectIdChanged());
        mViewPager.setOffscreenPageLimit(4);//保留4个界面
        //添加页面
        pageList = new ArrayList<>();
        oneFragment = new FirstFragment();
        twoFragment = new SecondFragment();
        threeFragment = new ThirdFragment();
        fourFragment = new FouthFragment();
        pageList.add(oneFragment);
        pageList.add(twoFragment);
        pageList.add(threeFragment);
        pageList.add(fourFragment);
        //设置适配器
        MyFragmentPagerAdapter pageAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(pageAdapter);
        //显示第一个
    }

    /**
     * 选择监听类
     */
    class MySelectIdChanged implements GradualChangeBar.OnSelectIdChanged {

        @Override
        public void selectOne() {
        }

        @Override
        public void selectTwo() {
        }

        @Override
        public void selectThree() {
        }

        @Override
        public void selectFour() {
        }
    }

    /**
     * pageAdapter
     */
    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return pageList.get(position);
        }

        @Override
        public int getCount() {
            return pageList.size();
        }
    }
}
