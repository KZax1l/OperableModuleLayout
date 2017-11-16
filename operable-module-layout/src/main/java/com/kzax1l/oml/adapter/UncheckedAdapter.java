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

public class UncheckedAdapter extends BaseAdapter {

    private Context context;
    public List<ModuleItem> moduleList;
    private TextView item_text;
    /**
     * 是否可见
     */
    boolean isVisible = true;
    /**
     * 要删除的position
     */
    public int remove_position = -1;

    public UncheckedAdapter(Context context, List<ModuleItem> moduleList) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.module_item, null);
        item_text = (TextView) view.findViewById(R.id.text_item);
        ModuleItem channel = getItem(position);
        item_text.setText(channel.name);
        if (!isVisible && (position == -1 + moduleList.size())) {
            item_text.setText("");
        }
        if (remove_position == position) {
            item_text.setText("");
        }
        return view;
    }

    /**
     * 获取频道列表
     */
    public List<ModuleItem> getChannnelLst() {
        return moduleList;
    }

    /**
     * 添加频道列表
     */
    public void addItem(ModuleItem channel) {
        moduleList.add(channel);
        notifyDataSetChanged();
    }

    /**
     * 设置删除的position
     */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
        // notifyDataSetChanged();
    }

    /**
     * 删除频道列表
     */
    public void remove() {
        moduleList.remove(remove_position);
        remove_position = -1;
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
     * 设置是否可见
     */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

}