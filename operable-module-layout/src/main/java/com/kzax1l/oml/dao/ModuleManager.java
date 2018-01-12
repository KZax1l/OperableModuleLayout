package com.kzax1l.oml.dao;

import android.database.SQLException;

import com.kzax1l.oml.OMLDataProvider;
import com.kzax1l.oml.db.OMLSqlHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kzax1l.oml.db.OMLSqlHelper.OML_MODULE_CHECK_STATE;
import static com.kzax1l.oml.db.OMLSqlHelper.OML_MODULE_CHECK_STATE_CHECKED;
import static com.kzax1l.oml.db.OMLSqlHelper.OML_MODULE_CHECK_STATE_UNCHECKED;
import static com.kzax1l.oml.db.OMLSqlHelper.OML_MODULE_FLAG;
import static com.kzax1l.oml.db.OMLSqlHelper.OML_MODULE_NAME;
import static com.kzax1l.oml.db.OMLSqlHelper.OML_MODULE_OPERABLE;
import static com.kzax1l.oml.db.OMLSqlHelper.OML_MODULE_ORDER_ID;

public class ModuleManager {
    private static ModuleManager sModuleManager;
    /**
     * 默认的用户选择频道列表
     */
    private final List<ModuleItem> mDefaultCheckedModules;
    /**
     * 默认的其他频道列表
     */
    private final List<ModuleItem> mDefaultUncheckedModules;

    private ModuleDao mModuleDao;

    /**
     * 判断数据库中是否存在用户数据
     */
    private boolean mIsExist = false;

    private ModuleManager(OMLSqlHelper paramDBHelper, OMLDataProvider provider) throws SQLException {
        if (provider == null)
            throw new NullPointerException("OMLDataProvider can not be null!");
        if (mModuleDao == null)
            mModuleDao = new ModuleDao(paramDBHelper.getContext());
        if (provider.available() != null) {
            mDefaultCheckedModules = provider.available();
        } else {
            mDefaultCheckedModules = new ArrayList<>();
        }
        if (provider.unavailable() != null) {
            mDefaultUncheckedModules = provider.unavailable();
        } else {
            mDefaultUncheckedModules = new ArrayList<>();
        }
    }

    /**
     * 初始化频道管理类
     */
    public static ModuleManager getManager(OMLSqlHelper dbHelper, OMLDataProvider provider) throws SQLException {
        if (sModuleManager == null)
            sModuleManager = new ModuleManager(dbHelper, provider);
        return sModuleManager;
    }

    /**
     * 清除所有的频道
     */
    public void deleteAllModules() {
        mModuleDao.clearFeedTable();
    }

    /**
     * 获取其他的频道
     *
     * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
     */
    public List<ModuleItem> getCheckedModules() {
        Object cacheList = mModuleDao.listCache(OML_MODULE_CHECK_STATE + "= ?", new String[]{String.valueOf(OML_MODULE_CHECK_STATE_CHECKED)});
        List<ModuleItem> list = new ArrayList<>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            mIsExist = true;
            List<Map<String, String>> mapList = (List) cacheList;
            int count = mapList.size();
            for (int i = 0; i < count; i++) {
                ModuleItem navigate = new ModuleItem();
                navigate.setName(mapList.get(i).get(OML_MODULE_NAME));
                navigate.setFlag(mapList.get(i).get(OML_MODULE_FLAG));
                navigate.setOrderId(Integer.valueOf(mapList.get(i).get(OML_MODULE_ORDER_ID)));
                navigate.setCheckState(Integer.valueOf(mapList.get(i).get(OML_MODULE_CHECK_STATE)));
                // 由于SQLite中获取到的Boolean对象是一个数字，1表示true
                navigate.setOperable(mapList.get(i).get(OML_MODULE_OPERABLE).equals("1"));
                list.add(navigate);
            }
            return list;
        }
        if (mIsExist) {
            return list;
        }
        initDefaultModules();
        return mDefaultCheckedModules;
    }

    /**
     * 获取其他的频道
     *
     * @return 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
     */
    public List<ModuleItem> getUncheckedModules() {
        Object cacheList = mModuleDao.listCache(OML_MODULE_CHECK_STATE + "= ?", new String[]{String.valueOf(OML_MODULE_CHECK_STATE_UNCHECKED)});
        List<ModuleItem> list = new ArrayList<>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            List<Map<String, String>> mapList = (List) cacheList;
            int count = mapList.size();
            for (int i = 0; i < count; i++) {
                ModuleItem navigate = new ModuleItem();
                navigate.setName(mapList.get(i).get(OML_MODULE_NAME));
                navigate.setFlag(mapList.get(i).get(OML_MODULE_FLAG));
                navigate.setOrderId(Integer.valueOf(mapList.get(i).get(OML_MODULE_ORDER_ID)));
                navigate.setCheckState(Integer.valueOf(mapList.get(i).get(OML_MODULE_CHECK_STATE)));
                // 由于SQLite中获取到的Boolean对象是一个数字，1表示true
                navigate.setOperable(mapList.get(i).get(OML_MODULE_OPERABLE).equals("1"));
                list.add(navigate);
            }
            return list;
        }
        if (mIsExist) {
            return list;
        }
        cacheList = mDefaultUncheckedModules;
        return (List<ModuleItem>) cacheList;
    }

    /**
     * 保存用户频道到数据库
     */
    public void saveCheckedModules(List<ModuleItem> modules) {
        for (int i = 0; i < modules.size(); i++) {
            ModuleItem item = modules.get(i);
            item.setOrderId(i);
            item.setCheckState(OML_MODULE_CHECK_STATE_CHECKED);
            mModuleDao.addCache(item);
        }
    }

    /**
     * 保存其他频道到数据库
     */
    public void saveUncheckedModules(List<ModuleItem> modules) {
        for (int i = 0; i < modules.size(); i++) {
            ModuleItem item = modules.get(i);
            item.setOrderId(i);
            item.setCheckState(OML_MODULE_CHECK_STATE_UNCHECKED);
            mModuleDao.addCache(item);
        }
    }

    /**
     * 初始化数据库内的频道数据
     */
    private void initDefaultModules() {
        deleteAllModules();
        saveCheckedModules(mDefaultCheckedModules);
        saveUncheckedModules(mDefaultUncheckedModules);
    }
}
