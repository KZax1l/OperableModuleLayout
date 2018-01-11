package com.kzax1l.oml.sample;

import com.kzax1l.oml.OMLApplication;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Administrator on 2018-01-11.
 *
 * @author kzaxil
 * @since 1.0.1
 */
public class WorkApplication extends OMLApplication {
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = setupLeakCanary();
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
}
