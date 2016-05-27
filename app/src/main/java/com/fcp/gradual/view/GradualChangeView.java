package com.fcp.gradual.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.fcp.gradual.R;


/**
 * 渐变各模块
 * Created by fcp on 2015/9/22.
 */
public class GradualChangeView extends View {
    //选中图片
    private Bitmap bitmapSelect;
    //未选中图片
    private Bitmap bitmapUnselect;
    //限制绘制icon的范围
    private Rect mIconRect  = new Rect();
    //底部文字
    private String mText;
    //文字转变色
    private int mColor = 0x45C01A;
    private int sColor = 0x555555;
    //文本大小
    private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
    //文本区域
    private Rect mTextBound = new Rect();
    //画笔
    private Paint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private Paint bitmapPaint = new Paint();

    /**
     * 透明度 0.0-1.0
     */
    private float mAlpha = 0f;


    public GradualChangeView(Context context) {
        super(context);
    }

    public GradualChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public GradualChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    /**
     * 初始化布局
     * @param context Context
     * @param attrs AttributeSet
     */
    private void initView(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GradualChangeBar);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.GradualChangeBar_grad_icon_top_selected:
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) typedArray.getDrawable(attr);
                    if(bitmapDrawable != null) bitmapSelect = bitmapDrawable.getBitmap();
                    break;
                case R.styleable.GradualChangeBar_grad_icon_top_unselect:
                    BitmapDrawable bitmapDrawable1 = (BitmapDrawable) typedArray.getDrawable(attr);
                    if(bitmapDrawable1 != null) bitmapUnselect = bitmapDrawable1.getBitmap();
                    break;
                case R.styleable.GradualChangeBar_grad_text_bottom:
                    mText = typedArray.getString(attr);
                    break;
                case R.styleable.GradualChangeBar_grad_text_size:
                    mTextSize = (int) typedArray.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                            10, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.GradualChangeBar_grad_text_color:
                    mColor = typedArray.getColor(attr , 0x45C01A);
                    break;
            }
        }
        typedArray.recycle();

        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(sColor);//初始颜色
        // 得到text绘制范围
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 得到绘制icon的宽
        //int bitmapWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int bitmapHeight = (int)((getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - mTextBound.height()) *0.8);//乘0.8只是让图片更小，整体更居中
        int bitmapWidth = bitmapHeight * bitmapSelect.getWidth() / bitmapSelect.getHeight() ;
        int left = ( getMeasuredWidth() - bitmapWidth ) / 2;
        int top =( getMeasuredHeight() - mTextBound.height() - bitmapHeight) / 2;
        // 设置icon的绘制范围
        mIconRect.set(left, top, left + bitmapWidth, top + bitmapHeight);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int alpha = (int) Math.ceil((255 * mAlpha));
        //if(alpha!=255){
            //绘制未选中图标
            bitmapPaint.setAlpha(255 - alpha);
            canvas.drawBitmap(bitmapUnselect, null, mIconRect, bitmapPaint);
            //绘制未选中文本
            mTextPaint.setColor(sColor);
            mTextPaint.setAlpha(255 - alpha);
            canvas.drawText(mText, mIconRect.left + (mIconRect.width() - mTextBound.width()) / 2,
                    mIconRect.bottom +  mTextBound.height() , mTextPaint);
        //}

        //if(alpha!=0){
            //绘制选中图标
            bitmapPaint.setAlpha(alpha);
            canvas.drawBitmap(bitmapSelect, null, mIconRect, bitmapPaint);
            //绘制选中文本
            mTextPaint.setColor(mColor);
            mTextPaint.setAlpha( alpha);
            canvas.drawText(mText, mIconRect.left + (mIconRect.width() - mTextBound.width()) / 2,
                    mIconRect.bottom  +  mTextBound.height() , mTextPaint);
        //}

    }


    public void setIconAlpha(float alpha) {
        this.mAlpha = alpha;
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    private static final String INSTANCE_STATE = "instance_state";
    private static final String STATE_ALPHA = "state_alpha";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(STATE_ALPHA, mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mAlpha = bundle.getFloat(STATE_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }


}
