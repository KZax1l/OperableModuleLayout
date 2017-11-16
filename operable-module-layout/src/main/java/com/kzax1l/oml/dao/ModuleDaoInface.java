package com.kzax1l.oml.dao;

import android.content.ContentValues;

import java.util.List;
import java.util.Map;

public interface ModuleDaoInface {

	public boolean addCache(ModuleItem item);

	public boolean deleteCache(String whereClause, String[] whereArgs);

	public boolean updateCache(ContentValues values, String whereClause,
                               String[] whereArgs);

	public Map<String, String> viewCache(String selection,
                                         String[] selectionArgs);

	public List<Map<String, String>> listCache(String selection,
                                               String[] selectionArgs);

	public void clearFeedTable();
}
