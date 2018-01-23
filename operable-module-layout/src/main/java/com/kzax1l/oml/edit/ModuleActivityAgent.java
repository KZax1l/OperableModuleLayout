package com.kzax1l.oml.edit;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kzax1l.oml.OMLInitializer;
import com.kzax1l.oml.adapter.CheckedAdapter;
import com.kzax1l.oml.adapter.UncheckedAdapter;
import com.kzax1l.oml.dao.ModuleItem;
import com.kzax1l.oml.view.CheckedGridView;
import com.kzax1l.oml.view.UncheckedGridView;

/**
 * Created by Administrator on 2018-01-11.
 *
 * @author kzaxil
 * @since 1.0.1
 */
public class ModuleActivityAgent implements AdapterView.OnItemClickListener {
    private AppCompatActivity mActivity;
    /**
     * 是否需要监听手势关闭功能
     */
    private boolean mNeedBackGesture = false;
    /**
     * 手势监听
     */
    private GestureDetector mGestureDetector;

    private CheckedAdapter mCheckedAdapter; // 适配器
    private CheckedGridView mCheckedGridView; // GridView
    private UncheckedAdapter mUncheckedAdapter; // 适配器
    private UncheckedGridView mUncheckedGridView; // GridView

    /**
     * 是否在移动，由于是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
     */
    private boolean mIsMove = false;
    private int mClickPosition;
    private Bitmap mViewCacheBitmap;
    private GridView mClickGridView;
    private ImageView mMoveImageView;
    private LinearLayout mMoveGroupView;
    private Handler mHandler = new Handler();
    private int[] mStartLocation = new int[2];
    private int[] mEndLocation = new int[2];

    public final static int OML_CODE_REQUEST = 0x98; // 请求码
    public final static int OML_CODE_RESULT = 0x99; // 返回码

    private Runnable mMoveToCheckedRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mClickGridView = mUncheckedGridView;
                // 获取终点的坐标
                mCheckedGridView.getChildAt(mCheckedGridView.getLastVisiblePosition()).getLocationInWindow(mEndLocation);
                moveAnim();
                mUncheckedAdapter.setRemove(mClickPosition);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable mMoveToUncheckedRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mClickGridView = mCheckedGridView;
                // 获取终点的坐标
                mUncheckedGridView.getChildAt(mUncheckedGridView.getLastVisiblePosition()).getLocationInWindow(mEndLocation);
                moveAnim();
                mCheckedAdapter.setRemove(mClickPosition);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Animation.AnimationListener mAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mIsMove = true;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            removeMoveImageView();
            if (mClickGridView instanceof CheckedGridView) {
                mUncheckedAdapter.setVisible(true);
                mUncheckedAdapter.notifyDataSetChanged();
                mCheckedAdapter.remove();
            } else {
                mCheckedAdapter.setVisible(true);
                mCheckedAdapter.notifyDataSetChanged();
                mUncheckedAdapter.remove();
            }
            mIsMove = false;
        }
    };

    @SuppressWarnings("WeakerAccess")
    public ModuleActivityAgent(@NonNull AppCompatActivity activity,
                               @IdRes final int checkedGridViewId,
                               @IdRes final int uncheckedGridViewId) {
        mActivity = activity;
        try {
            LayoutInflater.Factory2 factory2 = new LayoutInflater.Factory2() {
                @Override
                public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                    if (name.equals("GridView")) {
                        for (int i = 0; i < attrs.getAttributeCount(); i++) {
                            if (!attrs.getAttributeName(i).equals("id")) continue;
                            int value = attrs.getAttributeResourceValue(i, 0);
                            if (value == checkedGridViewId) {
                                mCheckedGridView = new CheckedGridView(context, attrs);
                                return mCheckedGridView;
                            } else if (value == uncheckedGridViewId) {
                                mUncheckedGridView = new UncheckedGridView(context, attrs);
                                return mUncheckedGridView;
                            }
                        }
                    }
                    AppCompatDelegate delegate = mActivity.getDelegate();
                    return delegate.createView(parent, name, context, attrs);
                }

                @Override
                public View onCreateView(String name, Context context, AttributeSet attrs) {
                    return null;
                }
            };
            LayoutInflaterCompat.setFactory2(LayoutInflater.from(activity), factory2);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage() + ",create a ModuleActivityAgent object must before super.onCreate(savedInstanceState)!");
        }
    }

    public void onCreate() {
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(mActivity.getApplicationContext(), new BackGestureListener(mActivity));
        }

        if (mCheckedGridView == null || mUncheckedGridView == null) return;
        mCheckedAdapter = new CheckedAdapter(mActivity, OMLInitializer.available());
        mCheckedGridView.setAdapter(mCheckedAdapter);
        mUncheckedAdapter = new UncheckedAdapter(mActivity, OMLInitializer.unavailable());
        mUncheckedGridView.setAdapter(mUncheckedAdapter);
        mUncheckedGridView.setOnItemClickListener(this);
        mCheckedGridView.setOnItemClickListener(this);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mNeedBackGesture) {
            return mGestureDetector.onTouchEvent(ev) || mActivity.dispatchTouchEvent(ev);
        }
        return mActivity.dispatchTouchEvent(ev);
    }

    /**
     * 设置是否进行手势监听
     */
    public void setNeedBackGesture(boolean mNeedBackGesture) {
        this.mNeedBackGesture = mNeedBackGesture;
    }

    /**
     * 返回
     */
    public void doBack() {
        mActivity.onBackPressed();
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isDataSetChanged() {
        return mCheckedAdapter != null && mCheckedAdapter.isDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
        // 如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (mIsMove) return;
        initMoveImageViewBitmap(view);
        if (mMoveImageView == null) return;
        mClickPosition = position;
        view.getLocationInWindow(mStartLocation);
        if (parent == mCheckedGridView) {
            ModuleItem item = ((CheckedAdapter) parent.getAdapter()).getItem(position);
            if (!item.operable) return;
            mUncheckedAdapter.setVisible(false);
            // 添加到最后一个
            mUncheckedAdapter.addItem(item);
            mHandler.removeCallbacksAndMessages(null);
            mHandler.postDelayed(mMoveToUncheckedRunnable, 50L);
        } else if (parent == mUncheckedGridView) {
            ModuleItem item = ((UncheckedAdapter) parent.getAdapter()).getItem(position);
            mCheckedAdapter.setVisible(false);
            // 添加到最后一个
            mCheckedAdapter.addItem(item);
            mHandler.removeCallbacksAndMessages(null);
            mHandler.postDelayed(mMoveToCheckedRunnable, 50L);
        }
    }

    /**
     * 点击ITEM移动动画
     */
    private void moveAnim() {
        if (mMoveImageView == null) return;
        int[] initLocation = new int[2];
        mMoveImageView.getLocationInWindow(initLocation);
        addMoveImageView(initLocation);
        // 创建移动动画
        TranslateAnimation animation = new TranslateAnimation(
                mStartLocation[0], mEndLocation[0],
                mStartLocation[1], mEndLocation[1]);
        animation.setDuration(300L);// 动画时间
        // 动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(animation);
        mMoveImageView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(mAnimListener);
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     */
    private void addMoveImageView(int[] initLocation) {
        if (mMoveImageView == null) return;
        int x = initLocation[0];
        int y = initLocation[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        mMoveImageView.setLayoutParams(lp);
        getMoveViewGroup().addView(mMoveImageView);
    }

    private void removeMoveImageView() {
        getMoveViewGroup().removeView(mMoveImageView);
        if (mViewCacheBitmap != null && !mViewCacheBitmap.isRecycled()) mViewCacheBitmap.recycle();
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        if (mMoveGroupView != null) return mMoveGroupView;
        ViewGroup moveViewGroup = (ViewGroup) mActivity.getWindow().getDecorView();
        mMoveGroupView = new LinearLayout(mActivity);
        mMoveGroupView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(mMoveGroupView);
        return mMoveGroupView;
    }

    /**
     * 获取点击的Item的对应View
     */
    private void initMoveImageViewBitmap(View view) {
        if (view == null) return;
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        if (mViewCacheBitmap != null && !mViewCacheBitmap.isRecycled()) mViewCacheBitmap.recycle();
        mViewCacheBitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        if (mMoveImageView == null) mMoveImageView = new ImageView(view.getContext());
        mMoveImageView.setImageBitmap(mViewCacheBitmap);
    }

    /**
     * 退出时候保存选择后数据库的设置
     */
    private void saveModules() {
        OMLInitializer.getModuleManager().deleteAllModules();
        OMLInitializer.getModuleManager().saveCheckedModules(mCheckedAdapter.getModules());
        OMLInitializer.getModuleManager().saveUncheckedModules(mUncheckedAdapter.getModules());
    }

    @SuppressWarnings("WeakerAccess")
    public void onBackPressed() {
        if (mCheckedAdapter.isDataSetChanged()) {
            saveModules();
            mActivity.setResult(OML_CODE_RESULT);
            mActivity.finish();
        } else {
            mActivity.onBackPressed();
        }
    }
}
