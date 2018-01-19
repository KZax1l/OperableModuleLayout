package com.kzax1l.oml.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kzax1l.oml.OMLInitializer;
import com.kzax1l.oml.dao.ModuleItem;

import java.util.List;

public class UncheckedAdapter extends BaseAdapter {
    private Context mContext;
    private List<ModuleItem> mModules;
    /**
     * 是否可见
     */
    private boolean mIsVisible = true;
    /**
     * 要删除的position
     */
    private int mRemovePosition = -1;

    public UncheckedAdapter(Context context, List<ModuleItem> modules) {
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
        if (!OMLInitializer.getModuleOptions().moduleAdapter(view, item)) fill(view, item);
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

    /**
     * 获取频道列表
     */
    public List<ModuleItem> getModules() {
        return mModules;
    }

    /**
     * 添加频道列表
     */
    public void addItem(ModuleItem module) {
        mModules.add(module);
        notifyDataSetChanged();
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
     * 设置是否可见
     */
    public void setVisible(boolean visible) {
        mIsVisible = visible;
    }
}