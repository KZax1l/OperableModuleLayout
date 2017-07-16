package com.kzax1l.oml;

import android.app.Application;

import com.kzax1l.oml.db.SQLHelper;

public class OMLApplication extends Application implements OMLInitialization {
    private SQLHelper sqlHelper;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 获取数据库Helper
     */
    @Override
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(this);
        return sqlHelper;
    }

    @Override
    public void onTerminate(SQLHelper sqlHelper) {
        super.onTerminate();
        if (sqlHelper != null) {
            sqlHelper.close();
        }
        //整体摧毁的时候调用这个方法
    }
}
