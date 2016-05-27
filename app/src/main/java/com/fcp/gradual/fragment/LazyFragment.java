package com.fcp.gradual.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fcp.gradual.R;

/**
 * 懒加载fragment（使用说明：onCreateView方法进行控件的初始化；在lazyLoad中进行数据的加载）
 * Created by fcp on 2016/5/27.
 */
public abstract class LazyFragment extends BaseFragment{

    //进度条布局ID
    static final int INTERNAL_PROGRESS_CONTAINER_ID = R.id.fragmentLoadProgress;
    //显示布局ID
    static final int INTERNAL_SHOW_CONTAINER_ID = R.id.fragemntLoadShowLayout;
    //初始化数据完成
    private boolean isDataPrepare = false;
    //控件生成完成
    private boolean isCreated = false;
    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser boolean
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //Log.d("setUserVisibleHint",this.getClass().getSimpleName()+"->setUserVisibleHint:"+isVisibleToUser);
        if(isVisibleToUser) {
            if(!isDataPrepare &&isCreated) {
                lazyLoad();
            }
            //前台显示
            if(isCreated){
                onShowForeground();
            }
        }else if(isCreated){
            onHideForeground();
        }
    }

    /**
     * 数据加载
     */
    public abstract void lazyLoad();

    /**
     * 当前台显示
     */
    protected void onShowForeground(){}

    /**
     * 当前台隐藏
     */
    protected void onHideForeground(){}


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Context context = getActivity();
        //外布局
        FrameLayout root = new FrameLayout(context);

        //---------------------------------------------进度条
        LinearLayout pframe = new LinearLayout(context);
        pframe.setId(INTERNAL_PROGRESS_CONTAINER_ID);
        pframe.setOrientation(LinearLayout.VERTICAL);
        pframe.setVisibility(View.VISIBLE);
        pframe.setGravity(Gravity.CENTER);

        ProgressBar progress = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        progress.setIndeterminateDrawable(getResources().getDrawable(R.drawable.rotate_view_wait_dialog));
        pframe.addView(progress, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        root.addView(pframe, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //--------------------------------------------子类布局
        View view = setContentView(inflater, container, savedInstanceState);
        if(view != null) {
            view.setId(INTERNAL_SHOW_CONTAINER_ID);
            root.addView(view , new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setVisibility(View.GONE);
        }
        isCreated = true;
        return root;
    }

    /**
     * 子类要显示的布局
     * @return View
     */
    protected abstract View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //如果没有加载数据，再加载一次（主要针对第一个界面）
        if(!isDataPrepare&&getUserVisibleHint()){
            setUserVisibleHint(true);
        }
    }

    /**
     * 加载完成，显示布局
     */
    protected void showContentView(){
        View root = getView();
        if(root != null && !isDataPrepare){
            View mProgressContainer = root.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
            if(mProgressContainer!=null) mProgressContainer.setVisibility(View.GONE);
            View showLayout = root.findViewById(INTERNAL_SHOW_CONTAINER_ID);
            if(showLayout!=null)showLayout.setVisibility(View.VISIBLE);
            isDataPrepare = true ;
        }
    }

    /**
     * 重现进度圈布局
     */
    protected void hideContentView(){
        View root = getView();
        if(root != null ){
            isDataPrepare = false ;
            View mProgressContainer = root.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
            if(mProgressContainer!=null) mProgressContainer.setVisibility(View.VISIBLE);
            View showLayout = root.findViewById(INTERNAL_SHOW_CONTAINER_ID);
            if(showLayout!=null)showLayout.setVisibility(View.GONE);
        }
    }

    public boolean isDataPrepare() {
        return isDataPrepare;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isDataPrepare", isDataPrepare);
        super.onSaveInstanceState(outState);
    }


}
