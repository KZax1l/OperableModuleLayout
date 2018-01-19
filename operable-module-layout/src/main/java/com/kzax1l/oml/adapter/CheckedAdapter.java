package com.kzax1l.oml.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kzax1l.oml.OMLInitializer;
import com.kzax1l.oml.dao.ModuleItem;

import java.util.List;

public class CheckedAdapter extends BaseAdapter {
    private Context mContext;
    /**
     * 是否显示底部的ITEM
     */
    private boolean mIsItemShow = false;
    /**
     * 控制的position
     */
    private int mHoldPosition;
    /**
     * 是否改变
     */
    private boolean mIsChanged = false;
    /**
     * 列表数据是否改变
     */
    private boolean mIsDataSetChanged = false;
    /**
     * 是否可见
     */
    private boolean mIsVisible = true;
    /**
     * 可以拖动的列表（即用户选择的频道列表）
     */
    private List<ModuleItem> mModules;
    /**
     * 要删除的position
     */
    private int mRemovePosition = -1;

    public CheckedAdapter(Context context, List<ModuleItem> modules) {
        this.mContext = context;
        this.mModules = modules;
    }

    @Override
    public int getCount() {
        return mModules == null ? 0 : mModules.size();
    }

    @Override
    public ModuleItem getItem(int position) {
        if (mModules != null && mModules.size() != 0) {
            return mModules.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = OMLInitializer.getModuleOptions().moduleLayout(mContext, parent);
        ModuleItem item = getItem(position);
        item.setChecked(true);
        if (!OMLInitializer.getModuleOptions().moduleAdapter(view, item)) fill(view, item);
        if (!item.operable) disable(view);
        if (mIsChanged && (position == mHoldPosition) && !mIsItemShow) {
            view.setVisibility(View.GONE);
            mIsChanged = false;
        }
        if (!mIsVisible && (position == -1 + mModules.size())) view.setVisibility(View.GONE);
        if (mRemovePosition == position) view.setVisibility(View.GONE);
        return view;
    }

    private void fill(View view, ModuleItem item) {
        if (view == null) return;
        if (!(view instanceof ViewGroup)) return;
        ViewGroup vg = (ViewGroup) view;
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setText(item.name);
            }
        }
    }

    private void disable(View view) {
        if (view == null) return;
        if (!(view instanceof ViewGroup)) return;
        ViewGroup vg = (ViewGroup) view;
        for (int i = 0; i < vg.getChildCount(); i++) {
            vg.getChildAt(i).setEnabled(false);
        }
    }

    /**
     * 添加频道列表
     */
    public void addItem(ModuleItem module) {
        mModules.add(module);
        mIsDataSetChanged = true;
        notifyDataSetChanged();
    }

    /**
     * 拖动变更频道排序
     */
    public void exchange(int dragPosition, int dropPosition) {
        mHoldPosition = dropPosition;
        ModuleItem dragItem = getItem(dragPosition);
        if (dragPosition < dropPosition) {
            mModules.add(dropPosition + 1, dragItem);
            mModules.remove(dragPosition);
        } else {
            mModules.add(dropPosition, dragItem);
            mModules.remove(dragPosition + 1);
        }
        mIsChanged = true;
        mIsDataSetChanged = true;
        notifyDataSetChanged();
    }

    /**
     * 获取频道列表
     */
    public List<ModuleItem> getModules() {
        return mModules;
    }

    /**
     * 设置删除的position
     */
    public void setRemove(int position) {
        mRemovePosition = position;
        notifyDataSetChanged();
    }

    /**
     * 删除频道列表
     */
    public void remove() {
        mModules.get(mRemovePosition).setChecked(false);
        mModules.remove(mRemovePosition);
        mRemovePosition = -1;
        mIsDataSetChanged = true;
        notifyDataSetChanged();
    }

    /**
     * 设置频道列表
     */
    public void setModules(List<ModuleItem> list) {
        mModules = list;
    }

    /**
     * 获取是否可见
     */
    public boolean isVisible() {
        return mIsVisible;
    }

    /**
     * 排序是否发生改变
     */
    public boolean isDataSetChanged() {
        return mIsDataSetChanged;
    }

    /**
     * 设置是否可见
     */
    public void setVisible(boolean visible) {
        mIsVisible = visible;
    }

    /**
     * 显示放下的ITEM
     */
    public void setShowDropItem(boolean show) {
        mIsItemShow = show;
    }
}