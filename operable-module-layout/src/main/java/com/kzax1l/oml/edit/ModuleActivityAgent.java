package com.kzax1l.oml.edit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
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
import android.widget.TextView;

import com.kzax1l.oml.OMLInitializer;
import com.kzax1l.oml.R;
import com.kzax1l.oml.adapter.CheckedAdapter;
import com.kzax1l.oml.adapter.UncheckedAdapter;
import com.kzax1l.oml.dao.ModuleItem;
import com.kzax1l.oml.view.CheckedGridView;
import com.kzax1l.oml.view.UncheckedGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-01-11.
 *
 * @author kzaxil
 * @since 1.0.1
 */
public class ModuleActivityAgent implements AdapterView.OnItemClickListener {
    private Activity mActivity;
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
    private List<ModuleItem> mCheckedModules = new ArrayList<>();

    private UncheckedAdapter mUncheckedAdapter; // 适配器
    private UncheckedGridView mUncheckedGridView; // GridView
    private List<ModuleItem> mUncheckedModules = new ArrayList<>(); // 数据源

    /**
     * 是否在移动，由于是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
     */
    private boolean mIsMove = false;

    public final static int OML_CODE_REQUEST = 0x98; // 请求码
    public final static int OML_CODE_RESULT = 0x99; // 返回码

    public ModuleActivityAgent(@NonNull Activity activity,
                               @NonNull CheckedGridView checked,
                               @NonNull UncheckedGridView unchecked) {
        mActivity = activity;
        mCheckedGridView = checked;
        mUncheckedGridView = unchecked;
    }

    public void onCreate() {
        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(mActivity.getApplicationContext(), new BackGestureListener(mActivity));
        }

        mCheckedModules = OMLInitializer.available();
        mUncheckedModules = OMLInitializer.unavailable();
        mCheckedAdapter = new CheckedAdapter(mActivity, mCheckedModules);
        mCheckedGridView.setAdapter(mCheckedAdapter);
        mUncheckedAdapter = new UncheckedAdapter(mActivity, mUncheckedModules);
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

    public boolean isDataSetChanged() {
        return mCheckedAdapter != null && mCheckedAdapter.isDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
        //如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (mIsMove) {
            return;
        }
        if (parent.getId() == R.id.userGridView) {
            final ModuleItem item = ((CheckedAdapter) parent.getAdapter()).getItem(position);
            if (!item.deletable) return;
            final ImageView moveImageView = getView(view);
            if (moveImageView != null) {
                TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                final int[] startLocation = new int[2];
                newTextView.getLocationInWindow(startLocation);
                mUncheckedAdapter.setVisible(false);
                //添加到最后一个
                mUncheckedAdapter.addItem(item);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        try {
                            int[] endLocation = new int[2];
                            //获取终点的坐标
                            mUncheckedGridView.getChildAt(mUncheckedGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                            MoveAnim(moveImageView, startLocation, endLocation, mCheckedGridView);
                            mCheckedAdapter.setRemove(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 50L);
            }
        } else if (parent.getId() == R.id.otherGridView) {
            // 其它GridView
            final ImageView moveImageView = getView(view);
            if (moveImageView != null) {
                TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                final int[] startLocation = new int[2];
                newTextView.getLocationInWindow(startLocation);
                final ModuleItem item = ((UncheckedAdapter) parent.getAdapter()).getItem(position);
                mCheckedAdapter.setVisible(false);
                //添加到最后一个
                mCheckedAdapter.addItem(item);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        try {
                            int[] endLocation = new int[2];
                            //获取终点的坐标
                            mCheckedGridView.getChildAt(mCheckedGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                            MoveAnim(moveImageView, startLocation, endLocation, mUncheckedGridView);
                            mUncheckedAdapter.setRemove(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 50L);
            }
        }
    }

    /**
     * 点击ITEM移动动画
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                mIsMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof CheckedGridView) {
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
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) mActivity.getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(mActivity);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(view.getContext());
        iv.setImageBitmap(cache);
        return iv;
    }

    /**
     * 退出时候保存选择后数据库的设置
     */
    private void saveModules() {
        OMLInitializer.manager().deleteAllModules();
        OMLInitializer.manager().saveCheckedModules(mCheckedAdapter.getModules());
        OMLInitializer.manager().saveUncheckedModules(mUncheckedAdapter.getModules());
    }

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
