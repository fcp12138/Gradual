package com.fcp.gradual.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import com.fcp.gradual.R;

import java.util.ArrayList;


/**
 * 渐变导航栏
 * Created by fcp on 2015/9/22.
 */
public class GradualChangeBar extends LinearLayout implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private GradualChangeView one;
    private GradualChangeView two;
    private GradualChangeView three;
    private GradualChangeView four;

    private ArrayList<GradualChangeView> mGradualChangeViews =new ArrayList<>();

    private ViewPager viewPager;
    private OnSelectIdChanged mOnSelectIdChanged;

    private int selectId;


    public GradualChangeBar(Context context) {
        super(context);
        init(context);
    }

    public GradualChangeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GradualChangeBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundResource(R.drawable.layer_list_graduallchangebar);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.activity_app_main_graduallbar, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        one = (GradualChangeView) findViewById(R.id.activity_app_main_gradual_bar_one);
        two = (GradualChangeView) findViewById(R.id.activity_app_main_gradual_bar_two);
        three = (GradualChangeView) findViewById(R.id.activity_app_main_gradual_bar_three);
        four = (GradualChangeView) findViewById(R.id.activity_app_main_gradual_bar_four);

        mGradualChangeViews.add(one);
        mGradualChangeViews.add(two);
        mGradualChangeViews.add(three);
        mGradualChangeViews.add(four);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

    }


    /**
     * 设置ViewPager
     * @param viewPager ViewPager
     */
    public void setViewPager(ViewPager viewPager, OnSelectIdChanged onSelectIdChanged) {
        this.viewPager = viewPager;
        this.mOnSelectIdChanged = onSelectIdChanged;
        //初始化第一项
        viewPager.setCurrentItem(0,false);
        one.setIconAlpha(1);
        selectId = one.getId();
        //监听
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == selectId){
            return ;
        }
        selectId = id;
        switch (id){
            case R.id.activity_app_main_gradual_bar_one:
                one.setIconAlpha(1);
                two.setIconAlpha(0);
                three.setIconAlpha(0);
                four.setIconAlpha(0);
                viewPager.setCurrentItem(0,false);
                if(mOnSelectIdChanged !=null) mOnSelectIdChanged.selectOne();
                break;
            case R.id.activity_app_main_gradual_bar_two:
                one.setIconAlpha(0);
                two.setIconAlpha(1);
                three.setIconAlpha(0);
                four.setIconAlpha(0);
                viewPager.setCurrentItem(1, false);
                if(mOnSelectIdChanged !=null) mOnSelectIdChanged.selectTwo();
                break;
            case R.id.activity_app_main_gradual_bar_three:
                one.setIconAlpha(0);
                two.setIconAlpha(0);
                three.setIconAlpha(1);
                four.setIconAlpha(0);
                viewPager.setCurrentItem(2, false);
                if(mOnSelectIdChanged !=null) mOnSelectIdChanged.selectThree();
                break;
            case R.id.activity_app_main_gradual_bar_four:
                one.setIconAlpha(0);
                two.setIconAlpha(0);
                three.setIconAlpha(0);
                four.setIconAlpha(1);
                viewPager.setCurrentItem(3, false);
                if(mOnSelectIdChanged !=null) mOnSelectIdChanged.selectFour();
                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            GradualChangeView left = mGradualChangeViews.get(position);
            GradualChangeView right = mGradualChangeViews.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
        selectId = mGradualChangeViews.get(position).getId();
        switch (position){
            case 0:
                if(mOnSelectIdChanged !=null) mOnSelectIdChanged.selectOne();
                break;
            case 1:
                if(mOnSelectIdChanged !=null) mOnSelectIdChanged.selectTwo();
                break;
            case 2:
                if(mOnSelectIdChanged !=null) mOnSelectIdChanged.selectThree();
                break;
            case 3:
                if(mOnSelectIdChanged !=null) mOnSelectIdChanged.selectFour();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnSelectIdChanged {
        void selectOne();
        void selectTwo();
        void selectThree();
        void selectFour();
    }


}
