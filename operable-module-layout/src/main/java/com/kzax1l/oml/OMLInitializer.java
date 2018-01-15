package com.kzax1l.oml;

import android.support.annotation.NonNull;

import com.kzax1l.oml.dao.ModuleItem;
import com.kzax1l.oml.dao.ModuleManager;
import com.kzax1l.oml.db.OMLSqlHelper;

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
