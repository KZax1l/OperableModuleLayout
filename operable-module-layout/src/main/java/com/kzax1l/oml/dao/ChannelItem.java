package com.kzax1l.oml.dao;

import java.io.Serializable;

/**
 * tab的对应可序化队列属性
 */
public class ChannelItem implements Serializable {
    private static final long serialVersionUID = -6465237897027410019L;
    /**
     * 栏目对应ID
     */
    public int id;
    /**
     * 栏目对应name
     */
    public String name;
    /**
     * 栏目在整体中的排序顺序  rank
     */
    public int orderId;
    /**
     * 栏目是否选中
     */
    public int selected;
    /**
     * 是否是可删除的项
     */
    public boolean deletable;

    public ChannelItem() {
    }

    public ChannelItem(int id, String name, int orderId, int selected) {
        this(id, name, orderId, selected, true);
    }

    public ChannelItem(int id, String name, int orderId, int selected, boolean deletable) {
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.selected = selected;
        this.deletable = deletable;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
}
