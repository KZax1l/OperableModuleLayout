package com.kzax1l.oml;

import android.app.Application;

import com.kzax1l.oml.dao.ModuleItem;
import com.kzax1l.oml.dao.ModuleManager;
import com.kzax1l.oml.db.OMLSqlHelper;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class OMLApplication extends Application implements OMLModuleManager, OMLModuleProvider {
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
        return new ModuleManager(getSQLHelper(), this);
    }

    @Override
    public List<ModuleItem> available() {
        List<ModuleItem> defaultModules = new ArrayList<>();
        defaultModules.add(new ModuleItem("推荐", "tj"));
        defaultModules.add(new ModuleItem("热点", "rd"));
        defaultModules.add(new ModuleItem("杭州", "hz"));
        defaultModules.add(new ModuleItem("时尚", "ss"));
        defaultModules.add(new ModuleItem("科技", "kj"));
        defaultModules.add(new ModuleItem("体育", "ty"));
        defaultModules.add(new ModuleItem("军事", "js"));
        defaultModules.add(new ModuleItem("娱乐", "yl"));
        return defaultModules;
    }

    @Override
    public List<ModuleItem> unavailable() {
        List<ModuleItem> defaultModules = new ArrayList<>();
        defaultModules.add(new ModuleItem("财经", "cj"));
        defaultModules.add(new ModuleItem("汽车", "qc"));
        defaultModules.add(new ModuleItem("房产", "fc"));
        defaultModules.add(new ModuleItem("社会", "sh"));
        defaultModules.add(new ModuleItem("情感", "qg"));
        defaultModules.add(new ModuleItem("女人", "nr"));
        defaultModules.add(new ModuleItem("旅游", "ly"));
        defaultModules.add(new ModuleItem("健康", "jk"));
        defaultModules.add(new ModuleItem("美女", "mn"));
        defaultModules.add(new ModuleItem("游戏", "yx"));
        defaultModules.add(new ModuleItem("数码", "sm"));
        return defaultModules;
    }
}
