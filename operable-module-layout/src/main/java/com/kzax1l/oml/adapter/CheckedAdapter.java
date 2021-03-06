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
    /**
     * 是否显示底部的ITEM
     */
    private boolean isItemShow = false;
    private Context context;
    /**
     * 控制的postion
     */
    private int holdPosition;
    /**
     * 是否改变
     */
    private boolean isChanged = false;
    /**
     * 列表数据是否改变
     */
    private boolean isListChanged = false;
    /**
     * 是否可见
     */
    boolean isVisible = true;
    /**
     * 可以拖动的列表（即用户选择的频道列表）
     */
    public List<ModuleItem> moduleList;
    /**
     * TextView 频道内容
     */
    private TextView item_text;
    /**
     * 要删除的position
     */
    public int remove_position = -1;

    public CheckedAdapter(Context context, List<ModuleItem> moduleList) {
        this.context = context;
        this.moduleList = moduleList;
    }

    @Override
    public int getCount() {
        return moduleList == null ? 0 : moduleList.size();
    }

    @Override
    public ModuleItem getItem(int position) {
        if (moduleList != null && moduleList.size() != 0) {
            return moduleList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.module_item, parent, false);
        item_text = (TextView) view.findViewById(R.id.text_item);
        ModuleItem channel = getItem(position);
        item_text.setText(channel.name);
        if (!channel.deletable) {
            item_text.setEnabled(false);
        }
        if (isChanged && (position == holdPosition) && !isItemShow) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
            isChanged = false;
        }
        if (!isVisible && (position == -1 + moduleList.size())) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
        }
        if (remove_position == position) {
            item_text.setText("");
        }
        return view;
    }

    /**
     * 添加频道列表
     */
    public void addItem(ModuleItem channel) {
        moduleList.add(channel);
        isListChanged = true;
        notifyDataSetChanged();
    }

    /**
     * 拖动变更频道排序
     */
    public void exchange(int dragPostion, int dropPostion) {
        holdPosition = dropPostion;
        ModuleItem dragItem = getItem(dragPostion);
        if (dragPostion < dropPostion) {
            moduleList.add(dropPostion + 1, dragItem);
            moduleList.remove(dragPostion);
        } else {
            moduleList.add(dropPostion, dragItem);
            moduleList.remove(dragPostion + 1);
        }
        isChanged = true;
        isListChanged = true;
        notifyDataSetChanged();
    }

    /**
     * 获取频道列表
     */
    public List<ModuleItem> getChannnelLst() {
        return moduleList;
    }

    /**
     * 设置删除的position
     */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
    }

    /**
     * 删除频道列表
     */
    public void remove() {
        moduleList.remove(remove_position);
        remove_position = -1;
        isListChanged = true;
        notifyDataSetChanged();
    }

    /**
     * 设置频道列表
     */
    public void setListDate(List<ModuleItem> list) {
        moduleList = list;
    }

    /**
     * 获取是否可见
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * 排序是否发生改变
     */
    public boolean isListChanged() {
        return isListChanged;
    }

    /**
     * 设置是否可见
     */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    /**
     * 显示放下的ITEM
     */
    public void setShowDropItem(boolean show) {
        isItemShow = show;
    }
}