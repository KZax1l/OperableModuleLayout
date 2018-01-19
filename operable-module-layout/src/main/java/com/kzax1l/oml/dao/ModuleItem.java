package com.kzax1l.oml.dao;

import java.io.Serializable;

/**
 * 后续可换成{@link android.os.Parcelable}
 */
public class ModuleItem implements Serializable {
    private static final long serialVersionUID = -6465237897027410019L;
    /**
     * 栏目对应name
     */
    public String name;
    /**
     * 栏目对应的flag
     */
    public String flag;
    /**
     * 栏目在整体中的排序顺序  rank
     */
    public int orderId;
    /**
     * 栏目是否选中
     */
    public int checkState;
    /**
     * 是否是可操作的项
     */
    public boolean operable;

    private boolean isChecked;

    public ModuleItem() {
    }

    public ModuleItem(String name, String flag) {
        this.name = name;
        this.flag = flag;
        this.operable = true;
    }

    public ModuleItem(String name, String flag, boolean operable) {
        this.name = name;
        this.flag = flag;
        this.operable = operable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setCheckState(int checkState) {
        this.checkState = checkState;
    }

    public void setOperable(boolean operable) {
        this.operable = operable;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
