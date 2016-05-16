package com.hnpolice.xiaoke.qqloadingwaterdrop.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * create by luoxiaoke on 2016/5/13 15:02.
 * use for QQ健康 水滴加载view
 */
public class WaterDropLoadingView extends View {

    //画笔
    private Paint mPaint;

    private Path mPath, trianglePath1, trianglePath2;
    //屏幕宽高 密度dpi
    private float sWidth, sHeight, sDensityDpi;
    //下面圆的半径 和 进度三角形边长
    private float mRaduis, triangleR;
    //进度弧形的其实角度和旋转角度
    private float startAngle, sweepAngle;
    //圆的圆点坐标
    private float pointerX, pointerY;
    //上部分的三角形三个顶点坐标  1 顶  2 左 3 右
    private float mTriangleX1, mTriangleX2, mTriangleX3, mTriangleY1, mTriangleY2, mTriangleY3;
    //控制三角形进度的高度值
    private float triangleHeight;
    //进度弧形的外切矩形
    private RectF progressRectF;

    public WaterDropLoadingView(Context context) {
        this(context, null);
    }

    public WaterDropLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterDropLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        //设备宽高和dpi密度
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        sWidth = displayMetrics.widthPixels;
        sHeight = displayMetrics.heightPixels;
        //320为我的测试机dpi密度，以次绘制视图
        sDensityDpi = displayMetrics.densityDpi / 320;

        //圆心坐标赋值
        pointerX = pointerY = Math.min(sWidth, sHeight) / 2;
        //半径和三角形边长赋值
        mRaduis = pointerX / 5;
        //赋值顶点坐标
        mTriangleX1 = pointerX;
        mTriangleY1 = (float) (pointerY - 1.5 * mRaduis * Math.sin(Math.PI / 3));
        mTriangleX2 = (float) (pointerX - mRaduis * Math.cos(Math.PI / 3));
        mTriangleX3 = (float) (pointerX + mRaduis * Math.cos(Math.PI / 3));
        mTriangleY2 = mTriangleY3 = (float) (pointerY - mRaduis * Math.sin(Math.PI / 3));

        //初始化画笔 去锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //初始化
        mPath = new Path();
        trianglePath1 = new Path();
        trianglePath2 = new Path();
        // 初始化速度范围的2个扇形外切矩形
        progressRectF = new RectF(pointerX - mRaduis + 8 * sDensityDpi, pointerY - mRaduis + 8 * sDensityDpi,
                pointerX + mRaduis - 8 * sDensityDpi, pointerY + mRaduis - 8 * sDensityDpi);
        //初始化起始角度
        startAngle = 90;

        //计算进度三角形边长
//        triangleR = (float) (8 * Math.sqrt(3) / 3);
        triangleR = mRaduis - 8 * sDensityDpi;
        Log.e("triangleR===", triangleR + "");

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        //设置画笔颜色和样式
        mPaint.setColor(0xFFDEE0DD);
        mPaint.setStyle(Paint.Style.FILL);
        //绘制圆
        canvas.drawCircle(pointerX, pointerY, mRaduis, mPaint);
        //绘制顶部三角形
        mPath.moveTo(mTriangleX1, mTriangleY1);
        mPath.lineTo(mTriangleX2, mTriangleY2);
        mPath.lineTo(mTriangleX3, mTriangleY3);
        //lineto起点
        mPath.close();
        canvas.drawPath(mPath, mPaint);


        //修改画笔颜色
        mPaint.setColor(0xFF13B5E8);
        startAngle -= 5;
        sweepAngle += 10;

        if (sweepAngle < 310) {
            canvas.drawArc(progressRectF, startAngle, sweepAngle, false, mPaint);
        } else {
            canvas.drawArc(progressRectF, -90, 360, false, mPaint);

            trianglePath1.moveTo(mTriangleX1, (float) (mTriangleY1 + mRaduis * Math.sin(Math.PI / 3) /3 ));
            trianglePath1.lineTo(mTriangleX2 + 2 * (mRaduis / 2 - triangleR / 2), mTriangleY2 + mRaduis / 2 - triangleR / 2);
            trianglePath1.lineTo(mTriangleX3 - 2 * (mRaduis / 2 - triangleR / 2), mTriangleY3 + mRaduis / 2 - triangleR / 2);
            trianglePath1.close();
            canvas.drawPath(trianglePath1, mPaint);

                startAngle = 90;
                sweepAngle = 0;
                triangleHeight = 0;
        }
        //100毫秒后重绘
        postInvalidateDelayed(100);
    }
}
