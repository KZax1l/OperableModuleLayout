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
    public int check_state;
    /**
     * 是否是可删除的项
     */
    public boolean deletable;

    public ModuleItem() {
    }

    public ModuleItem(int id, String name, int orderId, int check_state) {
        this(id, name, orderId, check_state, true);
    }

    public ModuleItem(int id, String name, int orderId, int check_state, boolean deletable) {
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.check_state = check_state;
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

    public void setCheck_state(int check_state) {
        this.check_state = check_state;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }
}
