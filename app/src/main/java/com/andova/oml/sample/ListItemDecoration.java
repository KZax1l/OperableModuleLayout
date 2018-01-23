package com.andova.oml.sample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Zsago on 2017/3/8.
 *
 * @since 2.0.1
 */
public class ListItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable divider;
    private int dividerHeight;

    public ListItemDecoration(Context context) {
        TypedArray ta = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
        divider = ta.getDrawable(0);
        ta.recycle();
        dividerHeight = 2;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        boolean signed = false;
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (!signed && child instanceof ViewGroup) {
                ViewGroup item = (ViewGroup) child;
                for (int j = 0; j < item.getChildCount(); j++) {
                    if (item.getChildAt(j) instanceof ImageView) {
                        ImageView imageView = (ImageView) item.getChildAt(j);
                        left += imageView.getRight();
                        signed = true;
                    }
                }
            }
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.topMargin;
            int bottom = top + dividerHeight;
            divider.setBounds(left, top, right - child.getPaddingRight(), bottom);
            divider.draw(c);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}