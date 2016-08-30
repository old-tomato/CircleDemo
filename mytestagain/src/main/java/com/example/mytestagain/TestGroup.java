package com.example.mytestagain;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/8/30 0030.
 */
public class TestGroup extends ViewGroup implements View.OnTouchListener {

    private Context mContext;
    private ImageView mIv;
    private Paint mCirclePaint;
    private int mWidth;
    private int mHeight;
    private int mHeartY;
    private int mHeartX;
    private int mRadius;

    public TestGroup(Context context) {
        this(context , null);
    }

    public TestGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mContext = context;
        init();
    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setStrokeWidth(10);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d("zkl" , "onLayout ==> " + l + "   " + t + "   " + r + "   " + b + "   ");

        mIv = (ImageView) getChildAt(0);

        // 将图案放置到圆环的顶部
        Log.d("zkl" , "width ==> " + mIv.getMeasuredWidth());

        // 设定的图片的宽度是30*30
        mIv.layout(235 , 85 , 265 , 115);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("zkl" , "onMeasure");
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("zkl" , "onDraw");
        mHeartX = mWidth / 2;
        mHeartY = mHeight / 2;
        //获得当前的半径
        mRadius = 150;
        canvas.drawCircle(mHeartX , mHeartY , mRadius , mCirclePaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 获得当前的触控的位置
        float x = event.getX();
        float y = event.getY();
        Log.d("zkl" , "x ==> " + x + "    y ==> " + y);

        // 获得到当前的位置，计算距离当前最近的圆上的点的位置
        double[] ivPoint = getPointPosition(x , y);
        double resultX = ivPoint[0];
        double resultY = ivPoint[1];

        // 计算图片的layout
        int l = (int) (resultX - 15);
        int t = (int) (resultY - 15);
        int r = (int) (resultX + 15);
        int b = (int) (resultY + 15);
        mIv.layout(l , t , r , b);
        invalidate();
        return true;
    }

    /**
     * 计算距离当前最近的圆上的点的位置
     * @param x
     * @param y
     * @return
     */
    private double[] getPointPosition(double x, double y) {

        //获得圆心
        double heartX = mHeartX;
        double heartY = mHeartY;
        double radius = mRadius;

        // 这里计算的结果需要是一个绝对值
        double lX = x - heartX;
        double lY = y - heartY;
        Log.d("zkl" , "lx ==> " + lX + "   lY ==> " + lY);

        double alX = Math.abs(x - heartX);
        double alY = Math.abs(y - heartY);

        // 获得当前的手指所触控的位置距离圆心的距离
        double length = Math.sqrt(alX * alX + alY * alY);
        Log.d("zkl" , "length ==> " + length);

        // 通过相似三角形原理获得当前的在圆上的最近的点的位置
        // 这里获得是大三角型是小三角形的几倍
        double v = radius / length;
        Log.d("zkl" , "v ==> " + v);

        // 放大当前的点的位置
        double xx = alX * v;
        double yy = alY * v;

        double resultX = 0;
        double resultY = 0;
        if(lX > 0 && lY < 0){
            // 这是第一象限的处理
            resultX = x + (xx - alX);
            resultY = y - (yy - alY);
        }
        if(lX > 0 && lY > 0){
            // 这是第二象限的处理
            resultX = x + (xx - alX);
            resultY = y + (yy - alY);
        }
        if(lX < 0 && lY > 0){
            // 这是第三象限的处理
            resultX = x - (xx - alX);
            resultY = y + (yy - alY);
        }
        if(lX < 0 && lY < 0){
            // 这是第四象限的处理
            resultX = x - (xx - alX);
            resultY = y - (yy - alY);
        }

        Log.d("zkl" , "result x ==> " + resultX + "    y ==> " + resultY);
        return new double[]{resultX , resultY};
    }
}
