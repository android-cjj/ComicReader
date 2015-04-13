package com.capricorn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
/**
 * 自定义十字架
 * @author cjj
 *
 */
public class CrossView extends View{
	private Paint mPaint;
	private int mRotation;
	private static final int DEFAULT_SIZE = 100;//默认的试图尺寸
	float[] mPoints = {0.5f,0f,0.5f,1f,0f,0.5f,1f,0.5f};
	public CrossView(Context context) {
		super(context);
		init();
	}

	public CrossView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(14);
		mPaint.setColor(0xffff8c00);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		   //需要使用计算好的宽和高的值作为该方法的实参
        setMeasuredDimension( calculateMeasure(widthMeasureSpec), calculateMeasure(heightMeasureSpec) );
	}
	
	 //实现计算测量值的代码
    private int calculateMeasure(int measureSpec){
        int result = ( int ) ( DEFAULT_SIZE*getResources().getDisplayMetrics().density );
        int specMode = MeasureSpec.getMode( measureSpec );//在MeasureSpec中检索模式
        int specSize = MeasureSpec.getSize( measureSpec );//在MeasureSpec中检索尺寸
        //基于模式选择尺寸
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else if(specMode == MeasureSpec.AT_MOST){
            result = Math.min( result, specSize );
        }
        return result;
    }
    
    
    @Override
    protected void onDraw( Canvas canvas ) {
        // TODO Auto-generated method stub
        super.onDraw( canvas );
        canvas.save();//所有的在画布上绘图的调用都应当受对应的sava（）和restore（）的约束
        int scale = getWidth();
        canvas.scale( scale, scale );
        canvas.rotate( mRotation );
        canvas.drawLines( mPoints, mPaint );//绘制十字的两条线
        canvas.restore();//所有的在画布上绘图的调用都应当受对应的sava（）和restore（）的约束
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	mRotation = 45;
    	postInvalidate();
    	return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	mRotation = 0;
    	postInvalidate();
    	return super.onKeyUp(keyCode, event);
    }
}
