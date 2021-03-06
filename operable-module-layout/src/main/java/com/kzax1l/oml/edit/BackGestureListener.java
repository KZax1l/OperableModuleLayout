package com.kzax1l.oml.edit;

import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

/**
 * 返回手势监听接口
 */
public class BackGestureListener implements OnGestureListener {

    private GestureDetectorActivity mActivity;

    public BackGestureListener(GestureDetectorActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if ((e2.getX() - e1.getX()) > 100 && Math.abs(e1.getY() - e2.getY()) < 60) {
            mActivity.onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

}
