package com.kzax1l.oml.sample;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView tvName = parent.findViewById(R.id.tv_module_name);
        ImageView ivIcon = parent.findViewById(R.id.iv_module_icon);
        ImageView ivType = parent.findViewById(R.id.iv_module_type);
        tvName.setText(item.name);
        switch (item.flag) {
            case "module_place":
                ivIcon.setImageResource(R.mipmap.ic_module_place);
                break;
            case "module_identification":
                ivIcon.setImageResource(R.mipmap.ic_module_identification);
                break;
            case "module_enterprise":
                ivIcon.setImageResource(R.mipmap.ic_module_enterprise);
                break;
            case "module_spot":
                ivIcon.setImageResource(R.mipmap.ic_module_spot);
                break;
            case "module_annual_survey":
                ivIcon.setImageResource(R.mipmap.ic_module_annual_survey);
                break;
            case "module_deliver":
                ivIcon.setImageResource(R.mipmap.ic_module_deliver);
                break;
            case "module_ring":
                ivIcon.setImageResource(R.mipmap.ic_module_ring);
                break;
            case "module_law":
                ivIcon.setImageResource(R.mipmap.ic_module_law);
                break;
            case "module_dangerous":
                ivIcon.setImageResource(R.mipmap.ic_module_dangerous);
                break;
            case "module_hotel":
                ivIcon.setImageResource(R.mipmap.ic_module_hotel);
                break;
        }
        if (item.isChecked()) {
            ivType.setImageResource(R.mipmap.ic_delete);
        } else {
            ivType.setImageResource(R.mipmap.ic_add);
        }
        return true;
    }

    @Override
    public int version() {
        return 0;
    }
}
