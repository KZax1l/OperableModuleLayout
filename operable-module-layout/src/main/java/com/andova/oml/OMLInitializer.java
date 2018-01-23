package com.andova.oml;

import android.support.annotation.NonNull;

import com.andova.oml.dao.ModuleItem;
import com.andova.oml.dao.ModuleManager;
import com.andova.oml.db.OMLSqlHelper;

import java.util.List;

/**
 * Created by Zsago on 2017/7/17.
 *
 * @author Zsago
 */
public class OMLInitializer {
    private static OMLModuleManager sManager;

    @SuppressWarnings("WeakerAccess")
    public static void initialize(@NonNull OMLModuleManager manager) {
        sManager = manager;
    }

    @NonNull
    public static OMLSqlHelper getSQLHelper() {
        return sManager.getSQLHelper();
    }

    public static ModuleManager getModuleManager() {
        return sManager.getModuleManager();
    }

    public static OMLModuleOptions getModuleOptions() {
        return sManager.getModuleOptions();
    }

    /**
     * 获取未添加的模块集
     */
    public static List<ModuleItem> unavailable() {
        return sManager.getModuleManager().getUncheckedModules();
    }

    /**
     * 获取已添加的模块集
     */
    public static List<ModuleItem> available() {
        return sManager.getModuleManager().getCheckedModules();
    }
}
