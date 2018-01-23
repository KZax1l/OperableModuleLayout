package com.andova.oml.dao;

import android.content.ContentValues;

import java.util.List;
import java.util.Map;

interface ModuleDaoInterface {
    boolean addCache(ModuleItem item);

    boolean deleteCache(String whereClause, String[] whereArgs);

    boolean updateCache(ContentValues values, String whereClause, String[] whereArgs);

    Map<String, String> viewCache(String selection, String[] selectionArgs);

    List<Map<String, String>> listCache(String selection, String[] selectionArgs);

    void clearFeedTable();
}
