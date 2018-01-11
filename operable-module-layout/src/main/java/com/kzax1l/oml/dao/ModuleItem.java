package com.kzax1l.oml.dao;

import java.io.Serializable;

/**
 * 后续可换成{@link android.os.Parcelable}
 */
public class ModuleItem implements Serializable {
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
    public int checkState;
    /**
     * 是否是可删除的项
     */
    public boolean deletable;

    public ModuleItem() {
    }

    public ModuleItem(int id, String name, int orderId, int checkState) {
        this(id, name, orderId, checkState, true);
    }

    public ModuleItem(int id, String name, int orderId, int checkState, boolean deletable) {
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.checkState = checkState;
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

    public void setCheckState(int checkState) {
        this.checkState = checkState;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
}
