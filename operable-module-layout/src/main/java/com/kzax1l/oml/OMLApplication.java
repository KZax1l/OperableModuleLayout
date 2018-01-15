package com.kzax1l.oml;

import android.app.Application;

import com.kzax1l.oml.dao.ModuleItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated Use {@link OMLApplicationAgent} instead
 */
public class OMLApplication extends Application implements OMLModuleProvider {
    private OMLApplicationAgent mAgent = new OMLApplicationAgent(this, this);

    @Override
    public void onCreate() {
        super.onCreate();
        mAgent.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mAgent.onTerminate();
    }

    @Override
    public List<ModuleItem> available() {
        List<ModuleItem> defaultModules = new ArrayList<>();
        defaultModules.add(new ModuleItem("推荐", "tj", false));
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
