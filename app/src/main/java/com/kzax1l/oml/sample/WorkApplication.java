package com.kzax1l.oml.sample;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kzax1l.oml.OMLApplicationAgent;
import com.kzax1l.oml.OMLModuleOptions;
import com.kzax1l.oml.OMLModuleProvider;
import com.kzax1l.oml.dao.ModuleItem;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-01-11.
 *
 * @author kzaxil
 * @since 1.0.1
 */
public class WorkApplication extends Application implements OMLModuleProvider, OMLModuleOptions {
    private RefWatcher refWatcher;
    private OMLApplicationAgent mAgent = new OMLApplicationAgent(this, this, this);

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = setupLeakCanary();
        mAgent.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mAgent.onTerminate();
    }

    private RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    /**
     * 调用{@link RefWatcher#watch(Object)}来监听可能内存泄漏的对象（如：Fragment对象）
     * <p>
     * LeakCanary在调用{@link LeakCanary#install(Application)}方法时会启动一个ActivityRefWatcher类
     * ，它用于自动监控Activity执行onDestroy方法之后是否发生内存泄露
     */
    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    @Override
    public List<ModuleItem> available() {
        List<ModuleItem> defaultModules = new ArrayList<>();
        defaultModules.add(new ModuleItem("场所档案", "module_place"));
        defaultModules.add(new ModuleItem("身份验证", "module_identification"));
        defaultModules.add(new ModuleItem("场所检查", "module_enterprise"));
        defaultModules.add(new ModuleItem("现场勘查", "module_spot"));
        defaultModules.add(new ModuleItem("特行年检", "module_annual_survey"));
        defaultModules.add(new ModuleItem("快递抽查", "module_deliver"));
        defaultModules.add(new ModuleItem("报警/预警处理", "module_ring"));
        defaultModules.add(new ModuleItem("法律法规", "module_law"));
        return defaultModules;
    }

    @Override
    public List<ModuleItem> unavailable() {
        List<ModuleItem> defaultModules = new ArrayList<>();
        defaultModules.add(new ModuleItem("危化盘库异常处理", "module_dangerous"));
        defaultModules.add(new ModuleItem("旅馆验收", "module_hotel"));
        return defaultModules;
    }

    @Override
    public View moduleLayout(Context context, ViewGroup parent) {
        return LayoutInflater.from(this).inflate(R.layout.item_module, parent, false);
    }

    @Override
    public boolean moduleAdapter(View parent, ModuleItem item) {
        return false;
    }

    @Override
    public int version() {
        return 0;
    }
}
