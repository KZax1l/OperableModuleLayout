package com.kzax1l.oml.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kzax1l.oml.db.OMLSqlHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ModuleDao implements ModuleDaoInterface {

    private OMLSqlHelper helper = null;

    ModuleDao(Context context) {
        helper = new OMLSqlHelper(context);
    }

    @Override
    public boolean addCache(ModuleItem item) {
        boolean flag = false;
        SQLiteDatabase database = null;
        long id;
        try {
            database = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(OMLSqlHelper.OML_MODULE_ID, item.id);
            values.put(OMLSqlHelper.OML_MODULE_NAME, item.name);
            values.put(OMLSqlHelper.OML_MODULE_ORDER_ID, item.orderId);
            values.put(OMLSqlHelper.OML_MODULE_CHECK_STATE, item.check_state);
            values.put(OMLSqlHelper.OML_MODULE_OPERABLE, item.deletable);
            id = database.insert(OMLSqlHelper.OML_MODULE_TABLE_NAME, null, values);
            flag = id != -1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return flag;
    }

    @Override
    public boolean deleteCache(String whereClause, String[] whereArgs) {
        boolean flag = false;
        SQLiteDatabase database = null;
        int count;
        try {
            database = helper.getWritableDatabase();
            count = database.delete(OMLSqlHelper.OML_MODULE_TABLE_NAME, whereClause, whereArgs);
            flag = count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return flag;
    }

    @Override
    public boolean updateCache(ContentValues values, String whereClause,
                               String[] whereArgs) {
        boolean flag = false;
        SQLiteDatabase database = null;
        int count;
        try {
            database = helper.getWritableDatabase();
            count = database.update(OMLSqlHelper.OML_MODULE_TABLE_NAME, values, whereClause, whereArgs);
            flag = count > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return flag;
    }

    @Override
    public Map<String, String> viewCache(String selection, String[] selectionArgs) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        Map<String, String> map = new HashMap<>();
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(true, OMLSqlHelper.OML_MODULE_TABLE_NAME, null, selection,
                    selectionArgs, null, null, null, null);
            int cols_len = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_values = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_values == null) {
                        cols_values = "";
                    }
                    map.put(cols_name, cols_values);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return map;
    }

    @Override
    public List<Map<String, String>> listCache(String selection, String[] selectionArgs) {
        List<Map<String, String>> list = new ArrayList<>();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(false, OMLSqlHelper.OML_MODULE_TABLE_NAME, null, selection, selectionArgs, null, null, null, null);
            int cols_len = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_values = cursor.getString(cursor.getColumnIndex(cols_name));
                    if (cols_values == null) {
                        cols_values = "";
                    }
                    map.put(cols_name, cols_values);
                }
                list.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public void clearFeedTable() {
        String sql = "DELETE FROM " + OMLSqlHelper.OML_MODULE_TABLE_NAME + ";";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(sql);
        revertSeq();
    }

    private void revertSeq() {
        String sql = "update sqlite_sequence set seq=0 where name='"
                + OMLSqlHelper.OML_MODULE_TABLE_NAME + "'";
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(sql);
    }

}
