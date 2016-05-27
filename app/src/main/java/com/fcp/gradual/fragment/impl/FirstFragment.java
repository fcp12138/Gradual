package com.fcp.gradual.fragment.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fcp.gradual.R;
import com.fcp.gradual.fragment.LazyFragment;

/**
 * 第一页
 * Created by fcp on 2016/5/27.
 */
public class FirstFragment extends LazyFragment {

    @Override
    public void lazyLoad() {
        showContentView();
    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first,container,false);
    }
}
