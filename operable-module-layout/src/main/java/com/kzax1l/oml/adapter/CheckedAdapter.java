package com.kzax1l.oml.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kzax1l.oml.R;
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
    /**
     * TextView 频道内容
     */
    private TextView item_text;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.module_item, parent, false);
        item_text = (TextView) view.findViewById(R.id.text_item);
        ModuleItem item = getItem(position);
        item_text.setText(item.name);
        if (!item.deletable) {
            item_text.setEnabled(false);
        }
        if (mIsChanged && (position == mHoldPosition) && !mIsItemShow) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
            mIsChanged = false;
        }
        if (!mIsVisible && (position == -1 + mModules.size())) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
        }
        if (mRemovePosition == position) {
            item_text.setText("");
        }
        return view;
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
    public boolean IsDataSetChanged() {
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