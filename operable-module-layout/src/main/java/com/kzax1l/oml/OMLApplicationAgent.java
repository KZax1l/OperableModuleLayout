package com.kzax1l.oml;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.kzax1l.oml.dao.ModuleItem;
import com.kzax1l.oml.dao.ModuleManager;
import com.kzax1l.oml.db.OMLSqlHelper;

import java.util.List;

/**
 * Created by Administrator on 2018-01-15.
 *
 * @author kzaxil
 * @since 1.0.1
 */
@SuppressWarnings("WeakerAccess")
public class OMLApplicationAgent implements OMLModuleManager, OMLModuleProvider, OMLModuleOptions {
    private Application mContext;
    private OMLSqlHelper mSqlHelper;
    private ModuleManager mManager;
    private OMLModuleOptions mOptions;
    private OMLModuleProvider mProvider;

    public OMLApplicationAgent(@NonNull Application cxt,
                               @NonNull OMLModuleOptions options,
                               @NonNull OMLModuleProvider provider) {
        mContext = cxt;
        mOptions = options;
        mProvider = provider;
        mManager = new ModuleManager(getSQLHelper(), provider);
    }

    /**
     * @see Application#onCreate()
     */
    public void onCreate() {
        OMLInitializer.initialize(this);
    }

    /**
     * @see Application#onTerminate()
     */
    public void onTerminate() {
        if (mSqlHelper != null) mSqlHelper.close();
    }

    @Override
    public List<ModuleItem> available() {
        return mProvider.available();
    }

    @Override
    public List<ModuleItem> unavailable() {
        return mProvider.unavailable();
    }

    @Override
    public OMLSqlHelper getSQLHelper() {
        if (mSqlHelper == null) mSqlHelper = new OMLSqlHelper(mContext, version());
        return mSqlHelper;
    }

    @Override
    public ModuleManager getModuleManager() {
        return mManager;
    }

    @Override
    public OMLModuleOptions getModuleOptions() {
        return mOptions;
    }

    @Override
    public View moduleLayout(Context context, ViewGroup parent) {
        return mOptions.moduleLayout(context, parent);
    }

    @Override
    public int version() {
        return mOptions.version();
    }
}
