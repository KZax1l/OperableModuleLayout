package com.andova.oml.dao;

import android.database.SQLException;
import android.support.annotation.NonNull;

import com.andova.oml.OMLModuleProvider;
import com.andova.oml.db.OMLSqlHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.andova.oml.db.OMLSqlHelper.OML_MODULE_CHECK_STATE;
import static com.andova.oml.db.OMLSqlHelper.OML_MODULE_CHECK_STATE_CHECKED;
import static com.andova.oml.db.OMLSqlHelper.OML_MODULE_CHECK_STATE_UNCHECKED;
import static com.andova.oml.db.OMLSqlHelper.OML_MODULE_FLAG;
import static com.andova.oml.db.OMLSqlHelper.OML_MODULE_NAME;
import static com.andova.oml.db.OMLSqlHelper.OML_MODULE_OPERABLE;
import static com.andova.oml.db.OMLSqlHelper.OML_MODULE_ORDER_ID;

public class ModuleManager {
    /**
     * 默认的用户选择频道列表
     */
    private List<ModuleItem> mDefaultCheckedModules;
    /**
     * 默认的其他频道列表
     */
    private List<ModuleItem> mDefaultUncheckedModules;

    private ModuleDao mModuleDao;

    /**
     * 判断数据库中是否存在用户数据
     */
    private boolean mIsExist = false;

    public ModuleManager(@NonNull OMLSqlHelper sqlHelper, @NonNull OMLModuleProvider provider) throws SQLException {
        setModuleProvider(provider);
        if (mModuleDao == null) mModuleDao = new ModuleDao(sqlHelper);
    }

    public void resetModuleProvider(@NonNull OMLModuleProvider provider) {
        if (mDefaultCheckedModules != null) deleteAllModules();
        setModuleProvider(provider);
    }

    public void updateModuleProvider(@NonNull OMLModuleProvider provider) {
        List<ModuleItem> add = new ArrayList<>();
        if (provider.available().size() > 0) {
            for (ModuleItem module : provider.available()) {
                module.setCheckState(OML_MODULE_CHECK_STATE_CHECKED);
                add.add(module);
            }
        }
        if (provider.unavailable().size() > 0) {
            for (ModuleItem module : provider.unavailable()) {
                module.setCheckState(OML_MODULE_CHECK_STATE_UNCHECKED);
                add.add(module);
            }
        }
        if (add.size() == 0) return;
        List<ModuleItem> delete = new ArrayList<>();
        List<ModuleItem> checked = getCheckedModules();
        List<ModuleItem> unchecked = getUncheckedModules();
        if (checked.size() > 0) delete.addAll(checked);
        if (unchecked.size() > 0) delete.addAll(unchecked);
        for (int i = add.size() - 1; i >= 0; i--) {
            ModuleItem item = add.get(i);
            for (int j = delete.size() - 1; j >= 0; j--) {
                ModuleItem module = delete.get(j);
                if (item.name.equals(module.name) && item.flag.equals(module.flag)) {
                    add.remove(item);
                    delete.remove(module);
                }
            }
        }
        for (ModuleItem module : delete) {
            mModuleDao.deleteCache(OML_MODULE_NAME + "=? AND " + OML_MODULE_FLAG + "=?", new String[]{module.name, module.flag});
        }
        checked.clear();
        unchecked.clear();
        checked = getCheckedModules();
        unchecked = getUncheckedModules();
        int checkedOrderId = checked.size() - 1;
        int uncheckedOrderId = unchecked.size() - 1;
        for (ModuleItem module : add) {
            switch (module.checkState) {
                case OML_MODULE_CHECK_STATE_CHECKED:
                    checkedOrderId++;
                    module.setOrderId(checkedOrderId);
                    break;
                case OML_MODULE_CHECK_STATE_UNCHECKED:
                    uncheckedOrderId++;
                    module.setOrderId(uncheckedOrderId);
                    break;
            }
            mModuleDao.addCache(module);
        }
    }

    private void setModuleProvider(@NonNull OMLModuleProvider provider) {
        mDefaultCheckedModules = provider.available() != null ? provider.available() : new ArrayList<ModuleItem>();
        mDefaultUncheckedModules = provider.unavailable() != null ? provider.unavailable() : new ArrayList<ModuleItem>();
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
        if (mIsExist && list.size() > 0) return list;
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
        return mDefaultUncheckedModules;
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
