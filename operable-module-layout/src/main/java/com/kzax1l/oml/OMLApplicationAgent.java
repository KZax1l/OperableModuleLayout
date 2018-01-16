package com.kzax1l.oml;

import android.app.Application;
import android.support.annotation.NonNull;

import com.kzax1l.oml.dao.ModuleItem;
import com.kzax1l.oml.dao.ModuleManager;
import com.kzax1l.oml.db.OMLSqlHelper;

import java.util.List;

import static com.kzax1l.oml.db.OMLSqlHelper.OML_DB_VERSION;

/**
 * Created by Administrator on 2018-01-15.
 *
 * @author kzaxil
 * @since 1.0.1
 */
@SuppressWarnings("WeakerAccess")
public class OMLApplicationAgent implements OMLModuleManager, OMLModuleProvider {
    private Application mContext;
    private OMLSqlHelper mSqlHelper;
    private ModuleManager mManager;
    private OMLModuleProvider mProvider;

    public OMLApplicationAgent(@NonNull Application cxt, @NonNull OMLModuleProvider provider) {
        mContext = cxt;
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
        if (mSqlHelper == null) mSqlHelper = new OMLSqlHelper(mContext, getVersion());
        return mSqlHelper;
    }

    @Override
    public ModuleManager getModuleManager() {
        return mManager;
    }

    @Override
    public int getVersion() {
        return OML_DB_VERSION;
    }
}
