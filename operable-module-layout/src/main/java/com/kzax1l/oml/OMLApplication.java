package com.kzax1l.oml;

import android.app.Application;

import com.kzax1l.oml.dao.ModuleItem;
import com.kzax1l.oml.dao.ModuleManager;
import com.kzax1l.oml.db.OMLSqlHelper;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class OMLApplication extends Application implements OMLManager, OMLDataProvider {
    private OMLSqlHelper mSqlHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        OMLInitializer.initialize(this);
    }

    /**
     * 获取数据库Helper
     */
    @Override
    public OMLSqlHelper getSQLHelper() {
        if (mSqlHelper == null)
            mSqlHelper = new OMLSqlHelper(this);
        return mSqlHelper;
    }

    @Override
    public void onTerminate(OMLSqlHelper sqlHelper) {
        super.onTerminate();
        if (sqlHelper != null) {
            sqlHelper.close();
        }
        //整体摧毁的时候调用这个方法
    }

    @Override
    public ModuleManager getModuleManager() {
        return ModuleManager.getManager(getSQLHelper(), this);
    }

    @Override
    public List<ModuleItem> available() {
        List<ModuleItem> defaultModules = new ArrayList<>();
        defaultModules.add(new ModuleItem(1, "推荐", 1, 1));
        defaultModules.add(new ModuleItem(2, "热点", 2, 1));
        defaultModules.add(new ModuleItem(3, "杭州", 3, 1));
        defaultModules.add(new ModuleItem(4, "时尚", 4, 1));
        defaultModules.add(new ModuleItem(5, "科技", 5, 1));
        defaultModules.add(new ModuleItem(6, "体育", 6, 1));
        defaultModules.add(new ModuleItem(7, "军事", 7, 1));
        defaultModules.add(new ModuleItem(19, "娱乐", 12, 0));
        return defaultModules;
    }

    @Override
    public List<ModuleItem> unavailable() {
        List<ModuleItem> defaultModules = new ArrayList<>();
        defaultModules.add(new ModuleItem(8, "财经", 1, 0));
        defaultModules.add(new ModuleItem(9, "汽车", 2, 0));
        defaultModules.add(new ModuleItem(10, "房产", 3, 0));
        defaultModules.add(new ModuleItem(11, "社会", 4, 0));
        defaultModules.add(new ModuleItem(12, "情感", 5, 0));
        defaultModules.add(new ModuleItem(13, "女人", 6, 0));
        defaultModules.add(new ModuleItem(14, "旅游", 7, 0));
        defaultModules.add(new ModuleItem(15, "健康", 8, 0));
        defaultModules.add(new ModuleItem(16, "美女", 9, 0));
        defaultModules.add(new ModuleItem(17, "游戏", 10, 0));
        defaultModules.add(new ModuleItem(18, "数码", 11, 0));
        return defaultModules;
    }
}
