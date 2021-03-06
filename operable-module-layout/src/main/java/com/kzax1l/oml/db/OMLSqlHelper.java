package com.kzax1l.oml.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OMLSqlHelper extends SQLiteOpenHelper {
    public static final int OML_DB_VERSION = 1;
    public static final String OML_DB_NAME = "oml.db";// 数据库名称
    public static final String OML_MODULE_TABLE_NAME = "oml_module";//数据表

    public static final String OML_MODULE_ID = "oml_module_id";
    public static final String OML_MODULE_NAME = "oml_module_name";
    public static final String OML_MODULE_ORDER_ID = "oml_module_order_id";
    /**
     * 是否可进行操作
     */
    public static final String OML_MODULE_OPERABLE = "oml_module_operable";
    public static final String OML_MODULE_CHECK_STATE = "oml_module_check_state";
    private Context mContext;

    public OMLSqlHelper(Context context) {
        super(context, OML_DB_NAME, null, OML_DB_VERSION);
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO 创建数据库后，对数据库的操作
        String sql = "create table if not exists "
                + OML_MODULE_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + OML_MODULE_ID + " INTEGER,"
                + OML_MODULE_NAME + " TEXT,"
                + OML_MODULE_ORDER_ID + " INTEGER,"
                + OML_MODULE_CHECK_STATE + " SELECTED,"
                + OML_MODULE_OPERABLE + " BOOLEAN)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 更改数据库版本的操作
        onCreate(db);
    }

}
