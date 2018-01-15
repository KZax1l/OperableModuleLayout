package com.kzax1l.oml.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kzax1l.oml.R;
import com.kzax1l.oml.Utils;
import com.kzax1l.oml.adapter.CheckedAdapter;

import java.lang.reflect.Field;

public class CheckedGridView extends GridView implements AdapterView.OnItemLongClickListener {
    /**
     * 点击坐标相对其所在控件的X坐标值
     */
    private int mViewX;
    /**
     * 点击坐标相对其所在控件的Y坐标值
     */
    private int mViewY;
    /**
     * 点击坐标相对其所在控件父容器的X坐标值
     */
    private int mViewGroupX;
    /**
     * 点击坐标相对其所在控件父容器的Y坐标值
     */
    private int mViewGroupY;

    /**
     * ITEM宽度值
     */
    private int mItemWidth;
    /**
     * ITEM高度值
     */
    private int mItemHeight;
    /**
     * 拖动项对应的position
     */
    private int mDragPosition;
    /**
     * 拖动手离开屏幕后对应项的position
     */
    private int mDropPosition;
    /**
     * 开始拖动的ITEM的position
     */
    private int mStartPosition;
    /**
     * 拖动的时候对应ITEM的VIEW
     */
    private View mDragImageView;
    /**
     * WindowManager管理器
     */
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    /**
     * 是否正在移动
     */
    private boolean mIsMoving = false;
    /**
     * 拖动的时候放大的倍数
     */
    private double mDragScale = 1.2D;
    /**
     * 震动器
     */
    private Vibrator mVibrator;
    /**
     * 移动时候最后个动画的ID
     */
    private String mLastAnimationID;

    private Bitmap mDragBitmap;
    private MotionEvent mMotionEvent;

    public CheckedGridView(Context context) {
        super(context);
        init(context);
    }

    public CheckedGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CheckedGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mWindowParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        setOnItemLongClickListener(this);
    }

    private int horizontalSpacing(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            try {
                Field field = getClass().getDeclaredField("mHorizontalSpacing");
                field.setAccessible(true);
                return Utils.dip2px(context, field.getInt(this));
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return 0;
            }
        } else {
            return getHorizontalSpacing();
        }
    }

    private int verticalSpacing(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            try {
                Field field = getClass().getDeclaredField("mVerticalSpacing");
                field.setAccessible(true);
                return Utils.dip2px(context, field.getInt(this));
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return 0;
            }
        } else {
            return getVerticalSpacing();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mMotionEvent = ev;
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mViewGroupX = (int) ev.getX();
            mViewGroupY = (int) ev.getY();
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (mDragImageView == null || mDragPosition == AdapterView.INVALID_POSITION)
            return super.onTouchEvent(ev);
        // 移动时候的对应x,y位置
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mViewGroupX = (int) ev.getX();
                mViewGroupY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                startDrag((int) ev.getRawX(), (int) ev.getRawY());
                if (!mIsMoving) startMove(x, y);
                if (pointToPosition(x, y) != AdapterView.INVALID_POSITION) {
                    break;
                }
                break;
            case MotionEvent.ACTION_UP:
                stopDrag();
                onDrop(x, y);
                requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 在拖动的情况
     */
    private void startDrag(int rawX, int rawY) {
        if (mDragImageView == null) return;
        mWindowParams.alpha = 0.6f;
        mWindowParams.x = rawX - mViewX;
        mWindowParams.y = rawY - mViewY;
        mWindowManager.updateViewLayout(mDragImageView, mWindowParams);
    }

    /**
     * 在松手下放的情况
     */
    private void onDrop(int x, int y) {
        // 根据拖动到的x,y坐标获取拖动位置下方的ITEM对应的POSITION
        mDropPosition = pointToPosition(x, y);
        CheckedAdapter mDragAdapter = (CheckedAdapter) getAdapter();
        // 显示刚拖动的ITEM
        mDragAdapter.setShowDropItem(true);
        // 刷新适配器，让对应的ITEM显示
        mDragAdapter.notifyDataSetChanged();
        if (mDragBitmap != null && !mDragBitmap.isRecycled()) mDragBitmap.recycle();
    }

    private void readyDrag(Bitmap dragBitmap, int rawX, int rawY) {
        stopDrag();
        // 这个必须加
        mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;
        //得到preview左上角相对于屏幕的坐标
        mWindowParams.x = rawX - mViewX;
        mWindowParams.y = rawY - mViewY;
        //设置拖拽item的宽和高
        mWindowParams.width = (int) (mDragScale * dragBitmap.getWidth());// 放大dragScale倍，可以设置拖动后的倍数
        mWindowParams.height = (int) (mDragScale * dragBitmap.getHeight());// 放大dragScale倍，可以设置拖动后的倍数
        this.mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        this.mWindowParams.format = PixelFormat.TRANSLUCENT;
        this.mWindowParams.windowAnimations = 0;
        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(dragBitmap);
        mWindowManager.addView(iv, mWindowParams);
        mDragImageView = iv;
    }

    /**
     * 停止拖动 ，释放并初始化
     */
    private void stopDrag() {
        if (mDragImageView == null) return;
        mWindowManager.removeView(mDragImageView);
        mDragImageView = null;
    }

    /**
     * 在ScrollView内，所以要进行计算高度
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 隐藏 放下 的ITEM
     */
    private void hideDropItem() {
        ((CheckedAdapter) getAdapter()).setShowDropItem(false);
    }

    /**
     * 获取移动动画
     */
    private Animation getMoveAnimation(float toXValue, float toYValue) {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, toXValue,
                Animation.RELATIVE_TO_SELF, 0.0F,
                Animation.RELATIVE_TO_SELF, toYValue);// 当前位置移动到指定位置
        animation.setFillAfter(true);// 设置一个动画效果执行完毕后，View对象保留在终止的位置。
        animation.setDuration(300L);
        return animation;
    }

    /**
     * 移动的时候触发
     */
    private void startMove(int x, int y) {
        // 拖动的VIEW下方的POSITION
        int downPos = pointToPosition(x, y);
        // 判断是从第几个索引开始可以拖拽移动
        if (downPos < 0) return;
        if (downPos == mDragPosition) return;
        mDropPosition = downPos;
        if (mDragPosition != mStartPosition) {
            mDragPosition = mStartPosition;
        }
        int moveCount = mDragPosition != mDropPosition ? mDropPosition - mDragPosition : 0;
        if (moveCount == 0) return;

        int moveCountAbs = Math.abs(moveCount);
        ViewGroup dragGroup = (ViewGroup) getChildAt(mDragPosition);
        dragGroup.setVisibility(View.INVISIBLE);

        float toX;// 当前下方position
        float toY;// 当前下方右边position
        // xValue移动的距离百分比（相对于自己长度的百分比）
        float xValue = ((float) horizontalSpacing(getContext()) / (float) mItemWidth) + 1.0f;
        // yValue移动的距离百分比（相对于自己宽度的百分比）
        float yValue = ((float) verticalSpacing(getContext()) / (float) mItemHeight) + 1.0f;
        for (int i = 0; i < moveCountAbs; i++) {
            int holdPosition;
            int column = getNumColumns();
            if (moveCount > 0) {
                // 判断是不是同一行的
                holdPosition = mDragPosition + i + 1;
                if (mDragPosition / column == holdPosition / column) {
                    toX = -xValue;
                    toY = 0;
                } else if (holdPosition % column == 0) {
                    toX = (column - 1) * xValue;
                    toY = -yValue;
                } else {
                    toX = -xValue;
                    toY = 0;
                }
            } else {
                //向右,下移到上，右移到左
                holdPosition = mDragPosition - i - 1;
                if (mDragPosition / column == holdPosition / column) {
                    toX = xValue;
                    toY = 0;
                } else if ((holdPosition + 1) % column == 0) {
                    toX = -(column - 1) * xValue;
                    toY = yValue;
                } else {
                    toX = xValue;
                    toY = 0;
                }
            }
            ViewGroup moveViewGroup = (ViewGroup) getChildAt(holdPosition);
            Animation moveAnimation = getMoveAnimation(toX, toY);
            moveViewGroup.startAnimation(moveAnimation);
            //如果是最后一个移动的，那么设置他的最后个动画ID为LastAnimationID
            if (holdPosition == mDropPosition) {
                mLastAnimationID = moveAnimation.toString();
            }
            moveAnimation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mIsMoving = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // 如果为最后个动画结束，那执行下面的方法
                    if (animation.toString().equalsIgnoreCase(mLastAnimationID)) {
                        CheckedAdapter mDragAdapter = (CheckedAdapter) getAdapter();
                        mDragAdapter.exchange(mStartPosition, mDropPosition);
                        mStartPosition = mDropPosition;
                        mDragPosition = mDropPosition;
                        mIsMoving = false;
                    }
                }
            });
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mMotionEvent == null) return false;
        mStartPosition = position;// 第一次点击的position
        mDragPosition = position;
        if (mStartPosition < 0) return false;
        ViewGroup dragViewGroup = (ViewGroup) getChildAt(mDragPosition - getFirstVisiblePosition());
        TextView dragTextView = (TextView) dragViewGroup.findViewById(R.id.text_item);
        dragTextView.setEnabled(false);
        dragTextView.setSelected(true);
        mItemWidth = dragViewGroup.getWidth();
        mItemHeight = dragViewGroup.getHeight();

        if (mDragPosition == AdapterView.INVALID_POSITION) return false;
        mViewX = mViewGroupX - dragViewGroup.getLeft();
        mViewY = mViewGroupY - dragViewGroup.getTop();
        dragViewGroup.destroyDrawingCache();
        dragViewGroup.setDrawingCacheEnabled(true);
        if (mDragBitmap != null && !mDragBitmap.isRecycled()) mDragBitmap.recycle();
        mDragBitmap = Bitmap.createBitmap(dragViewGroup.getDrawingCache());
        mVibrator.vibrate(50);// 设置震动时间
        readyDrag(mDragBitmap, (int) mMotionEvent.getRawX(), (int) mMotionEvent.getRawY());
        hideDropItem();
        dragViewGroup.setVisibility(View.INVISIBLE);
        mIsMoving = false;
        requestDisallowInterceptTouchEvent(true);
        return true;
    }
}