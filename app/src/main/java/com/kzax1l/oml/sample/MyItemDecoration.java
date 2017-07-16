package com.kzax1l.oml.sample;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;

    MyItemDecoration() {
        mPaint = new Paint();
    }

    /**
     * 复写onDraw方法，从而达到在每隔条目的被绘制之前（或之后），让他先帮我们画上去几根线吧
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //先初始化一个Paint来简单指定一下Canvas的颜色，就黑的吧！
        mPaint.setColor(ContextCompat.getColor(parent.getContext(), android.R.color.black));

        //获得RecyclerView中总条目数量
        int childCount = parent.getChildCount();

        //遍历一下
        for (int i = 0; i < childCount; i++) {
            //获得子View，也就是一个条目的View，准备给他画上边框
            View childView = parent.getChildAt(i);

            //先获得子View的长宽，以及在屏幕上的位置，方便我们得到边框的具体坐标
            float x = childView.getX();
            float y = childView.getY();
            int width = childView.getWidth();
            int height = childView.getHeight();

            //根据这些点画条目的四周的线
//            c.drawLine(x, y, x + width, y, mPaint);
            //如果是每行的第一个条目，那么我们就不画边框了
            if (!isFirstItem(parent, i)) {
                c.drawLine(x, y + height / 4, x, y + height / 4 * 3, mPaint);
            }
//            c.drawLine(x + width, y, x + width, y + height, mPaint);
            c.drawLine(x, y + height, x + width, y + height, mPaint);

            //当然了，这里大家肯定是要根据自己不同的设计稿进行画线，或者画一些其他的东西，都可以在这里搞，非常方便
        }
        super.onDraw(c, parent, state);
    }

    /**
     * 判断是否是每行第一个项
     */
    private boolean isFirstItem(RecyclerView parent, int position) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            int spanCount = layoutManager.getSpanCount();
            return position % spanCount == 0;
        }
        return true;
    }
}